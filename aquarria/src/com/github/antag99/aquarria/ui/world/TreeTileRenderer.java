package com.github.antag99.aquarria.ui.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.tile.FrameStyle.Frame;
import com.github.antag99.aquarria.tile.FrameStyle.FrameSkin;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.world.World;

public class TreeTileRenderer extends TileRenderer {
	public TreeTileRenderer(String internalName) {
		super(internalName);
	}

	@Override
	public void drawTile(Batch batch, World world, int x, int y) {
		TileType type = world.getTileType(x, y);
		FrameSkin skin = type.getSkin();

		if (skin != null) {
			Frame frame = type.getStyle().findFrame(world, x, y);
			TextureRegion texture = skin.getFrameTexture(frame);
			batch.draw(texture, x - 0.125f, y - 0.125f, 1.25f, 1.25f);
		}
	}
}
