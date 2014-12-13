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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.entity.EntityType;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.tile.FrameStyle.FrameSkin;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;

public final class GameRegistry {
	private GameRegistry() {
		throw new AssertionError();
	}

	private static ObjectMap<String, ItemType> internalNameToItemType = new ObjectMap<>();
	private static ObjectMap<String, TileType> internalNameToTileType = new ObjectMap<>();
	private static ObjectMap<String, WallType> internalNameToWallType = new ObjectMap<>();
	private static ObjectMap<String, EntityType> internalNameToEntityType = new ObjectMap<>();

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
			throw new IllegalArgumentException("No item with the internal name " + internalName + " has been registered");
		}
		return itemType;
	}

	public static TileType getTileType(String internalName) {
		TileType tileType = internalNameToTileType.get(internalName);
		if (tileType == null) {
			throw new IllegalArgumentException("No tile with the internal name " + internalName + " has been registered");
		}
		return tileType;
	}

	public static WallType getWallType(String internalName) {
		WallType wallType = internalNameToWallType.get(internalName);
		if (wallType == null) {
			throw new IllegalArgumentException("No wall with the internal name " + internalName + " has been registered");
		}
		return wallType;
	}

	public static EntityType getEntityType(String internalName) {
		EntityType entityType = internalNameToEntityType.get(internalName);
		if (entityType == null) {
			throw new IllegalArgumentException("No entity with the internal name " + internalName + " has been registered");
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

	public static void initialize() {
		registerItem(ItemType.air);
		registerItem(ItemType.dirt);
		registerItem(ItemType.stone);
		registerItem(ItemType.pickaxe);
		registerItem(ItemType.hammer);
		registerItem(ItemType.dirtWall);
		registerItem(ItemType.stoneWall);

		registerTile(TileType.air);
		registerTile(TileType.dirt);
		registerTile(TileType.stone);
		registerTile(TileType.grass);

		registerWall(WallType.air);
		registerWall(WallType.dirt);
		registerWall(WallType.stone);

		registerEntity(EntityType.player);
		registerEntity(EntityType.item);
	}

	public static void clear() {
		internalNameToItemType.clear();
		internalNameToTileType.clear();
		internalNameToEntityType.clear();
	}

	static void queueAssets(AssetManager assetManager) {
		for (ItemType itemType : getItemTypes()) {
			String texturePath = itemType.getTexturePath();
			if (texturePath != null) {
				assetManager.load(texturePath, TextureRegion.class);
			}
		}

		for (TileType tileType : getTileTypes()) {
			String skinPath = tileType.getSkinPath();
			if (skinPath != null) {
				assetManager.load(skinPath, TextureAtlas.class);
			}
		}

		for (WallType wallType : getWallTypes()) {
			String skinPath = wallType.getSkinPath();
			if (skinPath != null) {
				assetManager.load(skinPath, TextureAtlas.class);
			}
		}
	}

	static void getAssets(AssetManager assetManager) {
		for (ItemType itemType : getItemTypes()) {
			String texturePath = itemType.getTexturePath();
			if (texturePath != null) {
				itemType.setTexture(assetManager.get(texturePath, TextureRegion.class));
			}
		}

		for (TileType tileType : getTileTypes()) {
			String skinPath = tileType.getSkinPath();
			if (skinPath != null) {
				TextureAtlas atlas = assetManager.get(skinPath, TextureAtlas.class);
				tileType.setSkin(new FrameSkin(atlas));
			}
		}

		for (WallType wallType : getWallTypes()) {
			String skinPath = wallType.getSkinPath();
			if (skinPath != null) {
				TextureAtlas atlas = assetManager.get(skinPath, TextureAtlas.class);
				wallType.setSkin(new FrameSkin(atlas));
			}
		}
	}
}
