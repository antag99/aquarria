package com.github.antag99.aquarria.tile;

public final class TileFraming {
	private TileFraming() {
	}

	/**
	 * Gets the X position of the given block frame
	 */
	public static int getBlockFrameX(String frameName) {
		// FORMATTER_OFF
		switch(frameName) {
		case "empty": return 9;
		case "full": return 1;
		case "topStrip": return 6;
		case "rightStrip": return 12;
		case "bottomStrip": return 6;
		case "leftStrip": return 9;
		case "topLeftCorner": return 0;
		case "topRightCorner": return 1;
		case "bottomLeftCorner": return 0;
		case "bottomRightCorner": return 1;
		case "topEdge": return 1;
		case "rightEdge": return 4;
		case "bottomEdge": return 2;
		case "leftEdge": return 0;
		default:
			throw new IllegalArgumentException(frameName + " not found");
		}
		// FORMATTER_ON
	}

	/**
	 * Gets the X position of the given block frame
	 */
	public static int getBlockFrameY(String frameName) {
		// FORMATTER_OFF
		switch(frameName) {
		case "empty": return 3;
		case "full": return 1;
		case "topStrip": return 0;
		case "rightStrip": return 0;
		case "bottomStrip": return 3;
		case "leftStrip": return 0;
		case "topLeftCorner": return 3;
		case "topRightCorner": return 3;
		case "bottomLeftCorner": return 4;
		case "bottomRightCorner": return 4;
		case "topEdge": return 0;
		case "rightEdge": return 0;
		case "bottomEdge": return 2;
		case "leftEdge": return 0;
		default:
			throw new IllegalArgumentException(frameName + " not found");
		}
		// FORMATTER_ON
	}

	/**
	 * Gets the name of a frame based on the adjacent merges
	 */
	public static String getBlockFrame(boolean mergeTop, boolean mergeRight,
			boolean mergeBottom, boolean mergeLeft) {
		// FORMATTER_OFF
		if (!mergeTop) {
			//  -
			// ? ?
			//  ?
			if (!mergeRight) {
				//  -
				// ? -
				//  ?
				if (!mergeBottom) {
					//  -
					// ? -
					//  -
					if (!mergeLeft) {
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
					if (!mergeLeft) {
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
				if (!mergeBottom) {
					//  -
					// ? X
					//  -
					if (!mergeLeft) {
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
					if (!mergeLeft) {
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
			if (!mergeRight) {
				//  X
				// ? -
				//  ?
				if (!mergeBottom) {
					//  X
					// ? -
					//  -
					if (!mergeLeft) {
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
					if (!mergeLeft) {
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
				if (!mergeBottom) {
					//  X
					// ? X
					//  -
					if (!mergeLeft) {
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
					if (!mergeLeft) {
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
}
