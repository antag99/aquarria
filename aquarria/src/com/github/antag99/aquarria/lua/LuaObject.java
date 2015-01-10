/*******************************************************************************
 * Copyright (c) 2014, Anton Gustafsson
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
package com.github.antag99.aquarria.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public final class LuaObject {
	public static final LuaObject NIL = new LuaObject(LuaValue.NIL);

	private static final LuaObject FALSE = new LuaObject(LuaValue.valueOf(false));
	private static final LuaObject TRUE = new LuaObject(LuaValue.valueOf(true));

	public static LuaObject valueOf(boolean value) {
		return value ? TRUE : FALSE;
	}

	public static LuaObject valueOf(float value) {
		return new LuaObject(LuaValue.valueOf(value));
	}

	public static LuaObject valueOf(String value) {
		return new LuaObject(LuaValue.valueOf(value));
	}

	public static LuaObject valueOf(Object value) {
		return new LuaObject(LuaValue.userdataOf(value));
	}

	public static LuaObject valueOf(LuaCallback callback) {
		return new LuaObject(new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return callback.call(new LuaArguments(args)).toVarargs();
			}
		});
	}

	public static LuaObject createTable() {
		return new LuaObject(new LuaTable());
	}

	LuaValue value;

	LuaObject(LuaValue value) {
		this.value = value;
	}

	public LuaType getType() {
		switch (value.type()) {
		case LuaValue.TNIL:
			return LuaType.NIL;
		case LuaValue.TBOOLEAN:
			return LuaType.BOOLEAN;
		case LuaValue.TNUMBER:
			return LuaType.NUMBER;
		case LuaValue.TSTRING:
			return LuaType.STRING;
		case LuaValue.TFUNCTION:
			return LuaType.FUNCTION;
		case LuaValue.TTHREAD:
			return LuaType.THREAD;
		case LuaValue.TLIGHTUSERDATA:
		case LuaValue.TUSERDATA:
			return LuaType.USERVALUE;
		case LuaValue.TTABLE:
			return LuaType.TABLE;
		default:
			throw new AssertionError("unknown type: " + value.type());
		}
	}

	public LuaObject getMetatable() {
		return new LuaObject(value.getmetatable());
	}

	public void setMetatable(LuaObject metatable) {
		value.setmetatable(value);
	}

	public boolean isNil() {
		return value.isnil();
	}

	public boolean isBoolean() {
		return value.isboolean();
	}

	public boolean isString() {
		return value.isstring();
	}

	public boolean isFunction() {
		return value.isfunction();
	}

	public boolean isThread() {
		return value.isthread();
	}

	public boolean isUservalue() {
		return value.isuserdata();
	}

	public boolean isUservalue(Class<?> type) {
		return value.isuserdata(type);
	}

	public boolean isTable() {
		return value.istable();
	}

	public boolean convertBoolean() {
		return value.checkboolean();
	}

	public String convertString() {
		return value.checkjstring();
	}

	public float convertNumber() {
		return (float) value.checkdouble();
	}

	public Object convertUservalue() {
		return value.checkuserdata();
	}

	public <T> T convertUservalue(Class<T> type) {
		return type.cast(value.checkuserdata(type));
	}

	public LuaObject get(LuaObject key) {
		return new LuaObject(value.get(key.value));
	}

	public LuaObject get(String key) {
		return new LuaObject(value.get(key));
	}

	public void set(LuaObject key, LuaObject value) {
		this.value.set(key.value, value.value);
	}

	public void set(String key, LuaObject value) {
		this.value.set(key, value.value);
	}

	public LuaArguments call() {
		return new LuaArguments(value.invoke());
	}

	public LuaArguments call(LuaObject... arguments) {
		return new LuaArguments(value.invoke(LuaArguments.toVarargs(arguments)));
	}

	public LuaArguments call(LuaArguments arguments) {
		return new LuaArguments(value.invoke(arguments.toVarargs()));
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof LuaObject) && value.equals(((LuaObject) obj).value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
