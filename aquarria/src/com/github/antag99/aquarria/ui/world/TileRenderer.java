package com.github.antag99.aquarria.ui.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.github.antag99.aquarria.AbstractType;
import com.github.antag99.aquarria.world.World;

public abstract class TileRenderer extends AbstractType {
	public static Array<TileRenderer> getTileRenderers() {
		return AbstractType.getTypes(TileRenderer.class);
	}

	public static TileRenderer forName(String internalName) {
		return AbstractType.forName(TileRenderer.class, internalName);
	}

	public static final TileRenderer normal = new DefaultTileRenderer("normal");
	public static final TileRenderer tree = new TreeTileRenderer("tree");

	public TileRenderer(String internalName) {
		super(internalName);
	}

	public abstract void drawTile(Batch batch, World world, int x, int y);

	@Override
	protected Class<? extends AbstractType> getTypeClass() {
		return TileRenderer.class;
	}
}
