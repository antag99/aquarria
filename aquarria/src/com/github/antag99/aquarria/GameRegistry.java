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
package com.github.antag99.aquarria;

import java.util.LinkedList;
import java.util.List;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.item.ItemTypeLoader;
import com.github.antag99.aquarria.json.JsonObject;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.TileTypeLoader;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.tile.WallTypeLoader;

public final class GameRegistry {
	private GameRegistry() { /* don't instantiate */
		throw new AssertionError();
	}

	private static ObjectMap<Class<? extends Type>, ObjectMap<String, Type>> registeredTypes = new ObjectMap<>();
	private static ObjectMap<String, TypeLoader<?>> typeLoadersByExtension = new ObjectMap<>();
	private static ObjectMap<Class<? extends Type>, TypeLoader<?>> typeLoadersByClass = new ObjectMap<>();

	private static ObjectMap<String, Type> getRegisteredTypes(Class<? extends Type> typeClass) {
		ObjectMap<String, Type> types = registeredTypes.get(typeClass);
		if (types == null) {
			types = new ObjectMap<String, Type>();
			registeredTypes.put(typeClass, types);
		}
		return types;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Type> Iterable<T> getTypes(Class<T> typeClass) {
		return (Iterable<T>) getRegisteredTypes(typeClass).values();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Type> T getType(Class<T> typeClass, String internalName) {
		T type = (T) getRegisteredTypes(typeClass).get(internalName);
		if (type == null) {
			throw new IllegalArgumentException(typeClass.getSimpleName() + " not found: " + internalName);
		}
		return type;
	}

	public static void registerTypeLoader(TypeLoader<?> typeLoader) {
		typeLoadersByExtension.put(typeLoader.getExtension(), typeLoader);
		typeLoadersByClass.put(typeLoader.getTypeClass(), typeLoader);
	}

	public static void registerType(Type type) {
		getRegisteredTypes(type.getClass()).put(type.getId(), type);
	}

	public static ItemType getItemType(String internalName) {
		return getType(ItemType.class, internalName);
	}

	public static TileType getTileType(String internalName) {
		return getType(TileType.class, internalName);
	}

	public static WallType getWallType(String internalName) {
		return getType(WallType.class, internalName);
	}

	public static Iterable<ItemType> getItemTypes() {
		return getTypes(ItemType.class);
	}

	public static Iterable<TileType> getTileTypes() {
		return getTypes(TileType.class);
	}

	public static Iterable<WallType> getWallTypes() {
		return getTypes(WallType.class);
	}

	@SuppressWarnings("unchecked")
	private static void loadTypes() {
		/* http://stackoverflow.com/a/9571146 */
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		/* TODO: limit the resources that are scanned? */
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new ResourcesScanner())
				.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))));

		/* find type configuration files */
		for (String typePath : reflections.getResources((resourceName) -> {
			return typeLoadersByExtension.containsKey(new FileHandle(resourceName).extension());
		})) {
			FileHandle file = Gdx.files.internal(typePath);
			JsonObject config = Util.parseJson(file);

			TypeLoader<?> loader = typeLoadersByExtension.get(file.extension());
			Type typeInstance = null;

			try {
				typeInstance = (Type) loader.getTypeClass().newInstance();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}

			typeInstance.setId(config.getString("id"));
			typeInstance.setName(config.getString("name"));
			typeInstance.setConfig(config);

			System.out.println(typeInstance + "=" + typeInstance.getId());

			((TypeLoader<Type>) loader).load(typeInstance, config);

			registerType(typeInstance);
		}

		for (Class<? extends Type> typeClass : registeredTypes.keys()) {
			TypeLoader<?> loader = typeLoadersByClass.get(typeClass);
			for (Type type : getTypes(typeClass)) {
				((TypeLoader<Type>) loader).postLoad(type, type.getConfig());
			}
		}
	}

	public static void initialize() {
		registerTypeLoader(new ItemTypeLoader());
		registerTypeLoader(new TileTypeLoader());
		registerTypeLoader(new WallTypeLoader());

		loadTypes();

		ItemType.air = getItemType("air");
		TileType.air = getTileType("air");
		WallType.air = getWallType("air");
	}

	public static void clear() {
		typeLoadersByClass.clear();
		typeLoadersByExtension.clear();
		registeredTypes.clear();
	}
}
