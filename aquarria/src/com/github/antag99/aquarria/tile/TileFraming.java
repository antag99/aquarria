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
		case "rightStrip": return 9;
		case "bottomStrip": return 6;
		case "leftStrip": return 12;
		case "topLeftCorner": return 0;
		case "topRightCorner": return 1;
		case "bottomLeftCorner": return 0;
		case "bottomRightCorner": return 1;
		case "topEdge": return 1;
		case "rightEdge": return 4;
		case "bottomEdge": return 2;
		case "leftEdge": return 0;
		case "verticalStrip": return 5;
		case "horizontalStrip": return 6;
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
		case "topStrip": return 3;
		case "rightStrip": return 0;
		case "bottomStrip": return 0;
		case "leftStrip": return 0;
		case "topLeftCorner": return 3;
		case "topRightCorner": return 3;
		case "bottomLeftCorner": return 4;
		case "bottomRightCorner": return 4;
		case "topEdge": return 0;
		case "rightEdge": return 0;
		case "bottomEdge": return 2;
		case "leftEdge": return 0;
		case "verticalStrip": return 0;
		case "horizontalStrip": return 4;
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
