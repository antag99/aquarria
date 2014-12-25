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

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.github.antag99.aquarria.Type;
import com.github.antag99.aquarria.item.ItemType;

public final class TileType extends Type {
	public static TileType air;
	public static TileType dirt;
	public static TileType stone;
	public static TileType grass;
	public static TileType tree;

	private String displayName;
	private boolean solid;
	private ItemType drop;
	private TextureAtlas atlas;
	private FrameStyle style;
	private Rectangle frame = new Rectangle(0f, 0f, 1f, 1f);

	public TileType() {
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	public void setAtlas(TextureAtlas atlas) {
		this.atlas = atlas;
	}

	public FrameStyle getStyle() {
		return style;
	}

	public void setStyle(FrameStyle style) {
		this.style = style;
	}

	public ItemType getDrop() {
		return drop;
	}

	public void setDrop(ItemType drop) {
		this.drop = drop;
	}

	/**
	 * Gets the frame of this tile type. This specifies the
	 * rendering bounds of a tile, relative to the tile's
	 * position.
	 */
	public Rectangle getFrame() {
		return frame;
	}

	public void setFrame(Rectangle frame) {
		this.frame.set(frame);
	}
}
