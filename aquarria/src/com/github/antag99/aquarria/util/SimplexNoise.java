package com.github.antag99.aquarria.util;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/** Simplex noise implementation */
public class SimplexNoise extends Noise {
	// Based on http://webstaff.itn.liu.se/~stegu/simplexnoise/SimplexNoise.java
	private static Vector3 grad[] = {
			new Vector3(1, 1, 0), new Vector3(-1, 1, 0), new Vector3(1, -1, 0),
			new Vector3(-1, -1, 0), new Vector3(1, 0, 1), new Vector3(-1, 0, 1),
			new Vector3(1, 0, -1), new Vector3(-1, 0, -1), new Vector3(0, 1, 1),
			new Vector3(0, -1, 1), new Vector3(0, 1, -1), new Vector3(0, -1, -1)
	};

	private int[] permutation;

	public SimplexNoise(long seed) {
		Random random = new Random(seed);
		permutation = new int[512];
		for (int i = 0; i < 256; ++i) {
			permutation[i] = permutation[i + 256] = random.nextInt(256);
		}
	}

	// Skewing and unskewing factors for 3 dimensions
	private static final float F3 = 1.0f / 3.0f;
	private static final float G3 = 1.0f / 6.0f;

	private static float dot(Vector3 g, float x, float y, float z) {
		return g.x * x + g.y * y + g.z * z;
	}

	@Override
	public float get(float xin, float yin, float zin) {
		float n0, n1, n2, n3; // Noise contributions from the four corners
		// Skew the input space to determine which simplex cell we're in
		float s = (xin + yin + zin) * F3; // Very nice and simple skew factor for 3D
		int i = MathUtils.floor(xin + s);
		int j = MathUtils.floor(yin + s);
		int k = MathUtils.floor(zin + s);
		float t = (i + j + k) * G3;
		float X0 = i - t; // Unskew the cell origin back to (x,y,z) space
		float Y0 = j - t;
		float Z0 = k - t;
		float x0 = xin - X0; // The x,y,z distances from the cell origin
		float y0 = yin - Y0;
		float z0 = zin - Z0;
		// For the 3D case, the simplex shape is a slightly irregular tetrahedron.
		// Determine which simplex we are in.
		int i1, j1, k1; // Offsets for second corner of simplex in (i,j,k) coords
		int i2, j2, k2; // Offsets for third corner of simplex in (i,j,k) coords
		if (x0 >= y0) {
			if (y0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} // X Y Z order
			else if (x0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} // X Z Y order
			else {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} // Z X Y order
		} else { // x0<y0
			if (y0 < z0) {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} // Z Y X order
			else if (x0 < z0) {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} // Y Z X order
			else {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} // Y X Z order
		}
		// A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z),
		// a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z),
		// and a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z),
		// where c = 1/6.
		float x1 = x0 - i1 + G3; // Offsets for second corner in (x,y,z) coords
		float y1 = y0 - j1 + G3;
		float z1 = z0 - k1 + G3;
		float x2 = x0 - i2 + 2.0f * G3; // Offsets for third corner in (x,y,z) coords
		float y2 = y0 - j2 + 2.0f * G3;
		float z2 = z0 - k2 + 2.0f * G3;
		float x3 = x0 - 1.0f + 3.0f * G3; // Offsets for last corner in (x,y,z) coords
		float y3 = y0 - 1.0f + 3.0f * G3;
		float z3 = z0 - 1.0f + 3.0f * G3;
		// Work out the hashed gradient indices of the four simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int kk = k & 255;
		int gi0 = permutation[ii + permutation[jj + permutation[kk]]] % 12;
		int gi1 = permutation[ii + i1 + permutation[jj + j1 + permutation[kk + k1]]] % 12;
		int gi2 = permutation[ii + i2 + permutation[jj + j2 + permutation[kk + k2]]] % 12;
		int gi3 = permutation[ii + 1 + permutation[jj + 1 + permutation[kk + 1]]] % 12;
		// Calculate the contribution from the four corners
		float t0 = 0.6f - x0 * x0 - y0 * y0 - z0 * z0;
		if (t0 < 0)
			n0 = 0.0f;
		else {
			t0 *= t0;
			n0 = t0 * t0 * dot(grad[gi0], x0, y0, z0);
		}
		float t1 = 0.6f - x1 * x1 - y1 * y1 - z1 * z1;
		if (t1 < 0)
			n1 = 0.0f;
		else {
			t1 *= t1;
			n1 = t1 * t1 * dot(grad[gi1], x1, y1, z1);
		}
		float t2 = 0.6f - x2 * x2 - y2 * y2 - z2 * z2;
		if (t2 < 0)
			n2 = 0.0f;
		else {
			t2 *= t2;
			n2 = t2 * t2 * dot(grad[gi2], x2, y2, z2);
		}
		float t3 = 0.6f - x3 * x3 - y3 * y3 - z3 * z3;
		if (t3 < 0)
			n3 = 0.0f;
		else {
			t3 *= t3;
			n3 = t3 * t3 * dot(grad[gi3], x3, y3, z3);
		}
		// Add contributions from each corner to get the final noise value.
		// The result is scaled to stay just inside [-1,1]
		float result = 32.0f * (n0 + n1 + n2 + n3);
		
		// Convert from the [-1, 1] range to [0, 1]
		return (result + 1f) / 2f;
	}
}
