package com.github.antag99.aquarria.tile;

import com.github.antag99.aquarria.world.World;

/**
 * {@link DefaultFrameStyle} implementation for tiles
 */
public class BlockFrameStyle extends DefaultFrameStyle {
	@Override
	protected boolean merge(World world, int x, int y, int x2, int y2) {
		return !world.inBounds(x2, y2) || world.getTileType(x2, y2) != TileType.air;
	}
}
