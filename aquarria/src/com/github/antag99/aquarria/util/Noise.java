package com.github.antag99.aquarria.util;

/**
 * Base class for noise implementations.
 * Results are in the range [0, 1].
 */
public abstract class Noise {
	public float get(float x) {
		return get(x, 0f, 0f);
	}

	public float get(float x, float y) {
		return get(x, y, 0f);
	}

	public abstract float get(float x, float y, float z);
}
