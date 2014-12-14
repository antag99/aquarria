package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.utils.JsonValue;

public final class BlockFrameStyleFactory implements FrameStyleFactory {
	@Override
	public FrameStyle create(JsonValue tileConfiguration) {
		return new BlockFrameStyle();
	}
}
