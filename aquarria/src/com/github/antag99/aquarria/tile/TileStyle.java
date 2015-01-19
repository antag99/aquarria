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
package com.github.antag99.aquarria.tile;

import com.github.antag99.aquarria.world.World;

public interface TileStyle {
	public static TileStyle tile = new TileStyle() {
		TileFrame[][] tileFrames = new TileFrame[16][14];
		{ /* this is an instance initializer */
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 14; ++j) {
					tileFrames[i][j] = new TileFrame(i, j);
				}
			}
		}

		@Override
		public TileFrame findFrame(World world, int x, int y) {
			boolean mergeTop = y + 1 == world.getHeight() || world.getTileType(x, y + 1).isSolid();
			boolean mergeRight = x + 1 == world.getWidth() || world.getTileType(x + 1, y).isSolid();
			boolean mergeBottom = y == 0 || world.getTileType(x, y - 1).isSolid();
			boolean mergeLeft = x == 0 || world.getTileType(x - 1, y).isSolid();

			String frame = TileFraming.getBlockFrame(mergeTop,
					mergeRight, mergeBottom, mergeLeft);
			return tileFrames[TileFraming.getBlockFrameX(frame)][TileFraming.getBlockFrameY(frame)];
		}
	};

	public static TileStyle wall = new TileStyle() {
		TileFrame[][] tileFrames = new TileFrame[16][14];
		{ /* this is an instance initializer */
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 14; ++j) {
					tileFrames[i][j] = new TileFrame(i, j);
				}
			}
		}

		@Override
		public TileFrame findFrame(World world, int x, int y) {
			boolean mergeTop = y + 1 == world.getHeight() || world.getWallType(x, y + 1) != WallType.air;
			boolean mergeRight = x + 1 == world.getWidth() || world.getWallType(x + 1, y) != WallType.air;
			boolean mergeBottom = y == 0 || world.getWallType(x, y - 1) != WallType.air;
			boolean mergeLeft = x == 0 || world.getWallType(x - 1, y) != WallType.air;

			String frame = TileFraming.getBlockFrame(mergeTop,
					mergeRight, mergeBottom, mergeLeft);
			return tileFrames[TileFraming.getBlockFrameX(frame)][TileFraming.getBlockFrameY(frame)];
		}
	};

	/**
	 * Finds the frame of the tile or wall at the given position.
	 */
	TileFrame findFrame(World world, int x, int y);
}
