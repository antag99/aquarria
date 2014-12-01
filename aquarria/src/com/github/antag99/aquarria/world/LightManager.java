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
package com.github.antag99.aquarria.world;

import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;

public class LightManager {
	private World world;
	private byte[] lightBuffer;

	public LightManager(World world) {
		this.world = world;
		lightBuffer = new byte[world.getWidth() * world.getHeight()];
	}

	public float getLight(int x, int y) {
		return (lightBuffer[world.getHeight() * y + x] & 0xff) / 255f;
	}

	public void setLight(int x, int y, float light) {
		lightBuffer[y * world.getHeight() + x] = (byte) (light * 255);
	}

	public void computeLight(int x, int y, int width, int height) {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				if (world.getTileType(x + i, y + j) == TileType.air && world.getWallType(x + i, y + j) == WallType.air) {
					setLight(x + i, y + j, 1f);
				} else {
					setLight(x + i, y + j, 0f);
				}
			}
		}

		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				updateLight(x + i, y + j, x, y, x + width, y + height, getLight(x + i, y + j));
			}
		}
	}

	private void updateLight(int x, int y, int minX, int minY, int maxX, int maxY, float light) {
		if (x > minX)
			updateAdjacentLight(x - 1, y, minX, minY, maxX, maxY, light - 0.15f);
		if (y > minY)
			updateAdjacentLight(x, y - 1, minX, minY, maxX, maxY, light - 0.15f);
		if (x + 1 < maxX)
			updateAdjacentLight(x + 1, y, minX, minY, maxX, maxY, light - 0.15f);
		if (y + 1 < maxY)
			updateAdjacentLight(x, y + 1, minX, minY, maxX, maxY, light - 0.15f);
	}

	private void updateAdjacentLight(int x, int y, int minX, int minY, int maxX, int maxY, float light) {
		if (light > getLight(x, y)) {
			setLight(x, y, light);
			updateLight(x, y, minX, minY, maxX, maxY, light);
		}
	}
}
