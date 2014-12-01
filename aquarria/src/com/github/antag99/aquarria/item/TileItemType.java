package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.world.World;

public class TileItemType extends ItemType {
	private String createdTileName;
	private TileType createdTile = null;

	public TileItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public TileItemType(JsonValue properties) {
		super(properties);

		createdTileName = properties.getString("createdTile");
	}

	@Override
	public boolean useItem(PlayerEntity player, Item item) {
		if (createdTile == null) {
			createdTile = TileType.forName(createdTileName);
		}

		Vector2 worldFocus = player.getWorldFocus();

		int tileX = MathUtils.floor(worldFocus.x);
		int tileY = MathUtils.floor(worldFocus.y);

		World world = player.getWorld();
		if (world.getTileType(tileX, tileY) == TileType.air) {
			world.setTileType(tileX, tileY, createdTile);

			return true;
		}

		return false;
	}

	@Override
	public boolean canUseItem(PlayerEntity player, Item item) {
		return true;
	}
}
