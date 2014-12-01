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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.AbstractType;
import com.github.antag99.aquarria.world.World;

public abstract class FrameStyle extends AbstractType {
	public static Array<FrameStyle> getFrameStyles() {
		return AbstractType.getTypes(FrameStyle.class);
	}

	public static FrameStyle forName(String internalName) {
		return AbstractType.forName(FrameStyle.class, internalName);
	}

	/** Defines a frame, without duplicates (For example, the different bottom-left corner textures are not handled in framing logic). */
	public static class Frame {
		// Shared frame instances
		public static final Frame leftEdge = new Frame("leftEdge");
		public static final Frame topEdge = new Frame("topEdge");
		public static final Frame rightEdge = new Frame("rightEdge");
		public static final Frame bottomEdge = new Frame("bottomEdge");

		public static final Frame full = new Frame("full");
		public static final Frame empty = new Frame("empty");

		public static final Frame verticalStrip = new Frame("verticalStrip");
		public static final Frame horizontalStrip = new Frame("horizontalStrip");
		public static final Frame bottomStrip = new Frame("bottomStrip");
		public static final Frame topStrip = new Frame("topStrip");
		public static final Frame leftStrip = new Frame("leftStrip");
		public static final Frame rightStrip = new Frame("rightStrip");

		public static final Frame topLeftCorner = new Frame("topLeftCorner");
		public static final Frame topRightCorner = new Frame("topRightCorner");
		public static final Frame bottomLeftCorner = new Frame("bottomLeftCorner");
		public static final Frame bottomRightCorner = new Frame("bottomRightCorner");

		private String name;

		public Frame(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public static final FrameStyle block = new BlockFrameStyle("block");
	public static final FrameStyle wall = new WallFrameStyle("wall");

	/** Provides the textures for a specific set of frames */
	public static class FrameSkin {
		private TextureAtlas atlas;
		private ObjectMap<Frame, TextureRegion[]> cache;

		public FrameSkin(TextureAtlas atlas) {
			this.atlas = atlas;
			cache = new ObjectMap<>();
		}

		public TextureRegion getFrameTexture(Frame frame) {
			return getFrameTextures(frame)[0];
		}

		public TextureRegion[] getFrameTextures(Frame frame) {
			TextureRegion[] result = cache.get(frame);
			if (result == null) {
				Array<AtlasRegion> regions = atlas.findRegions(frame.name);
				result = regions.toArray(TextureRegion.class);
				cache.put(frame, result);
			}
			return result;
		}
	}

	public FrameStyle(String internalName) {
		super(internalName);
	}

	/** Finds the frame for the tile at the specified position */
	public abstract Frame findFrame(World world, int x, int y);

	@Override
	protected Class<? extends AbstractType> getTypeClass() {
		return FrameStyle.class;
	}
}
