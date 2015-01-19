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

/**
 * {@link SpriteGrid} stores the grid data for a {@link SpriteSheet},
 * as that is often shared between multiple sheets, for example player
 * animations have different parts that are rendered on top of each other.
 */
public class SpriteGrid {
	/* the amount of sprites on the sheet */
	private int spriteCountX;
	private int spriteCountY;
	/* the offset (in pixels) of the sprites */
	private int spriteOffsetX;
	private int spriteOffsetY;
	/* the size (in pixels) of a single sprite */
	private int spriteWidth;
	private int spriteHeight;
	/* the spacing between sprites */
	private int spriteSpacingX;
	private int spriteSpacingY;

	public SpriteGrid(
			int spriteCountX, int spriteCountY,
			int spriteOffsetX, int spriteOffsetY,
			int spriteWidth, int spriteHeight,
			int spriteSpacingX, int spriteSpacingY) {
		this.spriteCountX = spriteCountX;
		this.spriteCountY = spriteCountY;
		this.spriteOffsetX = spriteOffsetX;
		this.spriteOffsetY = spriteOffsetY;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.spriteSpacingX = spriteSpacingX;
		this.spriteSpacingY = spriteSpacingY;
	}

	/** Gets the amount of sprites on the x axis */
	public int getSpriteCountX() {
		return spriteCountX;
	}

	/** Gets the amount of sprites on the y axis */
	public int getSpriteCountY() {
		return spriteCountY;
	}

	/** Gets the x offset of sprites on the sprite sheet */
	public int getSpriteOffsetX() {
		return spriteOffsetX;
	}

	/** Gets the y offset of sprites on the sprite sheet */
	public int getSpriteOffsetY() {
		return spriteOffsetY;
	}

	/** Gets the x spacing between sprites on the sprite sheet */
	public int getSpriteSpacingX() {
		return spriteSpacingX;
	}

	/** Gets the y spacing between sprites on the sprite sheet */
	public int getSpriteSpacingY() {
		return spriteSpacingY;
	}

	/** Gets the width of a sprite on the sprite sheet */
	public int getSpriteWidth() {
		return spriteWidth;
	}

	/** Gets the height of a sprite on the sprite sheet */
	public int getSpriteHeight() {
		return spriteHeight;
	}
}
