package com.github.antag99.aquarria.world;

import com.badlogic.gdx.utils.Array;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.entity.ItemEntity;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.util.DynamicIDMapping;

public class World {
	private int width;
	private int height;
	
	private float spawnX, spawnY;
	
	private short[] tileMatrix;
	private DynamicIDMapping<TileType> tileMapping;
	
	private short[] wallMatrix;
	private DynamicIDMapping<WallType> wallMapping;
	
	private short[] surfaceLevel;
	
	private Array<Entity> entities;
	
	private LightManager lightManager;
	private LiquidManager liquidManager;
	
	public World(int width, int height) {
		this.width = width;
		this.height = height;
		lightManager = new LightManager(this);
		liquidManager = new LiquidManager(this);
		spawnX = width / 2f;
		spawnY = height / 2f;
		tileMatrix = new short[width * height];
		tileMapping = new DynamicIDMapping<TileType>();
		// Ensure that ID 0 is assigned to air
		tileMapping.getID(TileType.air);
		wallMatrix = new short[width * height];
		wallMapping = new DynamicIDMapping<WallType>();
		wallMapping.getID(WallType.air);
		entities = new Array<Entity>();
		surfaceLevel = new short[width];
	}

	public float getSpawnX() {
		return spawnX;
	}
	
	public float getSpawnY() {
		return spawnY;
	}
	
	public void setSpawnX(float spawnX) {
		this.spawnX = spawnX;
	}
	
	public void setSpawnY(float spawnY) {
		this.spawnY = spawnY;
	}
	
	public void checkBounds(int x, int y) {
		if(x < 0 || x >= width) throw new ArrayIndexOutOfBoundsException(x);
		if(y < 0 || y >= height) throw new ArrayIndexOutOfBoundsException(y);
	}
	
	public boolean inBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height; 
	}
	
	public TileType getTileType(int x, int y) {
		checkBounds(x, y);
		
		return tileMapping.getObject(tileMatrix[y * width + x]);
	}
	
	public void setTileType(int x, int y, TileType type) {
		checkBounds(x, y);
		
		tileMatrix[y * width + x] = (short) tileMapping.getID(type);
	}
	
	public WallType getWallType(int x, int y) {
		checkBounds(x, y);
		
		return wallMapping.getObject(wallMatrix[y * width + x]);
	}
	
	public void setWallType(int x, int y, WallType type) {
		checkBounds(x, y);
		
		wallMatrix[y * width + x] = (short) wallMapping.getID(type);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSurfaceLevel(int x) {
		checkBounds(x, 0);
		
		return surfaceLevel[x];
	}
	
	public void setSurfaceLevel(int x, int level) {
		checkBounds(x, 0);
		
		surfaceLevel[x] = (short) level;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		entity.setWorld(this);
	}
	
	public void removeEntity(Entity entity) {
		entities.removeValue(entity, true);
		entity.setWorld(null);
	}
	
	public void dropItem(Item item, float x, float y) {
		ItemEntity itemEntity = new ItemEntity(item);
		itemEntity.setX(x);
		itemEntity.setY(y);
		addEntity(itemEntity);
	}
	
	public Array<Entity> getEntities() {
		return entities;
	}
	
	public LightManager getLightManager() {
		return lightManager;
	}
	
	public LiquidManager getLiquidManager() {
		return liquidManager;
	}
	
	public void update(float delta) {
		liquidManager.update(delta);
		
		for(int i = 0; i < entities.size; ++i) {
			Entity entity = entities.get(i);
			if(entity.isActive()) {
				entity.update(delta);
			}
		}
		
		for(int i = 0; i < entities.size; ++i) {
			Entity entity = entities.get(i);
			if(!entity.isActive()) {
				entities.removeIndex(i);
			}
		}
	}
}
