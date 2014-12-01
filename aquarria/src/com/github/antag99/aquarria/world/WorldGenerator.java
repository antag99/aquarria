package com.github.antag99.aquarria.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.util.PerlinNoise;

public class WorldGenerator {
	private World world;
	private long seed;

	public WorldGenerator(World world, long seed) {
		this.world = world;
		this.seed = seed;
	}

	public void generate() {
		// Clear the world
		for (int i = 0; i < world.getWidth(); ++i) {
			for (int j = 0; j < world.getHeight(); ++j) {
				world.setTileType(i, j, TileType.air);
			}
			world.setSurfaceLevel(i, 0);
		}

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

		// Set the spawnpoint
		world.setSpawnX(world.getWidth() / 2);
		world.setSpawnY(Math.max(world.getSurfaceLevel((int) world.getSpawnX()),
				world.getSurfaceLevel((int) world.getSpawnX() + 1)));

		Pixmap worldPixmap = worldToPixmap(world);
		PixmapIO.writePNG(Gdx.files.local("debug/world.png"), worldPixmap);
		worldPixmap.dispose();
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
