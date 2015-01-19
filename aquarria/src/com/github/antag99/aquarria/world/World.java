/*******************************************************************************
 * Copyright (c) 2014-2015, Anton Gustafsson
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Aquarria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.github.antag99.aquarria.world;

import java.util.Arrays;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.aquarria.Direction;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.entity.ItemEntity;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;

public class World {
	public static final float PIXELS_PER_METER = 16;

	private int width;
	private int height;

	private float spawnX, spawnY;

	private TileType[] tiles;
	private WallType[] walls;

	private short[] surfaceLevel;
	private byte[] light;

	private byte[] tileAttachment;

	private Array<Entity> entities;

	private byte[] liquidLevel;
	private float tickCounter;
	private IntArray activeLiquids;

	// Liquid simulation uses a fixed time step,
	// as it is quite hard to interpolate liquid movement
	// based on the time since the last frame.
	private static final float TICK = (1f / 255);
	private static final int MAX_LIQUID = 255;

	public World(int width, int height) {
		this.width = width;
		this.height = height;

		tiles = new TileType[width * height];
		walls = new WallType[width * height];

		clear();
	}

	/**
	 * Clears this world
	 */
	public void clear() {
		spawnX = width / 2f;
		spawnY = height / 2f;
		Arrays.fill(tiles, TileType.air);
		Arrays.fill(walls, WallType.air);
		entities = new Array<Entity>();
		surfaceLevel = new short[width];
		light = new byte[width * height];
		liquidLevel = new byte[width * height];
		tileAttachment = new byte[width * height];
		activeLiquids = new IntArray();
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
		if (x < 0 || x >= width)
			throw new ArrayIndexOutOfBoundsException(x);
		if (y < 0 || y >= height)
			throw new ArrayIndexOutOfBoundsException(y);
	}

	public boolean inBounds(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	public TileType getTileType(int x, int y) {
		checkBounds(x, y);

		return tiles[y * width + x];
	}

	public void setTileType(int x, int y, TileType type) {
		checkBounds(x, y);

		if (type == null) {
			throw new NullPointerException();
		}

		tiles[y * width + x] = type;
	}

	public WallType getWallType(int x, int y) {
		checkBounds(x, y);

		return walls[y * width + x];
	}

	public void setWallType(int x, int y, WallType type) {
		checkBounds(x, y);

		if (type == null) {
			throw new NullPointerException();
		}

		walls[y * width + x] = type;
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

	private void tick() {
		for (int i = 0; i < activeLiquids.size; ++i) {
			int position = activeLiquids.items[i];
			int x = position % width;
			int y = position / width;

			flow(x, y);
		}
	}

	private void flow(int x, int y) {
		if (getTileType(x, y).isSolid())
			return;

		if (!flow(x, y, x, y - 1, 16, false) || getLiquid(x, y - 1) == MAX_LIQUID) {
			// Randomizing the order prevents issues related to
			// water not flowing in some fixed direction when just one unit is left.
			int dir = MathUtils.random(0, 1) * 2 - 1;
			flow(x, y, x + dir, y, 16, true);
			flow(x, y, x - dir, y, 16, true);
		}
	}

	/**
	 * @param smooth Whether the liquids between the two tiles should be smoothed to the same amount.
	 * @return Whether any liquid was moved.
	 */
	private boolean flow(int srcX, int srcY, int dstX, int dstY, int speed, boolean smooth) {
		if (inBounds(dstX, dstY) && !getTileType(dstX, dstY).isSolid()) {
			int srcLiquid = getLiquid(srcX, srcY);
			int dstLiquid = getLiquid(dstX, dstY);

			int amount = speed;

			if (smooth) {
				int targetLiquid = (srcLiquid + dstLiquid) / 2;
				int remainder = (srcLiquid + dstLiquid) % 2;
				amount = Math.min(targetLiquid - dstLiquid, amount) + remainder;
			} else {
				amount = Math.min(MAX_LIQUID - dstLiquid, amount);
			}

			amount = Math.min(srcLiquid, amount);

			setLiquid(srcX, srcY, srcLiquid - amount);
			setLiquid(dstX, dstY, dstLiquid + amount);

			return amount != 0;
		}

		return false;
	}

	public float getLight(int x, int y) {
		return (light[width * y + x] & 0xff) / 255f;
	}

	public void setLight(int x, int y, float light) {
		this.light[y * width + x] = (byte) (light * 255);
	}

	public void computeLight(int x, int y, int width, int height) {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				if (!getTileType(x + i, y + j).isSolid() &&
						getWallType(x + i, y + j) == WallType.air) {
					setLight(x + i, y + j, 1f);
				} else {
					setLight(x + i, y + j, 0f);
				}
			}
		}

		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				updateLight(x + i, y + j, x, y, x + width, y + height, getLight(x + i, y + j));
			}
		}
	}

	private void updateLight(int x, int y, int minX, int minY, int maxX, int maxY, float light) {
		if (x > minX)
			updateAdjacentLight(x - 1, y, minX, minY, maxX, maxY, light - 0.15f);
		if (y > minY)
			updateAdjacentLight(x, y - 1, minX, minY, maxX, maxY, light - 0.15f);
		if (x + 1 < maxX)
			updateAdjacentLight(x + 1, y, minX, minY, maxX, maxY, light - 0.15f);
		if (y + 1 < maxY)
			updateAdjacentLight(x, y + 1, minX, minY, maxX, maxY, light - 0.15f);
	}

	private void updateAdjacentLight(int x, int y, int minX, int minY, int maxX, int maxY, float light) {
		if (light > getLight(x, y)) {
			setLight(x, y, light);
			updateLight(x, y, minX, minY, maxX, maxY, light);
		}
	}

	public int getLiquid(int x, int y) {
		checkBounds(x, y);

		return liquidLevel[x + y * width] & 0xff;
	}

	public void setLiquid(int x, int y, int liquid) {
		checkBounds(x, y);

		int position = x + y * width;
		liquidLevel[position] = (byte) liquid;

		boolean liquidActive = activeLiquids.contains(position);
		if (liquid != 0 && !liquidActive)
			activeLiquids.add(position);
		if (liquid == 0 && liquidActive)
			activeLiquids.removeValue(position);
	}

	/**
	 * Gets whether the tile is attached to another tile in the given direction.
	 * Tiles attached to another tile will be destroyed when that tile is destroyed.
	 * 
	 * @param direction The direction towards the other tile.
	 */
	public boolean isAttached(int x, int y, Direction direction) {
		return (tileAttachment[x + y * width] & (1 << direction.ordinal())) != 0;
	}

	/**
	 * Sets whether the tile is attached to another tile in the given direction.
	 * Tiles attached to another tile will be destroyed when that tile is destroyed.
	 * 
	 * @param direction The direction towards the other tile
	 * @param attached Whether the given tile should be attached to the other tile.
	 */
	public void setAttached(int x, int y, Direction direction, boolean attached) {
		if (!attached)
			tileAttachment[x + y * width] &= ~(1 << direction.ordinal());
		else
			tileAttachment[x + y * width] |= 1 << direction.ordinal();
	}

	/**
	 * Recursively detects detached tiles and destroys them
	 * 
	 * @param player The player that caused the attachment to be changed,
	 *            and therefore should be responsible for destroying the tiles.
	 *            May be null, which will suppress any item drops.
	 */
	public void checkAttachment(int x, int y, PlayerEntity player) {
		// TODO: Add some property to indicate whether a tile can be attached to another
		if (getTileType(x, y) == TileType.air) {
			for (Direction direction : Direction.values()) {
				if (inBounds(x + direction.getHorizontal(), y + direction.getVertical()) &&
						isAttached(x + direction.getHorizontal(), y + direction.getVertical(), direction.opposite())) {
					destroyTile(x + direction.getHorizontal(), y + direction.getVertical(), player);
				}
			}
		}
	}

	public void clearAttachment(int x, int y) {
		tileAttachment[x + y * width] = 0;
	}

	/**
	 * Destroys the tile at the given position. This also checks the attachment of the adjacent
	 * tiles and drops items if the tile was destroyed by a player.
	 * 
	 * @param player The player that destroyed the tile. May be null,
	 *            which will suppress any item drops.
	 * @return Whether the tile at the specified position was destroyed
	 */
	public boolean destroyTile(int x, int y, PlayerEntity player) {
		TileType type = getTileType(x, y);
		if (type != TileType.air) {
			if (player != null && type.getDrop() != null) {
				dropItem(new Item(type.getDrop()), x, y);
			}

			setTileType(x, y, TileType.air);
			clearAttachment(x, y);
			checkAttachment(x, y, player);

			return true;
		}
		return false;
	}

	public boolean placeTile(int x, int y, TileType type, PlayerEntity player) {
		if (getTileType(x, y) == TileType.air) {
			setTileType(x, y, type);
			checkAttachment(x, y, player);

			return true;
		}

		return false;
	}

	/**
	 * Destroys the wall at the given position. This also causes
	 * the wall to drop an item if it was destroyed by a player.
	 * 
	 * @param player The player that destroyed the wall. May be null,
	 *            which will suppress any item drops.
	 * @return Whether the wall at the specified position was destroyed
	 */
	public boolean destroyWall(int x, int y, PlayerEntity player) {
		WallType type = getWallType(x, y);
		if (type != WallType.air) {
			if (type.getDrop() != null) {
				dropItem(new Item(type.getDrop()), x, y);
			}

			setWallType(x, y, WallType.air);

			return true;
		}
		return false;
	}

	public boolean placeWall(int x, int y, WallType type, PlayerEntity player) {
		if (getWallType(x, y) == WallType.air) {
			setWallType(x, y, type);

			return true;
		}

		return false;
	}

	public void update(float delta) {
		tickCounter += delta;

		while (tickCounter > TICK) {
			tick();

			tickCounter -= TICK;
		}

		for (int i = 0; i < entities.size; ++i) {
			Entity entity = entities.get(i);
			if (entity.isActive()) {
				entity.update(delta);
			}
		}

		for (int i = 0; i < entities.size; ++i) {
			Entity entity = entities.get(i);
			if (!entity.isActive()) {
				entities.removeIndex(i);
			}
		}
	}
}
