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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.world.World;
import com.github.antag99.aquarria.world.WorldView;

public class TreeTileType extends BasicTileType
		implements TileType, Json.Serializable {

	private SpriteSheet topSheet;
	private SpriteSheet branchSheet;

	public TreeTileType() {
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);

		topSheet = new SpriteSheet(Assets.getTexture(jsonData.getString("topSheet")), Assets.topsGrid);
		branchSheet = new SpriteSheet(Assets.getTexture(jsonData.getString("branchSheet")), Assets.branchesGrid);

		// HACK: Left branch sprites are offset manually
		for (int y = 0; y < 3; ++y) {
			TextureRegion texture = branchSheet.getSprite(0, y).getTexture();
			branchSheet.setSprite(0, y, new Sprite(texture, -24f, -10f, 40f, 40f));
		}
	}

	@Override
	public void destroyed(World world, int x, int y) {
		super.destroyed(world, x, y);

		// Unblock the tile underneath the tree
		if (world.getTileType(x, y - 1) != this) {
			world.setTileBlocked(x, y - 1, false);
		}
	}

	@Override
	protected SpriteGrid getGrid() {
		return Assets.trunkGrid;
	}

	@Override
	public Sprite getTexture(WorldView worldView, int x, int y) {
		TreeFrame treeFrame = TreeFrame.findFrame(worldView.getWorld(), x, y);

		if (treeFrame == TreeFrame.TOP) {
			return topSheet.getSprite(0, 0);
		} else if (treeFrame == TreeFrame.BRANCH_LEFT) {
			return branchSheet.getSprite(0, 0);
		} else if (treeFrame == TreeFrame.BRANCH_RIGHT) {
			return branchSheet.getSprite(1, 0);
		}

		return getSheet().getSprite(treeFrame.getX(), treeFrame.getY());
	}
}
