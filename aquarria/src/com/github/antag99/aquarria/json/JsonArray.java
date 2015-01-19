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

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

public class JsonArray implements Iterable<Object> {
	private Array<Object> elements = new Array<>();

	public JsonArray() {
	}

	public boolean getBoolean(int index) {
		Object value = elements.get(index);

		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not a boolean");
		}

		return (Boolean) value;
	}

	public float getFloat(int index) {
		Object value = elements.get(index);

		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not a number");
		}

		return ((Number) value).floatValue();
	}

	public int getInteger(int index) {
		Object value = elements.get(index);

		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not a number");
		}

		return ((Number) value).intValue();
	}

	public String getString(int index) {
		Object value = elements.get(index);

		if (!(value instanceof String)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not a string");
		}

		return (String) value;
	}

	public JsonObject getObject(int index) {
		Object value = elements.get(index);

		if (!(value instanceof JsonObject)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not an object");
		}

		return (JsonObject) value;
	}

	public JsonArray getArray(int index) {
		Object value = elements.get(index);

		if (!(value instanceof JsonArray)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not an array");
		}

		return (JsonArray) value;
	}

	public boolean getBoolean(int index, boolean defaultValue) {
		if (index >= elements.size) {
			return defaultValue;
		}

		Object value = elements.get(index);

		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not a boolean");
		}

		return (Boolean) value;
	}

	public float getFloat(int index, float defaultValue) {
		if (index >= elements.size) {
			return defaultValue;
		}

		Object value = elements.get(index);

		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not a number");
		}

		return ((Number) value).floatValue();
	}

	public int getInteger(int index, int defaultValue) {
		if (index >= elements.size) {
			return defaultValue;
		}

		Object value = elements.get(index);

		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not a number");
		}

		return ((Number) value).intValue();
	}

	public String getString(int index, String defaultValue) {
		if (index >= elements.size) {
			return defaultValue;
		}

		Object value = elements.get(index);

		if (!(value instanceof String)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not a string");
		}

		return (String) value;
	}

	public JsonObject getObject(int index, JsonObject defaultValue) {
		if (index >= elements.size) {
			return defaultValue;
		}

		Object value = elements.get(index);

		if (!(value instanceof JsonObject)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not an object");
		}

		return (JsonObject) value;
	}

	public JsonArray getArray(int index, JsonArray defaultValue) {
		if (index >= elements.size) {
			return defaultValue;
		}

		Object value = elements.get(index);

		if (!(value instanceof JsonArray)) {
			throw new IllegalArgumentException("value at index " + index + " was present but is not an array");
		}

		return (JsonArray) value;
	}

	public void addBoolean(boolean value) {
		elements.add(value);
	}

	public void addFloat(float value) {
		elements.add(value);
	}

	public void addInteger(int value) {
		elements.add(value);
	}

	public void addString(String value) {
		elements.add(value);
	}

	public void addObject(JsonObject value) {
		elements.add(value);
	}

	public void addArray(JsonArray value) {
		elements.add(value);
	}

	public int length() {
		return elements.size;
	}

	@Override
	public Iterator<Object> iterator() {
		return elements.iterator();
	}

	public void add(Object value) {
		if (value == null ||
				value instanceof Boolean ||
				value instanceof Number ||
				value instanceof String ||
				value instanceof JsonObject ||
				value instanceof JsonArray) {
			elements.add(value);
		} else {
			throw new IllegalArgumentException("unsupported value type: " + value.getClass().getSimpleName());
		}
	}
}
