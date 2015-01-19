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

import com.github.antag99.aquarria.tile.TileType;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleCombiner;
import com.sudoplay.joise.module.ModuleCombiner.CombinerType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;
import com.sudoplay.joise.module.ModuleGradient;
import com.sudoplay.joise.module.ModuleSelect;
import com.sudoplay.joise.module.ModuleTranslateDomain;

public class DirtGeneratorTask implements WorldGeneratorTask {
	private TileType target;
	private TileType replacement;

	public DirtGeneratorTask(TileType target, TileType replacement) {
		this.target = target;
		this.replacement = replacement;
	}

	@Override
	public void generate(WorldGenerator generator, long seed) {
		ModuleFractal fractal = new ModuleFractal();
		fractal.setType(FractalType.FBM);
		fractal.setAllSourceBasisTypes(BasisType.GRADIENT);
		fractal.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
		fractal.setFrequency(1f / 20f);
		fractal.setNumOctaves(2);
		fractal.setSeed(seed);

		ModuleGradient gradient = new ModuleGradient();
		gradient.setGradient(0, 0, 0, generator.getHeight() * 0.75);

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
				if (generator.getTileType(i, j) == target && select.get(i, j) == 1.0) {
					generator.setTileType(i, j, replacement);
				}
			}
		}
	}
}
