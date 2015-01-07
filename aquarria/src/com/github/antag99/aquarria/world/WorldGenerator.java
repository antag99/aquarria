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
import com.badlogic.gdx.utils.Array;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.util.Direction;
import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleCombiner;
import com.sudoplay.joise.module.ModuleCombiner.CombinerType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;
import com.sudoplay.joise.module.ModuleGradient;
import com.sudoplay.joise.module.ModuleSelect;
import com.sudoplay.joise.module.ModuleTranslateDomain;

public class WorldGenerator {
	private World world;
	private long seed;
	private Array<WorldGeneratorTask> tasks = new Array<>();

	public WorldGenerator(World world, long seed) {
		this.world = world;
		this.seed = seed;

		addTasks();
	}

	void addTasks() {
		// Clear the world
		tasks.add((generator, seed) -> {
			generator.getWorld().clear();
		});

		// Generate base terrain
		tasks.add((generator, seed) -> {
			// Base the terrain height on fractal noise
			ModuleFractal fractal = new ModuleFractal();
			fractal.setType(FractalType.FBM);
			fractal.setAllSourceBasisTypes(BasisType.GRADIENT);
			fractal.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
			fractal.setFrequency(1f / 100f);
			fractal.setNumOctaves(2);
			fractal.setSeed(seed);

			final Module source = fractal;

			int baseSurfaceLevel = generator.getHeight() * 2 / 3;

			for (int i = 0; i < generator.getWidth(); ++i) {
				// Sample the noise at Y level 0, as the joise
				// library dosen't support 1D noise.
				double noise = source.get(i, 0f);

				// Returns values in the range [-32, 32]
				int surfaceOffset = (int) (noise * 32.0);
				int surfaceLevel = baseSurfaceLevel + surfaceOffset;

				generator.setSurfaceLevel(i, surfaceLevel);

				for (int j = 0; j < surfaceLevel; ++j) {
					generator.setTileType(i, j, TileType.stone);
				}
			}
		});

		// Put dirt in the rocks... (This also leaves some rocks that appear to have been put in the dirt)
		tasks.add((generator, seed) -> {
			ModuleFractal fractal = new ModuleFractal();
			fractal.setType(FractalType.FBM);
			fractal.setAllSourceBasisTypes(BasisType.GRADIENT);
			fractal.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
			fractal.setFrequency(1f / 20f);
			fractal.setNumOctaves(2);
			fractal.setSeed(seed);

			ModuleGradient gradient = new ModuleGradient();
			gradient.setGradient(0, 0, 0, world.getHeight() * 0.75);

			ModuleCombiner combiner = new ModuleCombiner();
			combiner.setType(CombinerType.ADD);
			combiner.setSource(0, fractal);
			combiner.setSource(1, gradient);

			ModuleFractal perturbFractalX = new ModuleFractal();
			perturbFractalX.setType(FractalType.FBM);
			perturbFractalX.setAllSourceBasisTypes(BasisType.GRADIENT);
			perturbFractalX.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
			perturbFractalX.setFrequency(1f / 20f);
			perturbFractalX.setNumOctaves(2);
			perturbFractalX.setSeed(seed);

			ModuleCombiner perturbFractalXMult = new ModuleCombiner();
			perturbFractalXMult.setType(CombinerType.MULT);
			perturbFractalXMult.setSource(0, perturbFractalX);
			perturbFractalXMult.setSource(1, 40.0);

			ModuleFractal perturbFractalY = new ModuleFractal();
			perturbFractalY.setType(FractalType.FBM);
			perturbFractalY.setAllSourceBasisTypes(BasisType.GRADIENT);
			perturbFractalY.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
			perturbFractalY.setFrequency(1f / 20f);
			perturbFractalY.setNumOctaves(2);
			perturbFractalY.setSeed(seed * 31);

			ModuleCombiner perturbFractalYMult = new ModuleCombiner();
			perturbFractalYMult.setType(CombinerType.MULT);
			perturbFractalYMult.setSource(0, perturbFractalY);
			perturbFractalYMult.setSource(1, 40.0);

			ModuleTranslateDomain perturb = new ModuleTranslateDomain();
			perturb.setAxisXSource(perturbFractalXMult);
			perturb.setAxisYSource(perturbFractalYMult);
			perturb.setSource(combiner);

			ModuleSelect select = new ModuleSelect();
			select.setControlSource(perturb);
			select.setThreshold(0.5);
			select.setLowSource(0.0);
			select.setHighSource(1.0);

			for (int i = 0; i < generator.getWidth(); ++i) {
				for (int j = 0; j < generator.getHeight(); ++j) {
					if (generator.getTileType(i, j) == TileType.stone && select.get(i, j) == 1.0) {
						generator.setTileType(i, j, TileType.dirt);
					}
				}
			}
		});

		// Place dirt walls close to the surface
		tasks.add((generator, seed) -> {
			for (int i = 0; i < generator.getWidth(); ++i) {
				int surfaceLevel = generator.getSurfaceLevel(i);
				// Offset by -2; don't place walls behind grass blocks
				for (int j = surfaceLevel - 20; j < surfaceLevel - 2; ++j) {
					generator.setWallType(i, j, WallType.dirt);
				}
			}
		});

		// Grassify the surface
		tasks.add((generator, seed) -> {
			// Grassify the surface
			for (int i = 0; i < generator.getWidth(); ++i) {
				int surfaceLevel = generator.getSurfaceLevel(i);
				if (surfaceLevel > 0 && generator.getTileType(i, surfaceLevel - 1) == TileType.dirt) {
					generator.setTileType(i, surfaceLevel - 1, TileType.grass);
				}
			}
		});

		// Place some trees...
		tasks.add((generator, seed) -> {
			Random treeRandom = new Random(seed);

			for (int i = 5; i < generator.getWidth() - 5; i += 10) {
				placeTree(i, generator.getSurfaceLevel(i), treeRandom);
			}
		});

		// Find frames...
		tasks.add((generator, seed) -> {
			for (int i = 0; i < generator.getWidth(); ++i) {
				for (int j = 0; j < generator.getHeight(); ++j) {
					generator.findTileFrame(i, j);
					generator.findWallFrame(i, j);
				}
			}
		});

		// Set the spawnpoint
		tasks.add((generator, seed) -> {
			generator.setSpawnX(generator.getWidth() / 2);
			generator.setSpawnY(Math.max(generator.getSurfaceLevel((int) generator.getSpawnX()),
					generator.getSurfaceLevel((int) generator.getSpawnX() + 1)));
		});
	}

	public World getWorld() {
		return world;
	}

	public long getSeed() {
		return seed;
	}

	public void generate() {
		for (WorldGeneratorTask task : tasks) {
			task.generate(this, getSeed());
		}

		Pixmap worldPixmap = worldToPixmap(world);
		PixmapIO.writePNG(Gdx.files.local("debug/world.png"), worldPixmap);
		worldPixmap.dispose();
	}

	public void placeTree(int x, int y, Random random) {
		// Place stubs
		if (world.getTileType(x - 1, y) == TileType.air &&
				world.getTileType(x - 1, y - 1).isSolid() &&
				random.nextBoolean()) {
			world.setTileType(x - 1, y, TileType.tree);
			world.setAttached(x - 1, y, Direction.EAST, true);
			world.setAttached(x - 1, y, Direction.SOUTH, true);
		}
		if (world.getTileType(x + 1, y) == TileType.air &&
				world.getTileType(x + 1, y - 1).isSolid() &&
				random.nextBoolean()) {
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

		for (int i = 0; i + 1 < treeHeight; ++i) {
			if (--branchCounter == 0) {
				int branchDir = -branchSkip;
				if (branchDir == 0) {
					branchDir = random.nextBoolean() ? 1 : -1;
				}

				world.setTileType(x + branchDir, y + i, TileType.tree);
				world.setAttached(x + branchDir, y + i, Direction.get(-branchDir, 0), true);

				branchCounter = 1 + random.nextInt(3);
				branchSkip = branchCounter == 1 ? branchDir : 0;
			}
		}
	}

	static Pixmap moduleToPixmap(Module module, int width, int height, float xFrequency, float yFrequency) {
		Pixmap result = new Pixmap(width, height, Format.RGBA8888);
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				float value = (float) module.get(i * xFrequency, j * yFrequency);
				result.setColor(value, value, value, 1f);
				result.drawPixel(i, j);
			}
		}
		return result;
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

	public float getSpawnX() {
		return world.getSpawnX();
	}

	public float getSpawnY() {
		return world.getSpawnY();
	}

	public void setSpawnX(float spawnX) {
		world.setSpawnX(spawnX);
	}

	public void setSpawnY(float spawnY) {
		world.setSpawnY(spawnY);
	}

	public void checkBounds(int x, int y) {
		world.checkBounds(x, y);
	}

	public boolean inBounds(int x, int y) {
		return world.inBounds(x, y);
	}

	public TileType getTileType(int x, int y) {
		return world.getTileType(x, y);
	}

	public void setTileType(int x, int y, TileType type) {
		world.setTileType(x, y, type);
	}

	public WallType getWallType(int x, int y) {
		return world.getWallType(x, y);
	}

	public void setWallType(int x, int y, WallType type) {
		world.setWallType(x, y, type);
	}

	public void findTileFrame(int x, int y) {
		world.findTileFrame(x, y);
	}

	public void findTileFrameSquare(int x, int y) {
		world.findTileFrameSquare(x, y);
	}

	public void findWallFrame(int x, int y) {
		world.findWallFrame(x, y);
	}

	public void findWallFrameSquare(int x, int y) {
		world.findWallFrameSquare(x, y);
	}

	public int getWidth() {
		return world.getWidth();
	}

	public int getHeight() {
		return world.getHeight();
	}

	public int getSurfaceLevel(int x) {
		return world.getSurfaceLevel(x);
	}

	public void setSurfaceLevel(int x, int level) {
		world.setSurfaceLevel(x, level);
	}

	public int getLiquid(int x, int y) {
		return world.getLiquid(x, y);
	}

	public void setLiquid(int x, int y, int liquid) {
		world.setLiquid(x, y, liquid);
	}

	public boolean isAttached(int x, int y, Direction direction) {
		return world.isAttached(x, y, direction);
	}

	public void setAttached(int x, int y, Direction direction, boolean attached) {
		world.setAttached(x, y, direction, attached);
	}

	public void checkAttachment(int x, int y, PlayerEntity player) {
		world.checkAttachment(x, y, player);
	}

	public void clearAttachment(int x, int y) {
		world.clearAttachment(x, y);
	}
}
