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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Stores data for a sprite sheet, which is a texture consisting of multiple smaller textures ('sprites').
 * <p>
 * Sprite sheets are used for tile graphics and entity animations (through the use of {@link AnimatedSpriteSheet}).
 */
public class SpriteSheet {
	/* the sprite matrix */
	private TextureRegion[][] sprites;
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
	/* the offset (in pixels) when rendering */
	private int renderOffsetX;
	private int renderOffsetY;
	/* the size (in pixels) when rendering */
	private int renderWidth;
	private int renderHeight;
	/* the tint to apply to rendered sprites */
	private Color renderTint;

	public SpriteSheet(TextureRegion texture,
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

		renderOffsetX = 0;
		renderOffsetY = 0;
		renderWidth = spriteWidth;
		renderHeight = spriteHeight;
		renderTint = Color.WHITE;

		sprites = new TextureRegion[spriteCountX][spriteCountY];

		for (int i = 0; i < spriteCountX; ++i) {
			for (int j = 0; j < spriteCountY; ++j) {
				int spriteX = spriteOffsetX + (spriteWidth + spriteSpacingX) * i;
				int spriteY = spriteOffsetY + (spriteHeight + spriteSpacingY) * j;

				sprites[i][j] = new TextureRegion(texture, spriteX, spriteY, spriteWidth, spriteHeight);
			}
		}
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

	/** Gets the x offset (in pixels) of rendered sprites */
	public int getRenderOffsetX() {
		return renderOffsetX;
	}

	/** Gets the y offset (in pixels) of rendered sprites */
	public int getRenderOffsetY() {
		return renderOffsetY;
	}

	/** Sets the x offset (in pixels) of rendered sprites */
	public void setRenderOffsetX(int renderOffsetX) {
		this.renderOffsetX = renderOffsetX;
	}

	/** Sets the y offset (in pixels) of rendered sprites */
	public void setRenderOffsetY(int renderOffsetY) {
		this.renderOffsetY = renderOffsetY;
	}

	/** Gets the width (in pixels) of rendered sprites */
	public int getRenderWidth() {
		return renderWidth;
	}

	/** Gets the height (in pixels) of rendered sprites */
	public int getRenderHeight() {
		return renderHeight;
	}

	/** Sets the width (in pixels) of rendered sprites */
	public void setRenderWidth(int renderWidth) {
		this.renderWidth = renderWidth;
	}

	/** Sets the height (in pixels) of rendered sprites */
	public void setRenderHeight(int renderHeight) {
		this.renderHeight = renderHeight;
	}

	/** Gets the sprite at the given position */
	public TextureRegion getSprite(int x, int y) {
		return sprites[x][y];
	}

	/** Sets the sprite at the given position */
	public void setSprite(int x, int y, TextureRegion sprite) {
		sprites[x][y] = sprite;
	}

	/** Gets the color to tint rendered sprites with */
	public Color getRenderTint() {
		return renderTint;
	}

	/** Sets the color to tint rendered sprites with */
	public void setRenderTint(Color renderTint) {
		this.renderTint = renderTint;
	}
}
