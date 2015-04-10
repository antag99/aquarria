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

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Stores data for a sprite sheet, which is a texture consisting of multiple smaller textures ('sprites').
 * <p>
 * Sprite sheets are used for tile graphics and entity animations (through the use of {@link SpriteAnimation}).
 */
public class SpriteSheet {
	private final Sprite[] sprites;
	private final int width;
	private final int height;
	private final SpriteGrid layout;

	/**
	 * Gets the width of the sheet
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the sheet
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the layout that was used to create this sheet
	 */
	public SpriteGrid getLayout() {
		return layout;
	}

	/**
	 * Gets the sprite at the given position in the sheet
	 */
	public Sprite getSprite(int x, int y) {
		if (!(x >= 0 && x < width))
			throw new ArrayIndexOutOfBoundsException(x);
		if (!(y >= 0 && y < height))
			throw new ArrayIndexOutOfBoundsException(y);
		return sprites[x + y * width];
	}

	/**
	 * Creates a new spritesheet from the given texture and sprite sheet layout.
	 */
	public SpriteSheet(TextureRegion texture, SpriteGrid layout) {
		this.layout = layout;
		this.width = layout.getSpriteCountX();
		this.height = layout.getSpriteCountY();
		this.sprites = new Sprite[width * height];

		int spriteWidth = layout.getSpriteWidth();
		int spriteHeight = layout.getSpriteHeight();
		int spriteOffsetX = layout.getSpriteOffsetX();
		int spriteOffsetY = layout.getSpriteOffsetY();
		int spriteSpacingX = layout.getSpriteSpacingX();
		int spriteSpacingY = layout.getSpriteSpacingY();

		float drawOffsetX = layout.getDrawOffsetX();
		float drawOffsetY = layout.getDrawOffsetY();
		float drawWidth = layout.getDrawWidth();
		float drawHeight = layout.getDrawHeight();

		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				Sprite sprite = new Sprite(
						new TextureRegion(texture,
								spriteOffsetX + i * (spriteWidth + spriteSpacingX),
								spriteOffsetY + j * (spriteHeight + spriteSpacingY),
								spriteWidth, spriteHeight),
						drawOffsetX, drawOffsetY,
						drawWidth, drawHeight);
				sprites[i + j * width] = sprite;
			}
		}
	}
}
