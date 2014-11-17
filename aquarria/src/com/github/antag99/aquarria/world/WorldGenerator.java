package com.github.antag99.aquarria.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.tile.WallType;
import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleCombiner.CombinerType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;
import com.sudoplay.joise.module.ModuleCombiner;
import com.sudoplay.joise.module.ModuleGradient;
import com.sudoplay.joise.module.ModuleScaleDomain;
import com.sudoplay.joise.module.ModuleScaleOffset;
import com.sudoplay.joise.module.ModuleSelect;
import com.sudoplay.joise.module.ModuleTranslateDomain;

public class WorldGenerator {
	public WorldGenerator() {
	}
	
	public void generate(World world, long seed) {
		// Clear the world
		for(int i = 0; i < world.getWidth(); ++i) {
			for(int j = 0; j < world.getHeight(); ++j) {
				world.setTileType(i, j, TileType.air);
			}
			world.setSurfaceLevel(i, 0);
		}
		
		// Generate base terrain (stone)
		// See http://accidentalnoise.sourceforge.net/minecraftworlds.html
		ModuleGradient groundGradient = new ModuleGradient();
		groundGradient.setGradient(0, 0, 0, 1);
		
		ModuleFractal groundFractal = new ModuleFractal();
		groundFractal.setType(FractalType.FBM);
		groundFractal.setAllSourceBasisTypes(BasisType.GRADIENT);
		groundFractal.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
		groundFractal.setNumOctaves(4);
		groundFractal.setFrequency(2);
		groundFractal.setSeed(seed);
		
//		dumpPixmap(world, groundFractal, "groundFractal");
//		dumpPixmap(world, groundGradient, "groundGradinet");
		
		ModuleScaleOffset groundScale = new ModuleScaleOffset();
		groundScale.setSource(groundFractal);
		groundScale.setScale(0.2);
		groundScale.setOffset(0);
		
		ModuleScaleDomain groundScaleDomain = new ModuleScaleDomain();
		groundScaleDomain.setSource(groundScale);
		groundScaleDomain.setScaleY(0);
		
		ModuleTranslateDomain groundPerturb = new ModuleTranslateDomain();
		groundPerturb.setSource(groundGradient);
		groundPerturb.setAxisYSource(groundScaleDomain);
		
		ModuleSelect groundSelect = new ModuleSelect();
		groundSelect.setControlSource(groundPerturb);
		groundSelect.setLowSource(0);
		groundSelect.setHighSource(1);
		groundSelect.setThreshold(0.5);
		groundSelect.setFalloff(0);

		float xFrequency = 1f / world.getWidth();
		float yFrequency = 1f / world.getHeight();
		
		for(int i = 0; i < world.getWidth(); ++i) {
			int surfaceLevel = world.getSurfaceLevel(i);
			for(int j = 0; j < world.getHeight(); ++j) {
				if(groundSelect.get(xFrequency * i, 1f - yFrequency * j) == 1) {
					world.setTileType(i, j, TileType.stone);
					surfaceLevel = Math.max(surfaceLevel, j);
				}
			}
			world.setSurfaceLevel(i, surfaceLevel + 1);
		}
		

		// Put dirt in the rocks...
		ModuleGradient dirtGradient = new ModuleGradient();
		dirtGradient.setGradient(0, 0, 1, 0);
		
		ModuleFractal dirtFractal = new ModuleFractal();
		dirtFractal.setType(FractalType.FBM);
		dirtFractal.setAllSourceBasisTypes(BasisType.GRADIENT);
		dirtFractal.setAllSourceInterpolationTypes(InterpolationType.LINEAR);
		dirtFractal.setFrequency(50);
		dirtFractal.setNumOctaves(5);
		dirtFractal.setSeed(seed);
		
		ModuleCombiner dirtCombiner = new ModuleCombiner();
		dirtCombiner.setType(CombinerType.AVG);
		dirtCombiner.setSource(0, dirtGradient);
		dirtCombiner.setSource(1, dirtFractal);
		dirtCombiner.setSource(2, .4);
		
		ModuleSelect dirtSelect = new ModuleSelect();
		dirtSelect.setControlSource(dirtCombiner);
		dirtSelect.setLowSource(0);
		dirtSelect.setHighSource(1);
		dirtSelect.setThreshold(0.2);
		dirtSelect.setFalloff(0);
		
//		dumpPixmap(world, dirtFractal, "dirtFractal");
//		dumpPixmap(world, dirtSelect, "dirtSelect");
		
		for(int i = 0; i < world.getWidth(); ++i) {
			for(int j = 0; j < world.getHeight(); ++j) {
				TileType type = world.getTileType(i, j);
				if(type == TileType.dirt || type == TileType.stone &&
						dirtSelect.get(xFrequency * i, 1f - yFrequency * j) == 1) {
					world.setTileType(i, j, type == TileType.stone ? TileType.dirt : TileType.stone);
				}
			}
		}

		// Place dirt walls close to the surface
		for(int i = 0; i < world.getWidth(); ++i) {
			int surfaceLevel = world.getSurfaceLevel(i);
			// Offset by -2; don't place walls behind grass blocks
			for(int j = surfaceLevel - 20; j < surfaceLevel - 2; ++j) {
				world.setWallType(i, j, WallType.dirt);
			}
		}

		// Grassify the surface
		for(int i = 0; i < world.getWidth(); ++i) {
			int surfaceLevel = world.getSurfaceLevel(i);
			if(surfaceLevel > 0 && world.getTileType(i, surfaceLevel - 1) == TileType.dirt) {
				world.setTileType(i, surfaceLevel - 1, TileType.grass);
			}
		}
		
		// Set the spawnpoint
		world.setSpawnX(world.getWidth() / 2);
		world.setSpawnY(Math.max(world.getSurfaceLevel((int) world.getSpawnX()),
				world.getSurfaceLevel((int)world.getSpawnX() + 1)));
		
//		Pixmap worldPixmap = worldToPixmap(world);
//		PixmapIO.writePNG(Gdx.files.local("debug/world.png"), worldPixmap);
//		worldPixmap.dispose();
	}
	
	static void dumpPixmap(World world, Module sourceModule, String name) {
		Pixmap pixmap = new Pixmap(world.getWidth(), world.getHeight(), Format.RGBA8888);
		
		float xFrequency = 1f / world.getWidth();
		float yFrequency = 1f / world.getHeight();
		
		for(int i = 0; i < world.getWidth(); ++i) {
			for(int j = 0; j < world.getHeight(); ++j) {
				float value = MathUtils.clamp((float)sourceModule.get(xFrequency * i, yFrequency * j), 0, 1);
				pixmap.setColor(value, value, value, 1f);
				pixmap.drawPixel(i, j);
			}
		}
		
		PixmapIO.writePNG(Gdx.files.local("debug/" + name + ".png"), pixmap);
	}
	
	static Pixmap worldToPixmap(World world) {
		Pixmap result = new Pixmap(world.getWidth(), world.getHeight(), Format.RGBA8888);
		
		for(int i = 0; i < world.getWidth(); ++i) {
			for(int j = 0; j < world.getHeight(); ++j) {
				Color color;
				TileType type = world.getTileType(i, j);
				
				if(type == TileType.air) {
					color = Color.CLEAR;
				} else if(type == TileType.dirt) {
					color = Color.MAROON;
				} else if(type == TileType.grass) {
					color = Color.GREEN;
				} else if(type == TileType.stone) {
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
