package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.utils.JsonValue;

public final class WallFrameStyleFactory implements FrameStyleFactory {
	@Override
	public FrameStyle create(JsonValue tileConfiguration) {
		return new WallFrameStyle();
	}
}
