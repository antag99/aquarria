package com.github.antag99.aquarria;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;

public class World {
	private int width;
	private int height;
	
	private short[] tileMatrix;
	private ObjectIntMap<TileType> tileMapping;
	private IntMap<TileType> idMapping;
	
	private short[] surfaceLevel;
	
	public World(int width, int height) {
		this.width = width;
		this.height = height;
		tileMatrix = new short[width * height];
		tileMapping = new ObjectIntMap<TileType>();
		idMapping = new IntMap<TileType>();
		surfaceLevel = new short[width];
		// Add air manually, as it is the default value
		tileMapping.put(TileType.air, 0);
		idMapping.put(0, TileType.air);
	}
	
	public TileType getTileType(int x, int y) {
		return idMapping.get(tileMatrix[y * width + x]);
	}
	
	public void setTileType(int x, int y, TileType type) {
		int id = tileMapping.get(type, -1);
		if(id == -1) {
			id = tileMapping.size;
			tileMapping.put(type, id);
			idMapping.put(id, type);
		}
		tileMatrix[y * width + x] = (short) id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSurfaceLevel(int x) {
		return surfaceLevel[x];
	}
	
	public void setSurfaceLevel(int x, int level) {
		surfaceLevel[x] = (short) level;
	}
}
