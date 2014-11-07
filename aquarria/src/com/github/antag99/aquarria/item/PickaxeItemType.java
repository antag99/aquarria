package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.TileType;
import com.github.antag99.aquarria.world.World;

public class PickaxeItemType extends ItemType {
	public PickaxeItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public PickaxeItemType(JsonValue properties) {
		super(properties);
	}

	@Override
	public boolean useItem(PlayerEntity player, Item item) {
		Vector2 worldFocus = player.getWorldFocus();
		
		World world = player.getWorld();
		
		int tileX = MathUtils.floor(worldFocus.x);
		int tileY = MathUtils.floor(worldFocus.y);
		
		TileType type = world.getTileType(tileX, tileY);
		
		if(type != TileType.air) {
			world.setTileType(tileX, tileY, TileType.air);
			type.destroyed(player, tileX, tileY);
		}
		
		return true;
	}
}
