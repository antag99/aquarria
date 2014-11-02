package com.github.antag99.aquarria.world;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.entity.ItemEntity;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.tile.TileType;

public class World {
	private int width;
	private int height;
	
	private float spawnX, spawnY;
	
	private short[] tileMatrix;
	private ObjectIntMap<TileType> tileMapping;
	private IntMap<TileType> idMapping;
	
	private short[] surfaceLevel;
	
	private Array<Entity> entities;
	
	public World(int width, int height) {
		this.width = width;
		this.height = height;
		spawnX = width / 2f;
		spawnY = height / 2f;
		tileMatrix = new short[width * height];
		tileMapping = new ObjectIntMap<TileType>();
		idMapping = new IntMap<TileType>();
		surfaceLevel = new short[width];
		// Add air manually, as it is the default value
		tileMapping.put(TileType.air, 0);
		idMapping.put(0, TileType.air);
		entities = new Array<Entity>();
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
	
	public void update(float delta) {
		for(int i = 0; i < entities.size; ++i) {
			Entity entity = entities.get(i);
			entity.update(delta);
			if(!entity.isActive()) {
				entities.removeIndex(i);
			}
		}
	}
}
