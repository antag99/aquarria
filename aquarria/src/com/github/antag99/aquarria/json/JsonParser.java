/*******************************************************************************
 * Copyright (c) 2014-2015, Anton Gustafsson
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Aquarria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.github.antag99.aquarria.json;

import java.io.IOException;
import java.io.Reader;

import com.badlogic.gdx.files.FileHandle;

/**
 * Json parser that supports the standard json format in addition to comments.
 * This parser also accepts a trailing semicolon in objects and arrays.
 * Automatically closes the source reader on end of file.
 */
public class JsonParser {
	private Reader reader;
	private String source;
	/* next tokens in the input */
	private int next;
	private int nextButOne;
	/* current position in the input */
	private int line = 1;
	private int column = 1;

	public JsonParser(FileHandle file) {
		this(file.reader(), file.path());
	}

	public JsonParser(Reader reader) {
		this(reader, "");
	}

	public JsonParser(Reader reader, String source) {
		this.reader = reader;
		this.source = source;
	}

	public JsonObject parse() {
		/* buffer those damn leading tokens */
		advance();
		advance();

		/* skip to the first token */
		skipToNext();

		return parseObject();
	}

	/** Advances to the next character in the input stream */
	private void advance() {
		try {
			/* advance position counters */
			if (next == '\n') {
				line++;
				column = 1;
			} else if (next != -1) {
				column++;
			}

			/* read the next token */
			next = nextButOne;
			nextButOne = reader.read();

			/* automatically close the reader on eof */
			if (nextButOne == -1 && next != -1) {
				reader.close();
			}
		} catch (IOException ex) {
			/*
			 * wrap it up in a RuntimeException; we don't
			 * want 'throws IOException' everywhere...
			 */
			throw new RuntimeException(ex);
		}
	}

	/** Consumes the next character if it matches the given character */
	private boolean consume(int ch) {
		skipToNext();
		if (next == ch) {
			advance();
			return true;
		}

		return false;
	}

	/** Creates a new exception containing current location information */
	private RuntimeException syntaxError(String message) {
		return new RuntimeException(source + "[" + line + "," + column + "] " + message);
	}

	/** Skips any whitespace and comments in the input */
	private void skipToNext() {
		skipWhitespace();
		while (next == '/' && (nextButOne == '/' || nextButOne == '*')) {
			advance();
			if (next == '/') {
				advance();
				while (next != '\n') {
					advance();
				}
				advance();
			} else if (next == '*') {
				advance();
				while (next != '*' || nextButOne != '/') {
					if (next == -1) {
						throw syntaxError("eof in long comment");
					}

					advance();
				}
				advance();
				advance();
			}
		}
	}

	/** Skips whitespace to the next token */
	private void skipWhitespace() {
		while (Character.isWhitespace(next)) {
			advance();
		}
	}

	private Object parseValue() {
		/* skip leading whitespace/comments */
		skipToNext();
		switch (next) {
		case '{':
			return parseObject();
		case '[':
			return parseArray();
		case '"':
			return parseString();
		case 't':
		case 'f':
			return parseBoolean();
		case 'n':
			return parseNull();
		}
		if (Character.isDigit(next) || next == '-') {
			return parseNumber();
		}
		if (next == -1) {
			throw syntaxError("value expected");
		}
		throw syntaxError("value expected; unrecognized token '" + (char) next + "'");
	}

	private JsonObject parseObject() {
		if (!consume('{')) {
			throw syntaxError("json object expected");
		}

		JsonObject object = new JsonObject();

		while (next != '}') {
			String key = parseString();
			if (!consume(':')) {
				throw syntaxError("':' expected to separate key/value pair");
			}
			Object value = parseValue();
			if (object.has(key)) {
				throw syntaxError("duplicate key '" + key + "'");
			}
			object.set(key, value);

			if (!consume(',')) {
				break; /* expect object end */
			}
		}

		if (!consume('}')) {
			throw syntaxError("'}' expected to end json object");
		}

		return object;
	}

	private JsonArray parseArray() {
		if (!consume('[')) {
			throw syntaxError("json array expected"); /* shouldn't happen */
		}

		JsonArray array = new JsonArray();

		while (next != ']') {
			array.add(parseValue());
			if (!consume(',')) {
				break; /* expect array end */
			}
		}

		if (!consume(']')) {
			throw syntaxError("']' expected to end json array");
		}

		return array;
	}

	private String parseString() {
		if (!consume('"')) {
			throw syntaxError("key expected"); /* this is always the case */
		}

		StringBuilder str = new StringBuilder();

		while (next != '"') {
			if (next == '\\') { /* parse escape sequence */
				advance();
				switch (next) {
				case '"':
				case '\\':
				case '/':
					str.append((char) next);
					advance();
					break;
				case 'b':
					str.append('\b');
					advance();
					break;
				case 'f':
					str.append('\f');
					advance();
					break;
				case 'n':
					str.append('\n');
					advance();
					break;
				case 'r':
					str.append('\r');
					advance();
					break;
				case 't':
					str.append('\t');
					advance();
					break;
				case 'u':
					int ch = Character.digit(next, 16);
					advance();
					int count = 1;
					do {
						ch *= 16;
						int digit = Character.digit(next, 16);
						if (digit == -1) {
							throw syntaxError("hex digit expected");
						}
						advance();
						ch += digit;
					} while (count++ < 4);
					str.append((char) ch);
					break;
				default:
					throw syntaxError("unrecognized escape character '" + (char) next + "'");
				}
			} else {
				str.append((char) next);
				advance();
			}
		}

		advance();

		return str.toString();
	}

	private float parseNumber() {
		/* validate syntax of the number and use Float#parseFloat */
		StringBuilder str = new StringBuilder();

		if (consume('-')) {
			str.append('-');
		}

		while (Character.isDigit(next)) {
			str.append((char) next);
			advance();
		}

		if (consume('.')) {
			str.append('.');
			while (Character.isDigit(next)) {
				str.append((char) next);
				advance();
			}
		}

		if (consume('E') || consume('e')) {
			str.append('e');
			if (next == '+' || next == '-') {
				str.append((char) next);
				advance();
			}
			while (Character.isDigit(next)) {
				str.append((char) next);
				advance();
			}
		}

		return Float.parseFloat(str.toString());
	}

	private boolean parseBoolean() {
		if (consume('t')) {
			if (consume('r') && consume('u') && consume('e')) {
				return true;
			}
		} else if (consume('f')) {
			if (consume('a') && consume('l') && consume('s') && consume('e')) {
				return false;
			}
		}
		throw syntaxError("value expected; unrecognized token '" + (char) next + "'");
	}

	private Object parseNull() {
		if (consume('n') && consume('u') && consume('l') && consume('l')) {
			return null;
		}

		throw syntaxError("value expected; unrecognized token '" + (char) next + "'");
	}
}
