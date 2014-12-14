package com.github.antag99.aquarria.tile;

import com.github.antag99.aquarria.world.World;

/**
 * {@link DefaultFrameStyle} implementation for walls
 */
public class WallFrameStyle extends DefaultFrameStyle {
	@Override
	protected boolean merge(World world, int x, int y, int x2, int y2) {
		return !world.inBounds(x2, y2) || world.getWallType(x2, y2) != WallType.air;
	}
}
