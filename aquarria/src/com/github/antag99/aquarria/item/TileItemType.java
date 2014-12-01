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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.world.World;

public class TileItemType extends ItemType {
	private String createdTileName;
	private TileType createdTile = null;

	public TileItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public TileItemType(JsonValue properties) {
		super(properties);

		createdTileName = properties.getString("createdTile");
	}

	@Override
	public boolean useItem(PlayerEntity player, Item item) {
		if (createdTile == null) {
			createdTile = TileType.forName(createdTileName);
		}

		Vector2 worldFocus = player.getWorldFocus();

		int tileX = MathUtils.floor(worldFocus.x);
		int tileY = MathUtils.floor(worldFocus.y);

		World world = player.getWorld();
		if (world.getTileType(tileX, tileY) == TileType.air) {
			world.setTileType(tileX, tileY, createdTile);

			return true;
		}

		return false;
	}

	@Override
	public boolean canUseItem(PlayerEntity player, Item item) {
		return true;
	}
}
