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
