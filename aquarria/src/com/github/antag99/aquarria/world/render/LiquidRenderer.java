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
package com.github.antag99.aquarria.world.render;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.world.World;

public class LiquidRenderer extends WorldRendererCallback {
	private TextureRegion waterTopTexture;
	private TextureRegion waterFullTexture;

	public LiquidRenderer() {
	}

	@Override
	public void queueAssets(AssetManager assetManager) {
		assetManager.load("images/tiles/water.png", TextureRegion.class);
	}

	@Override
	public void getAssets(AssetManager assetManager) {
		TextureRegion waterTexture = assetManager.get("images/tiles/water.png");
		waterTopTexture = new TextureRegion(waterTexture, 0, 0, waterTexture.getRegionWidth(), 4);
		waterFullTexture = new TextureRegion(waterTexture, 0, 4, waterTexture.getRegionWidth(), waterTexture.getRegionHeight() - 4);
	}

	@Override
	public void render(Batch batch, World world, int startX, int startY, int endX, int endY) {
		batch.setColor(1f, 1f, 1f, 0.8f);
		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				int liquid = world.getLiquid(i, j);
				if (liquid != 0) {
					float liquidPercentage = liquid / 255f;

					boolean hasTopLiquid = j < world.getHeight() && (world.getLiquid(i, j + 1) != 0 ||
							(liquid == 255 && world.getTileType(i, j + 1).isSolid()));

					if (hasTopLiquid) {
						batch.draw(waterFullTexture, i, j, 1f, liquidPercentage);
					} else {
						batch.draw(waterFullTexture, i, j, 1f, liquidPercentage - 0.25f);
						batch.draw(waterTopTexture, i, j + liquidPercentage - 0.25f, 1f, 0.25f);
					}
				}
			}
		}
	}
}
