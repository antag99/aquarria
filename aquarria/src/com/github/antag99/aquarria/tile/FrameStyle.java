package com.github.antag99.aquarria.tile;

import java.util.Random;

import com.badlogic.gdx.math.GridPoint2;
import com.github.antag99.aquarria.world.World;

public abstract class FrameStyle {
	
	
	public abstract void findFrame(TileType type, int x, int y, World world, Random random, GridPoint2 frame);
	
	public static final FrameStyle defaultFrameStyle = new FrameStyle() {
		@Override
		public void findFrame(TileType type, int x, int y, World world, Random random, GridPoint2 frame) {
			TileType top = y + 1 < world.getHeight() ? world.getTileType(x, y + 1) : TileType.air;
			TileType right = x + 1 < world.getWidth() ? world.getTileType(x + 1, y) : TileType.air;
			TileType bottom = y > 1 ? world.getTileType(x, y - 1) : TileType.air;
			TileType left = x > 1 ? world.getTileType(x - 1, y) : TileType.air;
			
			frame.x = 1;
			frame.y = 1;
			
			// TODO: Implement the different variations
			
			// X = merges with the current tile
			// - = does not merge with the current tile
			// ? = unknown
			
			if(top != type) {
				// - 
				//? ?
				// ?
				if(right != type) {
					// -
					//? -
					// ?
					if(bottom != type) {
						// -
						//? -
						// -
						if(left != type) {
							// -
							//- -
							// -
							frame.x = 9;
							frame.y = 3;
						} else {
							// -
							//X -
							// -
							frame.x = 12;
							frame.y = 0;
						}
					} else {
						// -
						//? -
						// X
						if(left != type) {
							// -
							//- -
							// X
							frame.x = 6;
							frame.y = 0;
						} else {
							// -
							//X -
							// X
							frame.x = 1;
							frame.y = 3;
						}
					}
				} else {
					// -
					//? X
					// ?
					if(bottom != type) {
						// -
						//? X
						// -
						if(left != type) {
							// -
							//- X
							// -
							frame.x = 9;
							frame.y = 0;
						} else {
							// -
							//X X
							// -
							frame.x = 6;
							frame.y = 4;
						}
					} else {
						// -
						//? X
						// X
						if(left != type) {
							// -
							//- X
							// X
							frame.x = 0;
							frame.y = 3;
						} else {
							// -
							//X X
							// X
							frame.x = 1;
							frame.y = 0;
						}
					}
				}
			} else {
				// X
				//? ?
				// ?
				if(right != type) {
					// X
					//? -
					// ?
					if(bottom != type) {
						// X
						//? -
						// -
						if(left != type) {
							// X
							//- -
							// -
							frame.x = 6;
							frame.y = 3;
						} else {
							// X
							//X -
							// -
							frame.x = 1;
							frame.y = 4;
						}
					} else {
						// X
						//? -
						// X
						if(left != type) {
							// X
							//- -
							// X
							frame.x = 6;
							frame.y = 4;
						} else {
							// X
							//X -
							// X
							frame.x = 4;
							frame.y = 0;
						}
					}
				} else {
					// X
					//? X
					// ?
					if(bottom != type) {
						// X
						//? X
						// -
						if(left != type) {
							// X
							//- X
							// -
							frame.x = 0;
							frame.y = 4;
						} else {
							// X
							//X X
							// -
							frame.x = 1;
							frame.y = 2;
						}
					} else {
						// X
						//? X
						// X
						if(left != type) {
							// X
							//- X
							// X
							frame.x = 0;
							frame.y = 0;
						} else {
							// X
							//X X
							// X
							frame.x = 1;
							frame.y = 1;
						}
					}
				}
			}
		}
	};
}
