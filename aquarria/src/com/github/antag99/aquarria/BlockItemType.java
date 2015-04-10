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

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.world.World;
import com.github.antag99.aquarria.world.WorldView;

/**
 * Implements tile and item pairs, loaded from json files.
 */
public class BlockItemType extends BasicItemType
		implements ItemType, TileType, Json.Serializable {
	private SpriteSheet sheet;

	public BlockItemType() {
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);

		sheet = new SpriteSheet(Assets.getTexture(jsonData.getString("sheet")), Assets.tileGrid);
	}

	// Tile-specific stuff
	@Override
	public boolean isSolid() {
		return true;
	}

	@Override
	public Sprite getTexture(WorldView worldView, int x, int y) {
		World world = worldView.getWorld();
		BlockFrame frame = BlockFrame.findFrame(
				y == 0 || world.getTileType(x, y + 1) != GameRegistry.airTile,
				x + 1 == world.getWidth() || world.getTileType(x + 1, y) != GameRegistry.airTile,
				y + 1 == world.getHeight() || world.getTileType(x, y - 1) != GameRegistry.airTile,
				x == 0 || world.getTileType(x - 1, y) != GameRegistry.airTile);
		return sheet.getSprite(frame.getX(), frame.getY());
	}

	@Override
	public void placed(World world, int x, int y) {
	}

	@Override
	public void destroyed(World world, int x, int y) {
		world.dropItem(new Item(this, 1), x, y);
	}

	// Item-specific stuff
	@Override
	public float getUsageDelay() {
		return 0.3f;
	}

	@Override
	public boolean getUsageRepeat() {
		return true;
	}

	@Override
	public ItemAnimation getUsageAnimation() {
		return ItemAnimation.swing;
	}

	@Override
	public boolean isConsumable() {
		return true;
	}

	@Override
	public boolean canUse(PlayerEntity player, Item stack) {
		return true;
	}

	@Override
	public boolean usageEffect(PlayerEntity player, Item stack) {
		Vector2 focus = player.getWorldFocus();
		int tileX = MathUtils.floor(focus.x);
		int tileY = MathUtils.floor(focus.y);

		World world = player.getWorld();

		if (world.getTileType(tileX, tileY) == GameRegistry.airTile) {
			world.setTileType(tileX, tileY, this);
			return true;
		}

		return false;
	}
}
