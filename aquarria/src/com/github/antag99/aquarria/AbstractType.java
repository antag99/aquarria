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
package com.github.antag99.aquarria;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class AbstractType {
	private static ObjectMap<Class<?>, ObjectMap<String, AbstractType>> typesByName = new ObjectMap<>();
	private static ObjectMap<Class<?>, Array<AbstractType>> types = new ObjectMap<>();

	@SuppressWarnings("unchecked")
	public static <T extends AbstractType> Array<T> getTypes(Class<T> typeClass) {
		return (Array<T>) types.get(typeClass);
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractType> T forName(Class<T> typeClass, String internalName) {
		return (T) typesByName.get(typeClass).get(internalName);
	}

	static void queueAll(AssetManager assetManager) {
		for (Array<AbstractType> typeList : types.values()) {
			for (AbstractType type : typeList) {
				type.queueAssets(assetManager);
			}
		}
	}

	static void getAll(AssetManager assetManager) {
		for (Array<AbstractType> typeList : types.values()) {
			for (AbstractType type : typeList) {
				type.getAssets(assetManager);
			}
		}
	}

	private final String internalName;

	public AbstractType(String internalName) {
		if (internalName == null)
			throw new IllegalArgumentException();

		this.internalName = internalName;

		ObjectMap<String, AbstractType> typeMap = typesByName.get(getTypeClass());
		if (typeMap == null) {
			typeMap = new ObjectMap<String, AbstractType>();
			typesByName.put(getTypeClass(), typeMap);
		}

		typeMap.put(internalName, this);

		Array<AbstractType> typeList = types.get(getTypeClass());
		if (typeList == null) {
			typeList = new Array<AbstractType>(getTypeClass());
			types.put(getTypeClass(), typeList);
		}

		typeList.add(this);
	}

	public String getInternalName() {
		return internalName;
	}

	protected void queueAssets(AssetManager assetManager) {
	}

	protected void getAssets(AssetManager assetManager) {
	}

	protected Class<? extends AbstractType> getTypeClass() {
		return getClass();
	}
}
