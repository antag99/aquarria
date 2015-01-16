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
package com.github.antag99.aquarria.lua;

import java.io.InputStream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;

public class LuaEnvironment {
	private Globals globals;
	private LuaObject wrapper;
	private FileHandleResolver resolver;

	public LuaEnvironment() {
		globals = JsePlatform.debugGlobals();
		globals.finder = new ResourceFinder() {
			@Override
			public InputStream findResource(String filename) {
				return resolver.resolve(filename).read();
			}
		};
		wrapper = new LuaObject(globals);
		resolver = new InternalFileHandleResolver();
	}

	/**
	 * Gets the {@link FileHandleResolver} used to locate script files
	 */
	public FileHandleResolver getResolver() {
		return resolver;
	}

	/**
	 * Sets the {@link FileHandleResolver} used to locate script files.
	 * By default {@link InternalFileHandleResolver}.
	 */
	public void setResolver(FileHandleResolver resolver) {
		this.resolver = resolver;
	}

	/**
	 * Gets a global variable
	 */
	public LuaObject getGlobal(String key) {
		return new LuaObject(globals.get(key));
	}

	/**
	 * Sets a global variable
	 */
	public void setGlobal(String key, LuaObject value) {
		globals.set(key, value.value);
	}

	/**
	 * Gets the global environment
	 */
	public LuaObject getGlobals() {
		return wrapper;
	}

	/**
	 * Compiles a lua script given it's source code
	 * 
	 * @param script The source code of the script
	 * @param chunkName The name of the chunk
	 */
	public LuaObject compileScript(String script, String chunkName) {
		return new LuaObject(globals.load(script, chunkName));
	}

	/**
	 * Compiles a lua script from the given path
	 */
	public LuaObject loadScript(String path) {
		return new LuaObject(globals.loadfile(path));
	}
}
