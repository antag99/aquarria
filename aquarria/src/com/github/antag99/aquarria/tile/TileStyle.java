package com.github.antag99.aquarria.tile;

import com.github.antag99.aquarria.world.World;

public interface TileStyle {
	public static TileStyle block = new TileStyle() {
		TileFrame[][] tileFrames = new TileFrame[16][14];
		{ /* this is an instance initializer */
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 14; ++j) {
					tileFrames[i][j] = new TileFrame(i, j);
				}
			}
		}

		@Override
		public TileFrame findFrame(World world, int x, int y) {
			boolean mergeTop = y + 1 == world.getHeight() || world.getTileType(x, y + 1).isSolid();
			boolean mergeRight = x + 1 == world.getWidth() || world.getTileType(x + 1, y).isSolid();
			boolean mergeBottom = y == 0 || world.getTileType(x, y - 1).isSolid();
			boolean mergeLeft = x == 0 || world.getTileType(x - 1, y).isSolid();

			String frame = TileFraming.getBlockFrame(mergeTop,
					mergeRight, mergeBottom, mergeLeft);
			return tileFrames[TileFraming.getBlockFrameX(frame)][TileFraming.getBlockFrameY(frame)];
		}
	};

	public static TileStyle wall = new TileStyle() {
		TileFrame[][] tileFrames = new TileFrame[16][14];
		{ /* this is an instance initializer */
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 14; ++j) {
					tileFrames[i][j] = new TileFrame(i, j);
				}
			}
		}

		@Override
		public TileFrame findFrame(World world, int x, int y) {
			WallType type = world.getWallType(x, y);
			boolean mergeTop = y + 1 == world.getHeight() || world.getWallType(x, y + 1) != type;
			boolean mergeRight = x + 1 == world.getWidth() || world.getWallType(x + 1, y) != type;
			boolean mergeBottom = y == 0 || world.getWallType(x, y - 1) != type;
			boolean mergeLeft = x == 0 || world.getWallType(x - 1, y) != type;

			String frame = TileFraming.getBlockFrame(mergeTop,
					mergeRight, mergeBottom, mergeLeft);
			return tileFrames[TileFraming.getBlockFrameX(frame)][TileFraming.getBlockFrameY(frame)];
		}
	};

	/**
	 * Finds the frame of the tile or wall at the given position.
	 */
	TileFrame findFrame(World world, int x, int y);
}
