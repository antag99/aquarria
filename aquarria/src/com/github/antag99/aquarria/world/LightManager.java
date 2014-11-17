package com.github.antag99.aquarria.world;

import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;

public class LightManager {
	private World world;
	private byte[] lightBuffer;
	
	public LightManager(World world) {
		this.world = world;
		lightBuffer = new byte[world.getWidth() * world.getHeight()];
	}
	
	public float getLight(int x, int y) {
		return (lightBuffer[world.getHeight() * y + x] & 0xff) / 255f;
	}
	
	public void setLight(int x, int y, float light) {
		lightBuffer[y * world.getHeight() + x] = (byte)(light * 255);
	}
	
	public void computeLight(int x, int y, int width, int height) {
		for(int i = 0; i < width; ++i) {
			for(int j = 0; j < height; ++j) {
				if(world.getTileType(x + i, y + j) == TileType.air && world.getWallType(x + i, y + j) == WallType.air) {
					setLight(x + i, y + j, 1f);
				} else {
					setLight(x + i, y + j, 0f);
				}
			}
		}
		
		for(int i = 0; i < width; ++i) {
			for(int j = 0; j < height; ++j) {
				updateLight(x + i, y + j, x, y, x + width, y + height, getLight(x + i, y + j));
			}
		}
	}
	
	private void updateLight(int x, int y, int minX, int minY, int maxX, int maxY, float light) {
		if(x > minX) updateAdjacentLight(x - 1, y, minX, minY, maxX, maxY, light - 0.15f);
		if(y > minY) updateAdjacentLight(x, y - 1, minX, minY, maxX, maxY, light - 0.15f);
		if(x + 1 < maxX) updateAdjacentLight(x + 1, y, minX, minY, maxX, maxY, light - 0.15f);
		if(y + 1 < maxY) updateAdjacentLight(x, y + 1, minX, minY, maxX, maxY, light - 0.15f);
	}
	
	private void updateAdjacentLight(int x, int y, int minX, int minY, int maxX, int maxY, float light) {
		if(light > getLight(x, y)) {
			setLight(x, y, light);
			updateLight(x, y, minX, minY, maxX, maxY, light);
		}
	}
}
