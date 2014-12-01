package com.github.antag99.aquarria.ui.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.tile.FrameStyle.Frame;
import com.github.antag99.aquarria.tile.FrameStyle.FrameSkin;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.world.World;

public class DefaultTileRenderer extends TileRenderer {
	public DefaultTileRenderer(String internalName) {
		super(internalName);
	}

	@Override
	public void drawTile(Batch batch, World world, int x, int y) {
		TileType type = world.getTileType(x, y);
		FrameSkin skin = type.getSkin();

		if (skin != null) {
			Frame frame = type.getStyle().findFrame(world, x, y);
			TextureRegion texture = skin.getFrameTexture(frame);
			batch.draw(texture, x, y, 1f, 1f);
		}
	}
}
