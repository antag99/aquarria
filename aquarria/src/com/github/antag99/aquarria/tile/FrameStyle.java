package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.AbstractType;
import com.github.antag99.aquarria.world.World;

public abstract class FrameStyle extends AbstractType {
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
	
	public static final FrameStyle block = new FrameStyle("block") {
		@Override
		public Frame findFrame(World world, int x, int y) {
			TileType type = world.getTileType(x, y);
			
			TileType top = y + 1 < world.getHeight() ? world.getTileType(x, y + 1) : type;
			TileType right = x + 1 < world.getWidth() ? world.getTileType(x + 1, y) : type;
			TileType bottom = y > 0 ? world.getTileType(x, y - 1) : type;
			TileType left = x > 0 ? world.getTileType(x - 1, y) : type;

			// TODO: Implement the different variations
			
			// X = merges with the current tile
			// - = does not merge with the current tile
			// ? = unknown
			
			if(top == TileType.air) {
				// - 
				//? ?
				// ?
				if(right == TileType.air) {
					// -
					//? -
					// ?
					if(bottom == TileType.air) {
						// -
						//? -
						// -
						if(left == TileType.air) {
							// -
							//- -
							// -
							return Frame.empty;
						} else {
							// -
							//X -
							// -
							return Frame.leftStrip;
						}
					} else {
						// -
						//? -
						// X
						if(left == TileType.air) {
							// -
							//- -
							// X
							return Frame.bottomStrip;
						} else {
							// -
							//X -
							// X
							return Frame.topRightCorner;
						}
					}
				} else {
					// -
					//? X
					// ?
					if(bottom == TileType.air) {
						// -
						//? X
						// -
						if(left == TileType.air) {
							// -
							//- X
							// -
							return Frame.rightStrip;
						} else {
							// -
							//X X
							// -
							return Frame.horizontalStrip;
						}
					} else {
						// -
						//? X
						// X
						if(left == TileType.air) {
							// -
							//- X
							// X
							return Frame.topLeftCorner;
						} else {
							// -
							//X X
							// X
							return Frame.topEdge;
						}
					}
				}
			} else {
				// X
				//? ?
				// ?
				if(right == TileType.air) {
					// X
					//? -
					// ?
					if(bottom == TileType.air) {
						// X
						//? -
						// -
						if(left == TileType.air) {
							// X
							//- -
							// -
							return Frame.topStrip;
						} else {
							// X
							//X -
							// -
							return Frame.bottomRightCorner;
						}
					} else {
						// X
						//? -
						// X
						if(left == TileType.air) {
							// X
							//- -
							// X
							return Frame.verticalStrip;
						} else {
							// X
							//X -
							// X
							return Frame.rightEdge;
						}
					}
				} else {
					// X
					//? X
					// ?
					if(bottom == TileType.air) {
						// X
						//? X
						// -
						if(left == TileType.air) {
							// X
							//- X
							// -
							return Frame.bottomLeftCorner;
						} else {
							// X
							//X X
							// -
							return Frame.bottomEdge;
						}
					} else {
						// X
						//? X
						// X
						if(left == TileType.air) {
							// X
							//- X
							// X
							return Frame.leftEdge;
						} else {
							// X
							//X X
							// X
							return Frame.full;
						}
					}
				}
			}
		}
	};
	
	public static final FrameStyle wall = new FrameStyle("wall") {
		@Override
		public Frame findFrame(World world, int x, int y) {
			WallType type = world.getWallType(x, y);
			
			WallType top = y + 1 < world.getHeight() ? world.getWallType(x, y + 1) : type;
			WallType right = x + 1 < world.getWidth() ? world.getWallType(x + 1, y) : type;
			WallType bottom = y > 0 ? world.getWallType(x, y - 1) : type;
			WallType left = x > 0 ? world.getWallType(x - 1, y) : type;
			
			// X = merges with the current wall
			// - = does not merge with the current wall
			// ? = unknown
			
			if(top == WallType.air) {
				// - 
				//? ?
				// ?
				if(right == WallType.air) {
					// -
					//? -
					// ?
					if(bottom == WallType.air) {
						// -
						//? -
						// -
						if(left == WallType.air) {
							// -
							//- -
							// -
							return Frame.empty;
						} else {
							// -
							//X -
							// -
							return Frame.leftStrip;
						}
					} else {
						// -
						//? -
						// X
						if(left == WallType.air) {
							// -
							//- -
							// X
							return Frame.bottomStrip;
						} else {
							// -
							//X -
							// X
							return Frame.topRightCorner;
						}
					}
				} else {
					// -
					//? X
					// ?
					if(bottom == WallType.air) {
						// -
						//? X
						// -
						if(left == WallType.air) {
							// -
							//- X
							// -
							return Frame.rightStrip;
						} else {
							// -
							//X X
							// -
							return Frame.horizontalStrip;
						}
					} else {
						// -
						//? X
						// X
						if(left == WallType.air) {
							// -
							//- X
							// X
							return Frame.topLeftCorner;
						} else {
							// -
							//X X
							// X
							return Frame.topEdge;
						}
					}
				}
			} else {
				// X
				//? ?
				// ?
				if(right == WallType.air) {
					// X
					//? -
					// ?
					if(bottom == WallType.air) {
						// X
						//? -
						// -
						if(left == WallType.air) {
							// X
							//- -
							// -
							return Frame.topStrip;
						} else {
							// X
							//X -
							// -
							return Frame.bottomRightCorner;
						}
					} else {
						// X
						//? -
						// X
						if(left == WallType.air) {
							// X
							//- -
							// X
							return Frame.verticalStrip;
						} else {
							// X
							//X -
							// X
							return Frame.rightEdge;
						}
					}
				} else {
					// X
					//? X
					// ?
					if(bottom == WallType.air) {
						// X
						//? X
						// -
						if(left == WallType.air) {
							// X
							//- X
							// -
							return Frame.bottomLeftCorner;
						} else {
							// X
							//X X
							// -
							return Frame.bottomEdge;
						}
					} else {
						// X
						//? X
						// X
						if(left == WallType.air) {
							// X
							//- X
							// X
							return Frame.leftEdge;
						} else {
							// X
							//X X
							// X
							return Frame.full;
						}
					}
				}
			}
		}
	};
	
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
			if(result == null) {
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
}
