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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.world.World;

public class LightRenderer extends WorldRendererCallback {
	private TextureRegion lightTexture;

	public LightRenderer() {
	}

	@Override
	public void queueAssets(AssetManager assetManager) {
		assetManager.load("images/white.png", TextureRegion.class);
	}

	@Override
	public void getAssets(AssetManager assetManager) {
		lightTexture = assetManager.get("images/white.png");
	}

	@Override
	public void render(Batch batch, World world, int startX, int startY, int endX, int endY) {
		world.computeLight(startX, startY, endX - startX, endY - startY);

		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				float light = world.getLight(i, j);

				float topLeftLight = i > 0 && j + 1 < world.getHeight() ? world.getLight(i - 1, j + 1) : light;
				float bottomLeftLight = i > 0 && j > 0 ? world.getLight(i - 1, j - 1) : light;
				float bottomRightLight = j > 0 && i + 1 < world.getWidth() ? world.getLight(i + 1, j - 1) : light;
				float topRightLight = i + 1 < world.getWidth() && j + 1 < world.getHeight() ? world.getLight(i + 1, j + 1) : light;

				drawGradient(batch, i, j, 1f, 1f,
						Color.toFloatBits(0f, 0f, 0f, 1f - combineLight(light, topLeftLight)),
						Color.toFloatBits(0f, 0f, 0f, 1f - combineLight(light, topRightLight)),
						Color.toFloatBits(0f, 0f, 0f, 1f - combineLight(light, bottomLeftLight)),
						Color.toFloatBits(0f, 0f, 0f, 1f - combineLight(light, bottomRightLight)));
			}
		}
	}

	private float combineLight(float light, float adjacentLight) {
		return light;
	}

	private final float[] vertices = new float[20];

	// http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=9361#p42550
	private void drawGradient(Batch batch,
			float x, float y, float width, float height,
			float topLeftColor, float topRightColor,
			float bottomLeftColor, float bottomRightColor) {
		int idx = 0;
		float u = lightTexture.getU();
		float v = lightTexture.getV2();
		float u2 = lightTexture.getU2();
		float v2 = lightTexture.getV();

		// bottom left
		vertices[idx++] = x;
		vertices[idx++] = y;
		vertices[idx++] = bottomLeftColor;
		vertices[idx++] = u;
		vertices[idx++] = v;

		// top left
		vertices[idx++] = x;
		vertices[idx++] = y + height;
		vertices[idx++] = topLeftColor;
		vertices[idx++] = u;
		vertices[idx++] = v2;

		// top right
		vertices[idx++] = x + width;
		vertices[idx++] = y + height;
		vertices[idx++] = topRightColor;
		vertices[idx++] = u2;
		vertices[idx++] = v2;

		// bottom right
		vertices[idx++] = x + width;
		vertices[idx++] = y;
		vertices[idx++] = bottomRightColor;
		vertices[idx++] = u2;
		vertices[idx++] = v;

		batch.draw(lightTexture.getTexture(), vertices, 0, vertices.length);
	}
}
