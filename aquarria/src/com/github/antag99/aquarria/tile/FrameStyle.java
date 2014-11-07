package com.github.antag99.aquarria.tile;

import java.util.Random;

import com.badlogic.gdx.math.GridPoint2;
import com.github.antag99.aquarria.world.World;

public abstract class FrameStyle {
	
	
	public abstract void findFrame(TileType type, int x, int y, World world, Random random, GridPoint2 frame);
	
	public static final FrameStyle defaultFrameStyle = new FrameStyle() {
		@Override
		public void findFrame(TileType type, int x, int y, World world, Random random, GridPoint2 frame) {
			TileType top = y + 1 < world.getHeight() ? world.getTileType(x, y + 1) : type;
			TileType right = x + 1 < world.getWidth() ? world.getTileType(x + 1, y) : type;
			TileType bottom = y > 0 ? world.getTileType(x, y - 1) : type;
			TileType left = x > 0 ? world.getTileType(x - 1, y) : type;
			
			frame.x = 1;
			frame.y = 1;

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
						if(left == TileType.air) {
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
					if(bottom == TileType.air) {
						// -
						//? X
						// -
						if(left == TileType.air) {
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
						if(left == TileType.air) {
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
						if(left == TileType.air) {
							// X
							//- -
							// X
							frame.x = 5;
							frame.y = 0;
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
					if(bottom == TileType.air) {
						// X
						//? X
						// -
						if(left == TileType.air) {
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
						if(left == TileType.air) {
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
