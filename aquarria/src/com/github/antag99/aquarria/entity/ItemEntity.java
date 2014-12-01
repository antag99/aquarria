package com.github.antag99.aquarria.entity;

import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.ui.IngameScreen;

public class ItemEntity extends Entity {
	private Item item;

	public ItemEntity(Item item) {
		super(EntityType.item);

		setItem(item);
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public float getWidth() {
		return item.getType().getWidth() / IngameScreen.PIXELS_PER_METER;
	}

	@Override
	public float getHeight() {
		return item.getType().getHeight() / IngameScreen.PIXELS_PER_METER;
	}
}
