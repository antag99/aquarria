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

import java.util.Arrays;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class LuaArguments {
	private LuaObject[] arguments;

	LuaArguments(Varargs varargs) {
		arguments = new LuaObject[varargs.narg()];
		for (int i = 0; i < arguments.length; ++i) {
			arguments[i] = new LuaObject(varargs.arg(i + 1));
		}
	}

	Varargs toVarargs() {
		return toVarargs(arguments);
	}

	static Varargs toVarargs(LuaObject[] arguments) {
		LuaValue[] raw = new LuaValue[arguments.length];
		for (int i = 0; i < raw.length; ++i) {
			raw[i] = arguments[i].value;
		}
		return LuaValue.varargsOf(raw);
	}

	public LuaArguments(LuaObject... arguments) {
		this.arguments = arguments;
	}

	/**
	 * Gets the argument at the given 0-based index
	 */
	public LuaObject get(int index) {
		/* attempting to access arguments with negative indexes is a bug */
		return index < arguments.length ? arguments[index] : LuaObject.NIL;
	}

	public int count() {
		return arguments.length;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(arguments);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof LuaArguments) && Arrays.equals(arguments, ((LuaArguments) obj).arguments);
	}
}
