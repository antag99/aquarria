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

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.entity.EntityType;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.util.Direction;

public final class GameRegistry {
	private GameRegistry() {
		throw new AssertionError();
	}

	// Mappings of internal names to concrete types
	private static ObjectMap<Class<? extends Type>, ObjectMap<String, Type>> registeredTypes = new ObjectMap<>();
	private static ObjectMap<String, TypeLoader<?>> typeLoadersByExtension = new ObjectMap<>();
	private static ObjectMap<Class<? extends Type>, TypeLoader<?>> typeLoadersByClass = new ObjectMap<>();

	private static Globals globals;

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
		getRegisteredTypes(type.getClass()).put(type.getInternalName(), type);
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

	public static EntityType getEntityType(String internalName) {
		return getType(EntityType.class, internalName);
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

	public static Iterable<EntityType> getEntityTypes() {
		return getTypes(EntityType.class);
	}

	private static JsonReader jsonReader = new JsonReader();

	@SuppressWarnings("unchecked")
	private static void loadTypes() {
		// Source: http://stackoverflow.com/a/9571146
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		// TODO: Limit the resources that are scanned?
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new ResourcesScanner())
				.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))));

		for (String typePath : reflections.getResources((resourceName) -> {
			return typeLoadersByExtension.containsKey(new FileHandle(resourceName).extension());
		})) {
			FileHandle file = Gdx.files.internal(typePath);
			JsonValue config = jsonReader.parse(file);

			TypeLoader<?> loader = typeLoadersByExtension.get(file.extension());
			Type typeInstance = null;

			try {
				typeInstance = (Type) loader.getTypeClass().newInstance();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}

			typeInstance.setInternalName(config.getString("internalName"));
			typeInstance.setConfig(config);

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

	@SuppressWarnings("unchecked")
	static void loadAssets(AssetManager assetManager) {
		for (Class<? extends Type> typeClass : registeredTypes.keys()) {
			TypeLoader<?> loader = typeLoadersByClass.get(typeClass);
			for (Type type : getTypes(typeClass)) {
				((TypeLoader<Type>) loader).loadAssets(type, type.getConfig(), assetManager);
			}
		}
	}

	@SuppressWarnings("unchecked")
	static void getAssets(AssetManager assetManager) {
		for (Class<? extends Type> typeClass : registeredTypes.keys()) {
			TypeLoader<?> loader = typeLoadersByClass.get(typeClass);
			for (Type type : getTypes(typeClass)) {
				((TypeLoader<Type>) loader).getAssets(type, type.getConfig(), assetManager);
			}
		}
	}

	public static void initialize() {
		// Create LuaJ globals and register all game classes
		globals = JsePlatform.standardGlobals();

		// Source: http://stackoverflow.com/a/9571146
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
				.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
				.filterInputsBy(new FilterBuilder()
						/** Include aquarria classes */
						.include(FilterBuilder.prefix("com.github.antag99.aquarria"))
						/** Exclude tests and benchmarks in eclipse */
						.exclude(FilterBuilder.prefix("com.github.antag99.aquarria.tests"))
						.exclude(FilterBuilder.prefix("com.github.antag99.aquarria.benchmarks"))
						/** Exclude anonymous inner classes */
						.exclude(".*\\$\\d+.*")));

		for (String type : reflections.getAllTypes()) {
			// FIXME: This dosen't seem to include enumerations; find a workaround
			try {
				Class<?> clazz = Class.forName(type);
				// Put inner classes in nested tables
				String[] nesting = clazz.getSimpleName().split("\\$");
				LuaTable table = globals;
				for (int i = 0; i < nesting.length - 1; ++i) {
					if (table.get(nesting[i]) == LuaValue.NIL) {
						table.set(nesting[i], new LuaTable());
					}
				}
				table.set(nesting[nesting.length - 1], CoerceJavaToLua.coerce(clazz));
			} catch (ClassNotFoundException ex) {
				// Just shouldn't happen
				throw new AssertionError(ex);
			}
		}

		globals.set("Direction", CoerceJavaToLua.coerce(Direction.class));

		// Set ResourceFinder that uses internal files
		globals.finder = new ResourceFinder() {
			@Override
			public InputStream findResource(String filename) {
				return Gdx.files.internal(filename).read();
			}
		};

		registerTypeLoader(new ItemTypeLoader());
		registerTypeLoader(new TileTypeLoader());
		registerTypeLoader(new WallTypeLoader());
		registerTypeLoader(new EntityTypeLoader());

		loadTypes();

		// Set static convenience fields in type classes
		for (Class<? extends Type> typeClass : registeredTypes.keys()) {
			for (Type type : getTypes(typeClass)) {
				try {
					typeClass.getField(type.getInternalName()).set(null, type);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public static void clear() {
		typeLoadersByClass.clear();
		typeLoadersByExtension.clear();
		registeredTypes.clear();
	}

	public static Globals getGlobals() {
		return globals;
	}
}
