package com.github.antag99.aquarria.tile;

import com.github.antag99.aquarria.world.World;

public class WallFrameStyle extends FrameStyle {
	public WallFrameStyle(String internalName) {
		super(internalName);
	}

	@Override
	public Frame findFrame(World world, int x, int y) {
		WallType type = world.getWallType(x, y);

		WallType top = y + 1 < world.getHeight() ? world.getWallType(x, y + 1) : type;
		WallType right = x + 1 < world.getWidth() ? world.getWallType(x + 1, y) : type;
		WallType bottom = y > 0 ? world.getWallType(x, y - 1) : type;
		WallType left = x > 0 ? world.getWallType(x - 1, y) : type;

		// X = merges with the current wall
		// - = does not merge with the current wall
		// ? = unknown

		if (top == WallType.air) {
			// -
			// ? ?
			// ?
			if (right == WallType.air) {
				// -
				// ? -
				// ?
				if (bottom == WallType.air) {
					// -
					// ? -
					// -
					if (left == WallType.air) {
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
					if (left == WallType.air) {
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
				if (bottom == WallType.air) {
					// -
					// ? X
					// -
					if (left == WallType.air) {
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
					if (left == WallType.air) {
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
			if (right == WallType.air) {
				// X
				// ? -
				// ?
				if (bottom == WallType.air) {
					// X
					// ? -
					// -
					if (left == WallType.air) {
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
					if (left == WallType.air) {
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
				if (bottom == WallType.air) {
					// X
					// ? X
					// -
					if (left == WallType.air) {
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
					if (left == WallType.air) {
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
