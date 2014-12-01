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
package com.github.antag99.aquarria.tile;

import com.github.antag99.aquarria.world.World;

public class BlockFrameStyle extends FrameStyle {
	public BlockFrameStyle(String internalName) {
		super(internalName);
	}

	@Override
	public Frame findFrame(World world, int x, int y) {
		TileType type = world.getTileType(x, y);

		TileType top = y + 1 < world.getHeight() ? world.getTileType(x, y + 1) : type;
		TileType right = x + 1 < world.getWidth() ? world.getTileType(x + 1, y) : type;
		TileType bottom = y > 0 ? world.getTileType(x, y - 1) : type;
		TileType left = x > 0 ? world.getTileType(x - 1, y) : type;

		// TODO: Implement the different variations

		// X = merges with the current tile
		// - = does not merge with the current tile
		// ? = unknown

		if (top == TileType.air) {
			// -
			// ? ?
			// ?
			if (right == TileType.air) {
				// -
				// ? -
				// ?
				if (bottom == TileType.air) {
					// -
					// ? -
					// -
					if (left == TileType.air) {
						// -
						// - -
						// -
						return Frame.empty;
					} else {
						// -
						// X -
						// -
						return Frame.leftStrip;
					}
				} else {
					// -
					// ? -
					// X
					if (left == TileType.air) {
						// -
						// - -
						// X
						return Frame.bottomStrip;
					} else {
						// -
						// X -
						// X
						return Frame.topRightCorner;
					}
				}
			} else {
				// -
				// ? X
				// ?
				if (bottom == TileType.air) {
					// -
					// ? X
					// -
					if (left == TileType.air) {
						// -
						// - X
						// -
						return Frame.rightStrip;
					} else {
						// -
						// X X
						// -
						return Frame.horizontalStrip;
					}
				} else {
					// -
					// ? X
					// X
					if (left == TileType.air) {
						// -
						// - X
						// X
						return Frame.topLeftCorner;
					} else {
						// -
						// X X
						// X
						return Frame.topEdge;
					}
				}
			}
		} else {
			// X
			// ? ?
			// ?
			if (right == TileType.air) {
				// X
				// ? -
				// ?
				if (bottom == TileType.air) {
					// X
					// ? -
					// -
					if (left == TileType.air) {
						// X
						// - -
						// -
						return Frame.topStrip;
					} else {
						// X
						// X -
						// -
						return Frame.bottomRightCorner;
					}
				} else {
					// X
					// ? -
					// X
					if (left == TileType.air) {
						// X
						// - -
						// X
						return Frame.verticalStrip;
					} else {
						// X
						// X -
						// X
						return Frame.rightEdge;
					}
				}
			} else {
				// X
				// ? X
				// ?
				if (bottom == TileType.air) {
					// X
					// ? X
					// -
					if (left == TileType.air) {
						// X
						// - X
						// -
						return Frame.bottomLeftCorner;
					} else {
						// X
						// X X
						// -
						return Frame.bottomEdge;
					}
				} else {
					// X
					// ? X
					// X
					if (left == TileType.air) {
						// X
						// - X
						// X
						return Frame.leftEdge;
					} else {
						// X
						// X X
						// X
						return Frame.full;
					}
				}
			}
		}
	}
}
