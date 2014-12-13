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
package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.GameRegistry;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.world.World;

public final class ItemType {
	public static final ItemType air = new ItemType("items/air.json");
	public static final ItemType dirt = new ItemType("items/dirt.json");
	public static final ItemType stone = new ItemType("items/stone.json");
	public static final ItemType pickaxe = new ItemType("items/pickaxe.json");
	public static final ItemType hammer = new ItemType("items/hammer.json");

	public static final ItemType dirtWall = new ItemType("items/dirtWall.json");
	public static final ItemType stoneWall = new ItemType("items/stoneWall.json");

	private String internalName;
	private String displayName;
	private int maxStack;
	private float width;
	private float height;
	private String texturePath;
	private float usageTime;
	private float usageAnimationTime;
	private boolean usageRepeat;
	private boolean consumable;
	private ItemUsageStyle usageStyle;
	private boolean breakWall;
	private boolean breakTile;
	private String createdTileName;
	private String createdWallName;

	private TextureRegion texture;

	public ItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public ItemType(JsonValue properties) {
		internalName = properties.getString("internalName");
		displayName = properties.getString("displayName");
		maxStack = properties.getInt("maxStack", 1);
		width = properties.getFloat("width", 0f);
		height = properties.getFloat("height", 0f);
		texturePath = properties.getString("texture", null);
		usageTime = properties.getFloat("usageTime", 0f);
		usageAnimationTime = properties.getFloat("usageAnimationTime", usageTime);
		usageRepeat = properties.getBoolean("usageRepeat", false);
		consumable = properties.getBoolean("consumable", false);
		usageStyle = ItemUsageStyle.forName(properties.getString("usageStyle", "swing"));
		createdWallName = properties.getString("createdWall", null);
		createdTileName = properties.getString("createdTile", null);
		breakTile = properties.getBoolean("breakTile", false);
		breakWall = properties.getBoolean("breakWall", false);
	}

	public String getInternalName() {
		return internalName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public String getTexturePath() {
		return texturePath;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	public float getUsageTime() {
		return usageTime;
	}

	public float getUsageAnimationTime() {
		return usageAnimationTime;
	}

	public boolean getUsageRepeat() {
		return usageRepeat;
	}

	public boolean isConsumable() {
		return consumable;
	}

	public ItemUsageStyle getUsageStyle() {
		return usageStyle;
	}

	public boolean canUseItem(PlayerEntity player, Item item) {
		return breakTile || breakWall || createdTileName != null || createdWallName != null;
	}

	public void beginUseItem(PlayerEntity player, Item item) {
	}

	public void updateUseItem(PlayerEntity player, Item item, float delta) {
	}

	public boolean useItem(PlayerEntity player, Item item) {
		if (breakTile) {
			Vector2 worldFocus = player.getWorldFocus();

			int tileX = MathUtils.floor(worldFocus.x);
			int tileY = MathUtils.floor(worldFocus.y);

			return player.destroyTile(tileX, tileY);
		}

		if (breakWall) {
			Vector2 worldFocus = player.getWorldFocus();

			int tileX = MathUtils.floor(worldFocus.x);
			int tileY = MathUtils.floor(worldFocus.y);

			return player.destroyWall(tileX, tileY);
		}

		if (createdTileName != null) {
			Vector2 worldFocus = player.getWorldFocus();

			int tileX = MathUtils.floor(worldFocus.x);
			int tileY = MathUtils.floor(worldFocus.y);

			World world = player.getWorld();
			if (world.getTileType(tileX, tileY) == TileType.air) {
				world.setTileType(tileX, tileY, GameRegistry.getTileType(createdTileName));

				return true;
			}

			return false;
		}

		if (createdWallName != null) {
			Vector2 worldFocus = player.getWorldFocus();

			int tileX = MathUtils.floor(worldFocus.x);
			int tileY = MathUtils.floor(worldFocus.y);

			World world = player.getWorld();
			if (world.getWallType(tileX, tileY) == WallType.air) {
				world.setWallType(tileX, tileY, GameRegistry.getWallType(createdWallName));

				return true;
			}

			return false;
		}

		return false;
	}
}
