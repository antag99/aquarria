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
	/* the sprite grid */
	private SpriteGrid grid;
	/* the sprite matrix */
	private TextureRegion[][] sprites;
	/* the offset (in pixels) when rendering */
	private int renderOffsetX;
	private int renderOffsetY;
	/* the size (in pixels) when rendering */
	private int renderWidth;
	private int renderHeight;

	public SpriteSheet(TextureRegion texture, SpriteGrid grid) {
		this.grid = grid;

		renderOffsetX = 0;
		renderOffsetY = 0;
		renderWidth = grid.getSpriteWidth();
		renderHeight = grid.getSpriteHeight();

		sprites = new TextureRegion[grid.getSpriteCountX()][grid.getSpriteCountY()];

		for (int i = 0; i < grid.getSpriteCountX(); ++i) {
			for (int j = 0; j < grid.getSpriteCountY(); ++j) {
				int spriteX = grid.getSpriteOffsetX() + (grid.getSpriteWidth() + grid.getSpriteSpacingX()) * i;
				int spriteY = grid.getSpriteOffsetY() + (grid.getSpriteHeight() + grid.getSpriteSpacingY()) * j;

				sprites[i][j] = new TextureRegion(texture, spriteX, spriteY,
						grid.getSpriteWidth(), grid.getSpriteHeight());
			}
		}
	}

	public SpriteGrid getGrid() {
		return grid;
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
}
