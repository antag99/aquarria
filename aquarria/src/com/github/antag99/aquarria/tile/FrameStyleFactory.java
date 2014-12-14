package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.GameRegistry;

/**
 * Factory used to produce {@link FrameStyle} instances based on a
 * JSON configuration. Registered in {@link GameRegistry} to be referenced
 * from JSON files.
 */
public interface FrameStyleFactory {

	/**
	 * Creates a {@link FrameStyle} for the given JSON configuration.
	 */
	FrameStyle create(JsonValue tileConfiguration);
}
