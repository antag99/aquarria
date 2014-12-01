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
package com.github.antag99.aquarria.util;

import java.util.Random;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

import static com.badlogic.gdx.math.MathUtils.lerp;

/** Perlin noise implementation */
public class PerlinNoise extends Noise {
	// Based on http://mrl.nyu.edu/~perlin/noise/
	private int[] permutation;
	private Interpolation fade = Interpolation.fade;

	public PerlinNoise(long seed) {
		Random random = new Random(seed);
		permutation = new int[512];
		for (int i = 0; i < 256; ++i) {
			permutation[i] = permutation[i + 256] = random.nextInt(256);
		}
	}

	@Override
	public float get(float x, float y, float z) {
		// Find unit cube that contains point
		int unitX = MathUtils.floor(x) & 255;
		int unitY = MathUtils.floor(y) & 255;
		int unitZ = MathUtils.floor(z) & 255;

		// Find relative x, y, z of point in cube
		float relativeX = x - MathUtils.floor(x);
		float relativeY = y - MathUtils.floor(y);
		float relativeZ = z - MathUtils.floor(z);

		// Compute fade curves for each of x, y, z
		float fadeCurveX = fade.apply(relativeX);
		float fadeCurveY = fade.apply(relativeY);
		float fadeCurveZ = fade.apply(relativeZ);

		// Hash coordinates of the 8 cube corners
		int hashA = permutation[unitX] + unitY;
		int hashAA = permutation[hashA] + unitZ;
		int hashAB = permutation[hashA + 1] + unitZ;
		int hashB = permutation[unitX + 1] + unitY;
		int hashBA = permutation[hashB] + unitZ;
		int hashBB = permutation[hashB + 1] + unitZ;

		// And add blended results form 8 corners of cube
		float result = lerp(lerp(lerp(grad(permutation[hashAA], relativeX, relativeY, relativeZ),
				grad(permutation[hashBA], relativeX - 1, relativeY, relativeZ), fadeCurveX),
				lerp(grad(permutation[hashAB], relativeX, relativeY - 1, relativeZ),
						grad(permutation[hashBB], relativeX - 1, relativeY - 1, relativeZ), fadeCurveX), fadeCurveY),
				lerp(lerp(grad(permutation[hashAA + 1], relativeX, relativeY, relativeZ - 1),
						grad(permutation[hashBA + 1], relativeX - 1, relativeY, relativeZ - 1), fadeCurveX),
						lerp(grad(permutation[hashAB + 1], relativeX, relativeY - 1, relativeZ - 1),
								grad(permutation[hashBB + 1], relativeX - 1, relativeY - 1, relativeZ - 1), fadeCurveX), fadeCurveY), fadeCurveZ);

		// Convert from the [-1, 1] range to [0, 1]
		return (result + 1f) / 2f;
	}

	private static float grad(int hash, float x, float y, float z) {
		// Convert to lo 4 bits of hash code
		int h = hash & 15;
		float u = h < 8 ? x : y, v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}
}
