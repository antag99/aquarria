package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.entity.PlayerEntity.ItemUseState;

public abstract class UsableItemType extends ItemType {
	private float usageDelay;
	private boolean repeated;
	
	public UsableItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public UsableItemType(JsonValue properties) {
		super(properties);
		
		usageDelay = properties.getFloat("usageDelay");
		repeated = properties.getBoolean("repeated", false);
	}
	
	@Override
	public boolean canUseItem(PlayerEntity player, Item item) {
		return true;
	}
	
	@Override
	public void beginUseItem(PlayerEntity player, Item item) {
	}
	
	@Override
	public void updateUseItem(PlayerEntity player, Item item, float delta) {
		if(player.getUseTime() % usageDelay < delta) {
			useItem(player, item, (int) (player.getUseTime() / usageDelay));
			if(item.isEmpty() || !repeated || player.getUseState() == ItemUseState.RELASED) {
				player.setUseState(ItemUseState.NONE);
			}
		}
	}
	
	protected abstract void useItem(PlayerEntity player, Item item, int use);
}
