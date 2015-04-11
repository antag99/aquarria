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

import com.github.antag99.aquarria.world.World;

public enum TreeFrame {
	/** Middle trunk */
	TRUNK(1, 0),
	/** Trunk with left branch */
	TRUNK_LEFT(4, 0),
	/** Trunk with right branch */
	TRUNK_RIGHT(3, 3),
	/** Trunk with both branches */
	TRUNK_BOTH(5, 3),
	/** Top */
	TOP(5, 0),
	/** Top with left branch */
	TOP_LEFT(6, 0),
	/** Top with right branch */
	TOP_RIGHT(6, 3),
	/** Top with both branches */
	TOP_BOTH(5, 3),
	/** Bottom */
	BOTTOM(0, 0),
	/** Bottom with left foot */
	BOTTOM_LEFT(3, 6),
	/** Bottom with right foot */
	BOTTOM_RIGHT(0, 6),
	/** Bottom with both feet */
	BOTTOM_BOTH(4, 6),
	/** Left foot */
	FOOT_LEFT(2, 6),
	/** Right foot */
	FOOT_RIGHT(1, 6),
	/** Left branch */
	BRANCH_LEFT(3, 0),
	/** Right branch */
	BRANCH_RIGHT(4, 3);

	private final int x;
	private final int y;

	private TreeFrame(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static TreeFrame findFrame(World world, int x, int y) {
		TileType trunkType = world.getTileType(x, y);
		// Check for adjacent *attached* tree tiles
		boolean adjacentTop = world.inBounds(x, y + 1) &&
				world.getTileType(x, y + 1) == trunkType &&
				world.isTileAttached(x, y + 1, Direction.SOUTH);
		boolean adjacentBottom = world.inBounds(x, y - 1) &&
				world.getTileType(x, y - 1) == trunkType &&
				world.isTileAttached(x, y, Direction.SOUTH);
		boolean adjacentLeft = world.inBounds(x - 1, y) &&
				world.getTileType(x - 1, y) == trunkType &&
				world.isTileAttached(x - 1, y, Direction.EAST);
		boolean adjacentRight = world.inBounds(x + 1, y) &&
				world.getTileType(x + 1, y) == trunkType &&
				world.isTileAttached(x + 1, y, Direction.WEST);
		// Get the frame based upon this information
		if (adjacentTop && adjacentBottom) { // Middle trunk
			if (adjacentLeft && adjacentRight)
				return TRUNK_BOTH;
			if (adjacentLeft)
				return TRUNK_LEFT;
			if (adjacentRight)
				return TRUNK_RIGHT;
			return TRUNK;
		}

		if (!adjacentTop && adjacentBottom) { // Top trunk
			if (adjacentLeft && adjacentRight)
				return TOP_BOTH;
			if (adjacentLeft)
				return TOP_LEFT;
			if (adjacentRight)
				return TOP_RIGHT;
			return TOP;
		}

		if (!adjacentBottom && adjacentTop) { // Bottom trunk
			if (adjacentLeft && adjacentRight)
				return BOTTOM_BOTH;
			if (adjacentLeft)
				return BOTTOM_LEFT;
			if (adjacentRight)
				return BOTTOM_RIGHT;
			return BOTTOM;
		}

		// Left & right tree trunks the tile is attached to
		boolean trunkLeft = world.inBounds(x - 1, y) && world.getTileType(x - 1, y) == trunkType && world.isTileAttached(x, y, Direction.WEST);
		boolean trunkRight = world.inBounds(x + 1, y) && world.getTileType(x + 1, y) == trunkType && world.isTileAttached(x, y, Direction.EAST);

		if (!world.isTileAttached(x, y, Direction.SOUTH)) {
			if (trunkLeft)
				return BRANCH_RIGHT;
			if (trunkRight)
				return BRANCH_LEFT;
			return BOTTOM; // Invalid frame...
		} else { // Foot, bottom or trunk
			if (trunkRight)
				return FOOT_LEFT;
			if (trunkLeft)
				return FOOT_RIGHT;
			// Bottom
			if (adjacentLeft && adjacentRight)
				return BOTTOM_BOTH;
			if (adjacentLeft)
				return BOTTOM_LEFT;
			if (adjacentRight)
				return BOTTOM_RIGHT;
			return BOTTOM;
		}
	}
}
