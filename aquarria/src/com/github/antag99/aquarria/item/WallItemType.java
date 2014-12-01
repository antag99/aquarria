package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.world.World;

public class WallItemType extends ItemType {
	private String createdWallName;
	private WallType createdWall = null;

	public WallItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public WallItemType(JsonValue properties) {
		super(properties);

		createdWallName = properties.getString("createdWall");
	}

	@Override
	public boolean useItem(PlayerEntity player, Item item) {
		if (createdWall == null) {
			createdWall = WallType.forName(createdWallName);
		}

		Vector2 worldFocus = player.getWorldFocus();

		int tileX = MathUtils.floor(worldFocus.x);
		int tileY = MathUtils.floor(worldFocus.y);

		World world = player.getWorld();
		if (world.getWallType(tileX, tileY) == WallType.air) {
			world.setWallType(tileX, tileY, createdWall);

			return true;
		}

		return false;
	}

	@Override
	public boolean canUseItem(PlayerEntity player, Item item) {
		return true;
	}
}
