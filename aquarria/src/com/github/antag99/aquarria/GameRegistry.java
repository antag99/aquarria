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

import java.util.regex.Pattern;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

public final class GameRegistry {
	public static ItemType airItem;
	public static TileType airTile;
	public static WallType airWall;

	private GameRegistry() { /* Don't instantiate */
	}

	/*
	 * Mapping of type class to instances of the type
	 */
	private static ObjectMap<Class<? extends Type>, ObjectMap<String, Type>> instances = new ObjectMap<>();

	/*
	 * Type that is used on id clashes, which quite easy happens for the root interface Type.
	 * Attempting to access types that have the same id on a certain level in the hierarchy
	 * is prohibited.
	 */
	private static final Type AMBIGOUS = null;

	/*
	 * Reflections instance, used to get metadata
	 */
	private static Reflections reflections;

	/*
	 * Json instance, used to parse configuration files
	 */
	private static Json json;

	/*
	 * Initializes all types
	 */
	public static void initialize() {
		// Scan for json configuration files on the classpath
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setScanners(new ResourcesScanner());
		builder.setUrls(ClasspathHelper.forClassLoader(
				ClasspathHelper.contextClassLoader(),
				ClasspathHelper.staticClassLoader()));

		reflections = new Reflections(builder);
		json = new Json();
		for (String path : reflections.getResources(Pattern.compile(".*\\.json"))) {
			if (!path.startsWith("items") && !path.startsWith("tiles") && !path.startsWith("walls"))
				continue;

			FileHandle file = Gdx.files.internal(path);
			Type type = json.fromJson(null, file);
			registerType(type);
		}

		airItem = getItem("airItem");
		airTile = getTile("airTile");
		airWall = getWall("airWall");
	}

	@SuppressWarnings("unchecked")
	public static void registerType(Type type) {
		registerType(type, (Class<Type>) type.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Type> ObjectMap<String, T> getTypes(Class<T> typeClass) {
		if (!instances.containsKey(typeClass))
			instances.put(typeClass, new ObjectMap<String, Type>());
		return (ObjectMap<String, T>) instances.get(typeClass);
	}

	public static <T extends Type> T getType(Class<T> typeClass, String id) {
		ObjectMap<String, T> types = getTypes(typeClass);
		if (!types.containsKey(id))
			throw new IllegalArgumentException(typeClass.getSimpleName() + " " + id + " has not been registered");
		T type = types.get(id);
		if (type == AMBIGOUS)
			throw new IllegalArgumentException(typeClass.getSimpleName() + " " + id + " is ambigous");
		return type;
	}

	public static ItemType getItem(String id) {
		return getType(ItemType.class, id);
	}

	public static TileType getTile(String id) {
		return getType(TileType.class, id);
	}

	public static WallType getWall(String id) {
		return getType(WallType.class, id);
	}

	/*
	 * Registers the given type. All interfaces or class that directly or indirectly
	 * implements Type are registered in the instances map, and can thus be retrieved
	 * using the supplied methods. This is especially useful when sharing the same
	 * instance for an item/tile pair.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Type> void registerType(T type, Class<T> typeClass) {
		// Put type in the mapping
		ObjectMap<String, T> types = getTypes(typeClass);
		if (!types.containsKey(type.getId())) /* No id clash, put in map */
			types.put(type.getId(), type);
		else if (types.get(type.getId()) != type)
			/* Id clash, undefined on this level in the hierarchy */
			types.put(type.getId(), (T) AMBIGOUS);

		// Register type as superclass instance
		if (typeClass.getSuperclass() != null &&
				Type.class.isAssignableFrom(typeClass.getSuperclass())) {
			registerType(type, (Class<T>) typeClass.getSuperclass());
		}

		// Register type as superinterface instance
		for (Class<?> otherInterface : typeClass.getInterfaces()) {
			if (Type.class.isAssignableFrom(otherInterface)) {
				registerType(type, (Class<T>) otherInterface);
			}
		}
	}
}
