package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.world.World;

public class DropTileType extends TileType {

	private String dropName;
	private ItemType drop;

	public DropTileType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public DropTileType(JsonValue properties) {
		super(properties);

		dropName = properties.getString("drop");
	}

	@Override
	public void destroyed(PlayerEntity player, int x, int y) {
		if (drop == null) {
			drop = ItemType.forName(dropName);
			if (drop == null) {
				throw new RuntimeException("The item " + dropName + " wasn't found");
			}
		}

		World world = player.getWorld();
		world.dropItem(new Item(drop), x, y);
	}
}
