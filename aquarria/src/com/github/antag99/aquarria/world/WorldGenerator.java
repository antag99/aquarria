/*******************************************************************************
 * Copyright (c) 2014, Anton Gustafsson
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

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.util.Direction;
import com.github.antag99.aquarria.util.PerlinNoise;

public class WorldGenerator {
	private World world;
	private long seed;

	public WorldGenerator(World world, long seed) {
		this.world = world;
		this.seed = seed;
	}

	public void generate() {
		world.clear();

		// Generate terrain...
		PerlinNoise surfaceNoise = new PerlinNoise(seed);
		int baseSurfaceLevel = world.getHeight() * 2 / 3;
		for (int i = 0; i < world.getWidth(); ++i) {
			float noise = surfaceNoise.get(i / 100f);

			int surfaceOffset = (int) (noise * 64f - 32f);
			int surfaceLevel = baseSurfaceLevel + surfaceOffset;

			world.setSurfaceLevel(i, surfaceLevel);

			for (int j = 0; j < surfaceLevel; ++j) {
				world.setTileType(i, j, TileType.stone);
			}
		}

		// Put dirt in the rocks... (This also leaves some rocks that appear to have been put in the dirt)
		// TODO: Improve the turbulence - it still looks smooth
		PerlinNoise dirtNoise = new PerlinNoise(seed);
		PerlinNoise dirtPerturbX = new PerlinNoise(seed * 31);
		PerlinNoise dirtPerturbY = new PerlinNoise(seed * 31 * 31);

		for (int i = 0; i < world.getWidth(); ++i) {
			for (int j = 0; j < world.getHeight(); ++j) {
				if (world.getTileType(i, j) == TileType.stone) {
					float noiseX = i / 20f + dirtPerturbX.get(i, j) * 5f - 2.5f;
					float noiseY = j / 20f + dirtPerturbY.get(i, j) * 5f - 2.5f;
					// Multiply the noise output with a gradient; this gradually increases the chance of a dirt tile,
					// the higher up the terrain is.
					if (dirtNoise.get(noiseX, noiseY) * (j / (float) world.getHeight()) > 0.25f) {
						world.setTileType(i, j, TileType.dirt);
					}
				}
			}
		}

		// Place dirt walls close to the surface
		for (int i = 0; i < world.getWidth(); ++i) {
			int surfaceLevel = world.getSurfaceLevel(i);
			// Offset by -2; don't place walls behind grass blocks
			for (int j = surfaceLevel - 20; j < surfaceLevel - 2; ++j) {
				world.setWallType(i, j, WallType.dirt);
			}
		}

		// Grassify the surface
		for (int i = 0; i < world.getWidth(); ++i) {
			int surfaceLevel = world.getSurfaceLevel(i);
			if (surfaceLevel > 0 && world.getTileType(i, surfaceLevel - 1) == TileType.dirt) {
				world.setTileType(i, surfaceLevel - 1, TileType.grass);
			}
		}

		Random treeRandom = new Random(seed);

		// Place some trees...
		for (int i = 5; i < world.getWidth() - 5; i += 10) {
			placeTree(i, world.getSurfaceLevel(i), treeRandom);
		}

		// Find frames...
		for (int i = 0; i < world.getWidth(); ++i) {
			for (int j = 0; j < world.getHeight(); ++j) {
				world.findTileFrame(i, j);
				world.findWallFrame(i, j);
			}
		}

		// Set the spawnpoint
		world.setSpawnX(world.getWidth() / 2);
		world.setSpawnY(Math.max(world.getSurfaceLevel((int) world.getSpawnX()),
				world.getSurfaceLevel((int) world.getSpawnX() + 1)));

		Pixmap worldPixmap = worldToPixmap(world);
		PixmapIO.writePNG(Gdx.files.local("debug/world.png"), worldPixmap);
		worldPixmap.dispose();
	}

	public void placeTree(int x, int y, Random random) {
		// Place stubs
		if (random.nextBoolean()) {
			world.setTileType(x - 1, y, TileType.tree);
			world.setAttached(x - 1, y, Direction.EAST, true);
			world.setAttached(x - 1, y, Direction.SOUTH, true);
		}
		if (random.nextBoolean()) {
			world.setTileType(x + 1, y, TileType.tree);
			world.setAttached(x + 1, y, Direction.WEST, true);
			world.setAttached(x + 1, y, Direction.SOUTH, true);
		}

		// Place trunks
		int treeHeight = random.nextInt(7) + 8;

		for (int i = 0; i < treeHeight; ++i) {
			world.setTileType(x, y + i, TileType.tree);
			world.setAttached(x, y + i, Direction.SOUTH, true);
		}

		// Place branches
		// How many tiles away the next branch will be placed
		int branchCounter = 2 + random.nextInt(3);
		// Direction in which the next branch won't be placed
		int branchSkip = 0;

		for (int i = 0; i < treeHeight; ++i) {
			if (--branchCounter == 0) {
				int branchDir = -branchSkip;
				if (branchDir == 0) {
					branchDir = random.nextBoolean() ? 1 : -1;
				}

				world.setTileType(x + branchDir, y + i, TileType.tree);
				world.setAttached(x + branchDir, y + i, Direction.get(-branchDir, 0), true);

				branchCounter = random.nextInt(3);
				branchSkip = branchCounter == 1 ? branchDir : 0;
			}
		}
	}

	static Pixmap worldToPixmap(World world) {
		Pixmap result = new Pixmap(world.getWidth(), world.getHeight(), Format.RGBA8888);

		for (int i = 0; i < world.getWidth(); ++i) {
			for (int j = 0; j < world.getHeight(); ++j) {
				Color color;
				TileType type = world.getTileType(i, j);

				if (type == TileType.air) {
					color = Color.CLEAR;
				} else if (type == TileType.dirt) {
					color = Color.MAROON;
				} else if (type == TileType.grass) {
					color = Color.GREEN;
				} else if (type == TileType.stone) {
					color = Color.GRAY;
				} else {
					color = Color.PINK;
				}

				result.drawPixel(i, world.getHeight() - j + 1, Color.rgba8888(color));
			}
		}

		return result;
	}
}
