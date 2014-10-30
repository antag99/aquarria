package com.github.antag99.aquarria;

import java.util.Random;

import com.badlogic.gdx.utils.Array;

public class WorldGenerator {
	
	private World world;
	private long seed;
	private Random random;
	private Array<Runnable> tasks = new Array<Runnable>();
	
	public WorldGenerator() {
		// Clear the world
		tasks.add(() -> {
			for(int i = 0; i < world.getWidth(); ++i) {
				for(int j = 0; j < world.getHeight(); ++j) {
					world.setTileType(i, j, TileType.air);
				}
			}
		});
		
		// Generate surface
		tasks.add(() -> {
			for(int i = 0; i < world.getWidth(); ++i) {
				for(int j = 0; j < world.getHeight() / 2; ++j) {
					world.setTileType(i, j, TileType.dirt);
				}
				world.setSurfaceLevel(i, world.getHeight() / 2);
			}
		});
		
		// Grassify the surface
		tasks.add(() -> {
			for(int i = 0; i < world.getWidth(); ++i) {
				world.setTileType(i, world.getSurfaceLevel(i), TileType.grass);
				world.setSurfaceLevel(i, world.getSurfaceLevel(i) + 1);
			}
		});
		
		// Set the spawnpoint
		tasks.add(() -> {
			world.getSpawnPoint().y = world.getSurfaceLevel((int) world.getSpawnPoint().x);
		});
	}
	
	public void generate(World world, long seed) {
		this.world = world;
		this.seed = seed;
		
		for(Runnable task : tasks) {
			// Reset random for every task, this minimizes the difference of worlds 
			// with the same seed when changing world generation
			this.random = new Random(this.seed);
			task.run();
		}
		
		this.world = null;
		this.seed = 0L;
		this.random = null;
	}
}
