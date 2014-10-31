package com.github.antag99.aquarria.entity;

import com.github.antag99.aquarria.item.Item;

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
		getEntity().getSize().set(item.getType().getWidth(),
				item.getType().getHeight());
	}
	
	@Override
	public void update(float delta) {
	}

}
