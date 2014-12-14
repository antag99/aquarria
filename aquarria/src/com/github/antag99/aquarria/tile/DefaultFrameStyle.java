package com.github.antag99.aquarria.tile;

import com.github.antag99.aquarria.world.World;

/**
 * Provides the default framing style in a target-independent manner.
 * Subclasses {@link BlockFrameStyle} and {@link WallFrameStyle} provide
 * the concrete implementations.
 */
public abstract class DefaultFrameStyle implements FrameStyle {

	@Override
	public String findFrame(World world, int x, int y) {
		boolean mergeNorth = merge(world, x, y, x, y + 1);
		boolean mergeEast = merge(world, x, y, x + 1, y);
		boolean mergeSouth = merge(world, x, y, x, y - 1);
		boolean mergeWest = merge(world, x, y, x - 1, y);

		// FORMATTER_OFF
		if (!mergeNorth) {
			//  -
			// ? ?
			//  ?
			if (!mergeEast) {
				//  -
				// ? -
				//  ?
				if (!mergeSouth) {
					//  -
					// ? -
					//  -
					if (!mergeWest) {
						//  -
						// - -
						//  -
						return "empty";
					} else {
						//  -
						// X -
						//  -
						return "leftStrip";
					}
				} else {
					//  -
					// ? -
					//  X
					if (!mergeWest) {
						//  -
						// - -
						//  X
						return "bottomStrip";
					} else {
						//  -
						// X -
						//  X
						return "topRightCorner";
					}
				}
			} else {
				//  -
				// ? X
				//  ?
				if (!mergeSouth) {
					//  -
					// ? X
					//  -
					if (!mergeWest) {
						//  -
						// - X
						//  -
						return "rightStrip";
					} else {
						//  -
						// X X
						//  -
						return "horizontalStrip";
					}
				} else {
					//  -
					// ? X
					//  X
					if (!mergeWest) {
						//  -
						// - X
						//  X
						return "topLeftCorner";
					} else {
						//  -
						// X X
						//  X
						return "topEdge";
					}
				}
			}
		} else {
			//  X
			// ? ?
			//  ?
			if (!mergeEast) {
				//  X
				// ? -
				//  ?
				if (!mergeSouth) {
					//  X
					// ? -
					//  -
					if (!mergeWest) {
						//  X
						// - -
						//  -
						return "topStrip";
					} else {
						//  X
						// X -
						//  -
						return "bottomRightCorner";
					}
				} else {
					//  X
					// ? -
					//  X
					if (!mergeWest) {
						//  X
						// - -
						//  X
						return "verticalStrip";
					} else {
						//  X
						// X -
						//  X
						return "rightEdge";
					}
				}
			} else {
				//  X
				// ? X
				//  ?
				if (!mergeSouth) {
					//  X
					// ? X
					//  -
					if (!mergeWest) {
						//  X
						// - X
						//  -
						return "bottomLeftCorner";
					} else {
						//  X
						// X X
						//  -
						return "bottomEdge";
					}
				} else {
					//  X
					// ? X
					//  X
					if (!mergeWest) {
						//  X
						// - X
						//  X
						return "leftEdge";
					} else {
						//  X
						// X X
						//  X
						return "full";
					}
				}
			}
		}
		// FORMATTER_ON
	}

	/**
	 * Gets whether the two tiles in the specified world should be merged
	 */
	protected abstract boolean merge(World world, int x, int y, int x2, int y2);
}
