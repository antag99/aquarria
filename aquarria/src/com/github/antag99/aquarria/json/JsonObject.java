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

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Stores the data of an json object. This is a regular mapping between
 * strings and objects, except that the types that can be stored in a json objects
 * are limited; null, boolean, number, string, object and array.
 * </p>
 * This implementation was made as the libgdx one was pretty cumbersome to use
 * and did not support comments, which is a non-standard feature that is pretty useful.
 */
public class JsonObject {
	/* boxed/raw values in this object, depending on type */
	private ObjectMap<String, Object> values = new ObjectMap<>();

	public JsonObject() {
	}

	public boolean has(String key) {
		return values.containsKey(key);
	}

	public boolean getBoolean(String key) {
		if (!values.containsKey(key)) {
			throw new IllegalArgumentException("'" + key + "' not present in object");
		}

		Object value = values.get(key);
		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not a boolean");
		}

		return (Boolean) value;
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		if (!values.containsKey(key)) {
			return defaultValue;
		}

		Object value = values.get(key);
		if (!(value instanceof Boolean)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not a boolean");
		}

		return (Boolean) value;
	}

	public float getFloat(String key) {
		if (!values.containsKey(key)) {
			throw new IllegalArgumentException("'" + key + "' not present in object");
		}

		Object value = values.get(key);
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not a number");
		}

		return ((Number) value).floatValue();
	}

	public float getFloat(String key, float defaultValue) {
		if (!values.containsKey(key)) {
			return defaultValue;
		}

		Object value = values.get(key);
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not a number");
		}

		return ((Number) value).floatValue();
	}

	public int getInteger(String key) {
		if (!values.containsKey(key)) {
			throw new IllegalArgumentException("'" + key + "' not present in object");
		}

		Object value = values.get(key);
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not a number");
		}

		return ((Number) value).intValue();
	}

	public int getInteger(String key, int defaultValue) {
		if (!values.containsKey(key)) {
			return defaultValue;
		}

		Object value = values.get(key);
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not a number");
		}

		return ((Number) value).intValue();
	}

	public String getString(String key) {
		if (!values.containsKey(key)) {
			throw new IllegalArgumentException("'" + key + "' not present in object");
		}

		Object value = values.get(key);
		if (!(value instanceof String)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not a string");
		}

		return (String) value;
	}

	public String getString(String key, String defaultValue) {
		if (!values.containsKey(key)) {
			return defaultValue;
		}

		Object value = values.get(key);
		if (!(value instanceof String)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not a string");
		}

		return (String) value;
	}

	public JsonObject getObject(String key) {
		if (!values.containsKey(key)) {
			throw new IllegalArgumentException("'" + key + "' not present in object");
		}

		Object value = values.get(key);
		if (!(value instanceof JsonObject)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not an object");
		}

		return (JsonObject) value;
	}

	public JsonObject getObject(String key, JsonObject defaultValue) {
		if (!values.containsKey(key)) {
			return defaultValue;
		}

		Object value = values.get(key);
		if (!(value instanceof JsonObject)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not an object");
		}

		return (JsonObject) value;
	}

	public JsonArray getArray(String key) {
		if (!values.containsKey(key)) {
			throw new IllegalArgumentException("'" + key + "' not present in object");
		}

		Object value = values.get(key);
		if (!(value instanceof JsonArray)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not an array");
		}

		return (JsonArray) value;
	}

	public JsonArray getArray(String key, JsonArray array) {
		if (!values.containsKey(key)) {
			return array;
		}

		Object value = values.get(key);
		if (!(value instanceof JsonArray)) {
			throw new IllegalArgumentException("the value of '" + key + "' was present but is not an array");
		}

		return (JsonArray) value;
	}

	public void setBoolean(String key, boolean value) {
		values.put(key, value);
	}

	public void setFloat(String key, float value) {
		values.put(key, value);
	}

	public void setInteger(String key, int value) {
		values.put(key, value);
	}

	public void setString(String key, String value) {
		values.put(key, value);
	}

	public void setObject(String key, JsonObject value) {
		values.put(key, value);
	}

	public void setArray(String key, JsonArray value) {
		values.put(key, value);
	}

	public Object get(String key) {
		if (!values.containsKey(key)) {
			throw new IllegalArgumentException("'" + key + "' not present in object");
		}

		return values.get(key);
	}

	public void set(String key, Object value) {
		if (value == null ||
				value instanceof Boolean ||
				value instanceof Number ||
				value instanceof String ||
				value instanceof JsonObject ||
				value instanceof JsonArray) {
			values.put(key, value);
		} else {
			throw new IllegalArgumentException("unsupported value type: " + value.getClass().getSimpleName());
		}
	}
}
