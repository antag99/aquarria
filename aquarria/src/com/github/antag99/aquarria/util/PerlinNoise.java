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
