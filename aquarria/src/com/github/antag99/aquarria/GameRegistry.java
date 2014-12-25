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

import java.util.LinkedList;
import java.util.List;

import org.luaj.vm2.Globals;
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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.entity.EntityType;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.item.ItemUsageStyle;
import com.github.antag99.aquarria.tile.BlockFrameStyleFactory;
import com.github.antag99.aquarria.tile.FrameStyleFactory;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.TreeFrameStyleFactory;
import com.github.antag99.aquarria.tile.WallFrameStyleFactory;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.util.Utils;

public final class GameRegistry {
	private GameRegistry() {
		throw new AssertionError();
	}

	// Asset manager, used to get the assets when loading is done
	private static AssetManager assetManager = null;

	// Mappings of internal names to concrete types
	private static ObjectMap<String, ItemType> internalNameToItemType = new ObjectMap<>();
	private static ObjectMap<String, TileType> internalNameToTileType = new ObjectMap<>();
	private static ObjectMap<String, WallType> internalNameToWallType = new ObjectMap<>();
	private static ObjectMap<String, EntityType> internalNameToEntityType = new ObjectMap<>();

	private static ObjectMap<String, FrameStyleFactory> frameStyleFactories = new ObjectMap<>();

	private static Globals globals;

	public static void registerItem(ItemType itemType) {
		internalNameToItemType.put(itemType.getInternalName(), itemType);
	}

	public static void registerTile(TileType tileType) {
		internalNameToTileType.put(tileType.getInternalName(), tileType);
	}

	public static void registerWall(WallType wallType) {
		internalNameToWallType.put(wallType.getInternalName(), wallType);
	}

	public static void registerEntity(EntityType entityType) {
		internalNameToEntityType.put(entityType.getInternalName(), entityType);
	}

	public static ItemType getItemType(String internalName) {
		ItemType itemType = internalNameToItemType.get(internalName);
		if (itemType == null) {
			throw new IllegalArgumentException("ItemType not found: " + internalName);
		}
		return itemType;
	}

	public static TileType getTileType(String internalName) {
		TileType tileType = internalNameToTileType.get(internalName);
		if (tileType == null) {
			throw new IllegalArgumentException("TileType not found: " + internalName);
		}
		return tileType;
	}

	public static WallType getWallType(String internalName) {
		WallType wallType = internalNameToWallType.get(internalName);
		if (wallType == null) {
			throw new IllegalArgumentException("WallType not found: " + internalName);
		}
		return wallType;
	}

	public static EntityType getEntityType(String internalName) {
		EntityType entityType = internalNameToEntityType.get(internalName);
		if (entityType == null) {
			throw new IllegalArgumentException("EntityType not found: " + internalName);
		}
		return entityType;
	}

	public static Iterable<ItemType> getItemTypes() {
		return internalNameToItemType.values();
	}

	public static Iterable<TileType> getTileTypes() {
		return internalNameToTileType.values();
	}

	public static Iterable<WallType> getWallTypes() {
		return internalNameToWallType.values();
	}

	public static Iterable<EntityType> getEntityTypes() {
		return internalNameToEntityType.values();
	}

	private static JsonReader jsonReader = new JsonReader();

	private static void loadConfigurations() {
		// Source: http://stackoverflow.com/a/9571146
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		// TODO: Limit the resources that are scanned?
		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new ResourcesScanner())
				.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))));

		for (String itemPath : reflections.getResources((name) -> name.endsWith(".item"))) {
			FileHandle file = Gdx.files.internal(itemPath);
			if (file.exists()) {
				JsonValue value = jsonReader.parse(file);
				ItemType itemType = new ItemType();
				itemType.setConfig(value);
				itemType.setInternalName(value.getString("internalName"));
				registerItem(itemType);
			}
		}

		for (String tilePath : reflections.getResources((name) -> name.endsWith(".tile"))) {
			FileHandle file = Gdx.files.internal(tilePath);
			if (file.exists()) {
				JsonValue value = jsonReader.parse(file);
				TileType tileType = new TileType();
				tileType.setConfig(value);
				tileType.setInternalName(value.getString("internalName"));
				registerTile(tileType);
			}
		}

		for (String wallPath : reflections.getResources((name) -> name.endsWith(".wall"))) {
			FileHandle file = Gdx.files.internal(wallPath);
			if (file.exists()) {
				JsonValue value = jsonReader.parse(file);
				WallType wallType = new WallType();
				wallType.setConfig(value);
				wallType.setInternalName(value.getString("internalName"));
				registerWall(wallType);
			}
		}

		for (String entityPath : reflections.getResources((name) -> name.endsWith(".entity"))) {
			FileHandle file = Gdx.files.internal(entityPath);
			if (file.exists()) {
				JsonValue value = jsonReader.parse(file);
				EntityType entityType = new EntityType();
				entityType.setConfig(value);
				entityType.setInternalName(value.getString("internalName"));
				registerEntity(entityType);
			}
		}
	}

	/** Called to queue the required assets for loading */
	static void loadAssets(AssetManager assetManager) {
		loadConfigurations();

		GameRegistry.assetManager = assetManager;

		for (ItemType itemType : getItemTypes()) {
			if (itemType.getConfig().has("texture")) {
				assetManager.load(itemType.getConfig().getString("texture"), TextureRegion.class);
			}
		}

		for (TileType tileType : getTileTypes()) {
			if (tileType.getConfig().has("skin")) {
				assetManager.load(tileType.getConfig().getString("skin"), TextureAtlas.class);
			}
		}

		for (WallType wallType : getWallTypes()) {
			if (wallType.getConfig().has("skin")) {
				assetManager.load(wallType.getConfig().getString("skin"), TextureAtlas.class);
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
				.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.github.antag99.aquarria"))));

		for (String type : reflections.getAllTypes()) {
			try {
				Class<?> clazz = Class.forName(type);
				globals.set(clazz.getSimpleName(), CoerceJavaToLua.coerce(clazz));
			} catch (ClassNotFoundException ex) {
				// Just shouldn't happen
				throw new AssertionError(ex);
			}
		}

		// Set static convenience fields in type classes
		for (ItemType type : getItemTypes()) {
			try {
				ItemType.class.getField(type.getInternalName()).set(null, type);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		for (TileType type : getTileTypes()) {
			try {
				TileType.class.getField(type.getInternalName()).set(null, type);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		for (WallType type : getWallTypes()) {
			try {
				WallType.class.getField(type.getInternalName()).set(null, type);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		for (EntityType type : getEntityTypes()) {
			try {
				EntityType.class.getField(type.getInternalName()).set(null, type);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// Register frame style factories
		registerFrameStyleFactory("block", new BlockFrameStyleFactory());
		registerFrameStyleFactory("wall", new WallFrameStyleFactory());
		registerFrameStyleFactory("tree", new TreeFrameStyleFactory());

		// Initialize and register all types, leave out references to other types
		for (ItemType itemType : getItemTypes()) {
			JsonValue itemConfiguration = itemType.getConfig();
			itemType.setDisplayName(itemConfiguration.getString("displayName", ""));
			itemType.setMaxStack(itemConfiguration.getInt("maxStack", 1));
			itemType.setWidth(itemConfiguration.getFloat("width"));
			itemType.setHeight(itemConfiguration.getFloat("height"));
			itemType.setUsageTime(itemConfiguration.getFloat("usageTime", 0f));
			itemType.setUsageAnimationTime(itemConfiguration.getFloat("usageAnimationTime", itemType.getUsageTime()));
			itemType.setUsageRepeat(itemConfiguration.getBoolean("usageRepeat", false));
			itemType.setUsageStyle(ItemUsageStyle.swing); // TODO: This should be changed
			itemType.setConsumable(itemConfiguration.getBoolean("consumable", false));

			if (assetManager != null && itemConfiguration.has("texture")) {
				itemType.setTexture(assetManager.get(itemConfiguration.getString("texture", null), TextureRegion.class));
			}

			registerItem(itemType);
		}

		for (TileType tileType : getTileTypes()) {
			JsonValue tileConfiguration = tileType.getConfig();
			tileType.setDisplayName(tileConfiguration.getString("displayName", ""));
			tileType.setSolid(tileConfiguration.getBoolean("solid", true));

			if (tileConfiguration.has("frame")) {
				tileType.setFrame(Utils.asRectangle(tileConfiguration.get("frame")));
			}

			String tileFrameStyle = tileConfiguration.getString("style", "block");
			FrameStyleFactory styleFactory = getFrameStyleFactory(tileFrameStyle);
			tileType.setStyle(styleFactory.create(tileConfiguration));

			if (tileConfiguration.has("drop")) {
				tileType.setDrop(getItemType(tileConfiguration.getString("drop")));
			}

			if (assetManager != null && tileConfiguration.has("skin")) {
				TextureAtlas atlas = assetManager.get(tileConfiguration.getString("skin"), TextureAtlas.class);
				tileType.setAtlas(atlas);
			}

			registerTile(tileType);
		}

		for (WallType wallType : getWallTypes()) {
			JsonValue wallConfiguration = wallType.getConfig();
			wallType.setDisplayName(wallConfiguration.getString("displayName", ""));

			if (wallConfiguration.has("frame")) {
				wallType.setFrame(Utils.asRectangle(wallConfiguration.get("frame")));
			}

			String wallFrameStyle = wallConfiguration.getString("style", "wall");
			FrameStyleFactory styleFactory = getFrameStyleFactory(wallFrameStyle);
			wallType.setStyle(styleFactory.create(wallConfiguration));

			if (wallConfiguration.has("drop")) {
				wallType.setDrop(getItemType(wallConfiguration.getString("drop")));
			}

			if (assetManager != null && wallConfiguration.has("skin")) {
				TextureAtlas atlas = assetManager.get(wallConfiguration.getString("skin"), TextureAtlas.class);
				wallType.setAtlas(atlas);
			}

			registerWall(wallType);
		}

		for (EntityType entityType : getEntityTypes()) {
			JsonValue entityConfiguration = entityType.getConfig();
			entityType.setDisplayName(entityConfiguration.getString("displayName", ""));
			entityType.setMaxHealth(entityConfiguration.getInt("maxHealth", 0));
			entityType.setSolid(entityConfiguration.getBoolean("solid", true));
			entityType.setWeight(entityConfiguration.getFloat("weight", 1f));
			entityType.setDefaultWidth(entityConfiguration.getFloat("width"));
			entityType.setDefaultHeight(entityConfiguration.getFloat("height"));

			registerEntity(entityType);
		}
	}

	public static void clear() {
		internalNameToItemType.clear();
		internalNameToTileType.clear();
		internalNameToEntityType.clear();
		frameStyleFactories.clear();
	}

	public static void registerFrameStyleFactory(String name, FrameStyleFactory factory) {
		frameStyleFactories.put(name, factory);
	}

	public static FrameStyleFactory getFrameStyleFactory(String name) {
		FrameStyleFactory factory = frameStyleFactories.get(name);
		if (factory == null) {
			throw new IllegalArgumentException("FrameStyleFactory not found: " + name);
		}
		return factory;
	}
}
