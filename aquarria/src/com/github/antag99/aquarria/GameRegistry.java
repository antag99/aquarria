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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.entity.EntityType;
import com.github.antag99.aquarria.item.DestroyTileCallbackFactory;
import com.github.antag99.aquarria.item.DestroyWallCallbackFactory;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.item.ItemUsageCallbackFactory;
import com.github.antag99.aquarria.item.ItemUsageStyle;
import com.github.antag99.aquarria.item.TilePlaceCallbackFactory;
import com.github.antag99.aquarria.item.WallPlaceCallbackFactory;
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

	// Mappings of file names (without extensions) to JSON configurations
	private static ObjectMap<String, JsonValue> fileNameToItemConfiguration = new ObjectMap<>();
	private static ObjectMap<String, JsonValue> fileNameToTileConfiguration = new ObjectMap<>();
	private static ObjectMap<String, JsonValue> fileNameToWallConfiguration = new ObjectMap<>();
	private static ObjectMap<String, JsonValue> fileNameToEntityConfiguration = new ObjectMap<>();

	// Asset manager, used to get the assets when loading is done
	private static AssetManager assetManager = null;

	// Mappings of internal names to concrete types
	private static ObjectMap<String, ItemType> internalNameToItemType = new ObjectMap<>();
	private static ObjectMap<String, TileType> internalNameToTileType = new ObjectMap<>();
	private static ObjectMap<String, WallType> internalNameToWallType = new ObjectMap<>();
	private static ObjectMap<String, EntityType> internalNameToEntityType = new ObjectMap<>();

	// Mapping of name to ItemUsageCallback factories, name is specified when registered
	private static ObjectMap<String, ItemUsageCallbackFactory> itemUsageCallbackFactories = new ObjectMap<>();
	private static ObjectMap<String, FrameStyleFactory> frameStyleFactories = new ObjectMap<>();

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
		fileNameToItemConfiguration.clear();
		fileNameToTileConfiguration.clear();
		fileNameToWallConfiguration.clear();
		fileNameToEntityConfiguration.clear();

		// TODO: This is dependent upon the assets directory existing
		// in a separate directory, rather than in the classpath.
		seekConfigurationDirectory(Gdx.files.local("assets"));
	}

	private static void seekConfigurationDirectory(FileHandle file) {
		for (FileHandle child : file.list()) {
			if (child.isDirectory()) {
				seekConfigurationDirectory(child);
			} else {
				ObjectMap<String, JsonValue> mapping = null;

				switch (child.extension()) {
				case "item":
					mapping = fileNameToItemConfiguration;
					break;
				case "tile":
					mapping = fileNameToTileConfiguration;
					break;
				case "wall":
					mapping = fileNameToWallConfiguration;
					break;
				case "entity":
					mapping = fileNameToEntityConfiguration;
					break;
				}

				if (mapping != null) {
					if (mapping.containsKey(child.nameWithoutExtension())) {
						throw new RuntimeException("Conflicting file name " + child.name());
					}

					mapping.put(child.nameWithoutExtension(), jsonReader.parse(child));
				}
			}
		}
	}

	/** Called to queue the required assets for loading */
	static void loadAssets(AssetManager assetManager) {
		loadConfigurations();

		GameRegistry.assetManager = assetManager;

		for (JsonValue itemConfiguration : fileNameToItemConfiguration.values()) {
			if (itemConfiguration.has("texture")) {
				assetManager.load(itemConfiguration.getString("texture"), TextureRegion.class);
			}
		}

		for (JsonValue tileConfiguration : fileNameToTileConfiguration.values()) {
			if (tileConfiguration.has("skin")) {
				assetManager.load(tileConfiguration.getString("skin"), TextureAtlas.class);
			}
		}

		for (JsonValue wallConfiguration : fileNameToWallConfiguration.values()) {
			if (wallConfiguration.has("skin")) {
				assetManager.load(wallConfiguration.getString("skin"), TextureAtlas.class);
			}
		}
	}

	public static void initialize() {
		// Register item usage callback factories
		registerItemUsageCallbackFactory("placeTile", new TilePlaceCallbackFactory());
		registerItemUsageCallbackFactory("placeWall", new WallPlaceCallbackFactory());
		registerItemUsageCallbackFactory("destroyTile", new DestroyTileCallbackFactory());
		registerItemUsageCallbackFactory("destroyWall", new DestroyWallCallbackFactory());

		// Register frame style factories
		registerFrameStyleFactory("block", new BlockFrameStyleFactory());
		registerFrameStyleFactory("wall", new WallFrameStyleFactory());
		registerFrameStyleFactory("tree", new TreeFrameStyleFactory());

		// Initialize and register all types, leave out references to other types
		for (JsonValue itemConfiguration : fileNameToItemConfiguration.values()) {
			ItemType itemType = new ItemType();
			itemType.setInternalName(itemConfiguration.getString("internalName"));
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

		for (JsonValue tileConfiguration : fileNameToTileConfiguration.values()) {
			TileType tileType = new TileType();
			tileType.setInternalName(tileConfiguration.getString("internalName"));
			tileType.setDisplayName(tileConfiguration.getString("displayName", ""));
			tileType.setSolid(tileConfiguration.getBoolean("solid", true));

			if (tileConfiguration.has("frame")) {
				tileType.setFrame(Utils.asRectangle(tileConfiguration.get("frame")));
			}

			if (assetManager != null && tileConfiguration.has("skin")) {
				TextureAtlas atlas = assetManager.get(tileConfiguration.getString("skin"), TextureAtlas.class);
				tileType.setAtlas(atlas);
			}

			registerTile(tileType);
		}

		for (JsonValue wallConfiguration : fileNameToWallConfiguration.values()) {
			WallType wallType = new WallType();
			wallType.setInternalName(wallConfiguration.getString("internalName"));
			wallType.setDisplayName(wallConfiguration.getString("displayName", ""));

			if (wallConfiguration.has("frame")) {
				wallType.setFrame(Utils.asRectangle(wallConfiguration.get("frame")));
			}

			if (assetManager != null && wallConfiguration.has("skin")) {
				TextureAtlas atlas = assetManager.get(wallConfiguration.getString("skin"), TextureAtlas.class);
				wallType.setAtlas(atlas);
			}

			registerWall(wallType);
		}

		for (JsonValue entityConfiguration : fileNameToEntityConfiguration.values()) {
			EntityType entityType = new EntityType();
			entityType.setInternalName(entityConfiguration.getString("internalName"));
			entityType.setDisplayName(entityConfiguration.getString("displayName", ""));
			entityType.setMaxHealth(entityConfiguration.getInt("maxHealth", 0));
			entityType.setSolid(entityConfiguration.getBoolean("solid", true));
			entityType.setWeight(entityConfiguration.getFloat("weight", 1f));
			entityType.setDefaultWidth(entityConfiguration.getFloat("width"));
			entityType.setDefaultHeight(entityConfiguration.getFloat("height"));

			registerEntity(entityType);
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

		// Then fixup the references, and other stuff that might required some types to be loaded
		for (JsonValue itemConfiguration : fileNameToItemConfiguration.values()) {
			ItemType itemType = getItemType(itemConfiguration.getString("internalName"));
			if (itemConfiguration.has("usage")) {
				ItemUsageCallbackFactory factory = getItemUsageCallbackFactory(itemConfiguration.getString("usage"));
				itemType.setUsageCallback(factory.create(itemConfiguration));
			}
		}

		for (JsonValue tileConfiguration : fileNameToTileConfiguration.values()) {
			TileType tileType = getTileType(tileConfiguration.getString("internalName"));

			String tileFrameStyle = tileConfiguration.getString("style", "block");
			FrameStyleFactory styleFactory = getFrameStyleFactory(tileFrameStyle);
			tileType.setStyle(styleFactory.create(tileConfiguration));

			if (tileConfiguration.has("drop")) {
				tileType.setDrop(getItemType(tileConfiguration.getString("drop")));
			}
		}

		for (JsonValue wallConfiguration : fileNameToWallConfiguration.values()) {
			WallType wallType = getWallType(wallConfiguration.getString("internalName"));

			String wallFrameStyle = wallConfiguration.getString("style", "wall");
			FrameStyleFactory styleFactory = getFrameStyleFactory(wallFrameStyle);
			wallType.setStyle(styleFactory.create(wallConfiguration));

			if (wallConfiguration.has("drop")) {
				wallType.setDrop(getItemType(wallConfiguration.getString("drop")));
			}
		}
	}

	public static void clear() {
		internalNameToItemType.clear();
		internalNameToTileType.clear();
		internalNameToEntityType.clear();
		itemUsageCallbackFactories.clear();
		frameStyleFactories.clear();
	}

	public static void registerItemUsageCallbackFactory(String name, ItemUsageCallbackFactory factory) {
		itemUsageCallbackFactories.put(name, factory);
	}

	public static ItemUsageCallbackFactory getItemUsageCallbackFactory(String name) {
		ItemUsageCallbackFactory factory = itemUsageCallbackFactories.get(name);
		if (factory == null) {
			throw new IllegalArgumentException("ItemUsageCallbackFactory not found: " + name);
		}
		return factory;
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
