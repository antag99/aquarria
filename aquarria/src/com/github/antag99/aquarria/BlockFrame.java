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

public enum BlockFrame {
	EMPTY(9, 3),
	FULL(1, 1),
	TOP_STRIP(6, 3),
	RIGHT_STRIP(9, 0),
	BOTTOM_STRIP(6, 0),
	LEFT_STRIP(12, 0),
	TOP_LEFT_CORNER(0, 3),
	TOP_RIGHT_CORNER(1, 3),
	BOTTOM_LEFT_CORNER(0, 4),
	BOTTOM_RIGHT_CORNER(1, 4),
	TOP_EDGE(1, 0),
	RIGHT_EDGE(4, 0),
	BOTTOM_EDGE(2, 2),
	LEFT_EDGE(0, 0),
	VERTICAL_STRIP(5, 0),
	HORIZONTAL_STRIP(6, 4);

	private final int x;
	private final int y;

	private BlockFrame(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static BlockFrame findFrame(boolean mergeTop, boolean mergeRight,
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
						return EMPTY;
					} else {
						//  -
						// X -
						//  -
						return LEFT_STRIP;
					}
				} else {
					//  -
					// ? -
					//  X
					if (!mergeLeft) {
						//  -
						// - -
						//  X
						return BOTTOM_STRIP;
					} else {
						//  -
						// X -
						//  X
						return TOP_RIGHT_CORNER;
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
						return RIGHT_STRIP;
					} else {
						//  -
						// X X
						//  -
						return HORIZONTAL_STRIP;
					}
				} else {
					//  -
					// ? X
					//  X
					if (!mergeLeft) {
						//  -
						// - X
						//  X
						return TOP_LEFT_CORNER;
					} else {
						//  -
						// X X
						//  X
						return TOP_EDGE;
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
						return TOP_STRIP;
					} else {
						//  X
						// X -
						//  -
						return BOTTOM_RIGHT_CORNER;
					}
				} else {
					//  X
					// ? -
					//  X
					if (!mergeLeft) {
						//  X
						// - -
						//  X
						return VERTICAL_STRIP;
					} else {
						//  X
						// X -
						//  X
						return RIGHT_EDGE;
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
						return BOTTOM_LEFT_CORNER;
					} else {
						//  X
						// X X
						//  -
						return BOTTOM_EDGE;
					}
				} else {
					//  X
					// ? X
					//  X
					if (!mergeLeft) {
						//  X
						// - X
						//  X
						return LEFT_EDGE;
					} else {
						//  X
						// X X
						//  X
						return FULL;
					}
				}
			}
		}
		// FORMATTER_ON
	}
}
