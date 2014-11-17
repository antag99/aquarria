package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.world.World;

public class HammerItemType extends ItemType {
	public HammerItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public HammerItemType(JsonValue properties) {
		super(properties);
	}

	@Override
	public boolean useItem(PlayerEntity player, Item item) {
		Vector2 worldFocus = player.getWorldFocus();
		
		World world = player.getWorld();
		
		int tileX = MathUtils.floor(worldFocus.x);
		int tileY = MathUtils.floor(worldFocus.y);
		
		WallType type = world.getWallType(tileX, tileY);
		
		if(type != WallType.air) {
			world.setWallType(tileX, tileY, WallType.air);
			type.destroyed(player, tileX, tileY);
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean canUseItem(PlayerEntity player, Item item) {
		return true;
	}
}
