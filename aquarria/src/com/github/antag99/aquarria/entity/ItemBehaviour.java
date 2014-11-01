package com.github.antag99.aquarria.entity;

import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.ui.IngameScreen;

public class ItemBehaviour extends EntityBehaviour {
	private Item item;
	
	public ItemBehaviour(Entity entity) {
		super(entity);
		
		item = new Item();
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
		getEntity().getSize().set(item.getType().getWidth() / IngameScreen.PIXELS_PER_METER,
				item.getType().getHeight() / IngameScreen.PIXELS_PER_METER);
	}
	
	@Override
	public void update(float delta) {
	}

}
