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
