package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.antag99.aquarria.item.Inventory;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.item.ItemType;

public class PlayerEntity extends Entity {
	private Inventory hotbar;
	private Inventory inventory;

	public PlayerEntity() {
		super(EntityType.player);
		
		hotbar = new Inventory(10);
		inventory = new Inventory(40);
		hotbar.addItem(new Item(ItemType.dirt, ItemType.dirt.getMaxStack()));
		hotbar.addItem(new Item(ItemType.stone, ItemType.stone.getMaxStack()));
	}
	
	@Override
	public void update(float delta) {
		boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean jump = Gdx.input.isKeyPressed(Input.Keys.SPACE);
		
		if(moveLeft && !moveRight) {
			setVelocityX(-4f);
		} else if(moveRight && !moveLeft) {
			setVelocityX(4f);
		} else {
			setVelocityX(0f);
		}
		
		// FIXME: This will cause errors when in mid-air and velocity gets 0
		if(jump && getVelocityY() == 0f) {
			setVelocityY(5f);
		}
		
		for(Entity otherEntity : getWorld().getEntities()) {
			if(otherEntity instanceof ItemEntity) {
				if(getBounds().overlaps(otherEntity.getBounds())) {
					ItemEntity itemEntity = (ItemEntity) otherEntity;
					Item item = inventory.addItem(itemEntity.getItem());
					itemEntity.setItem(item);
					if(item.isEmpty()) {
						itemEntity.setActive(false);
					}
				}
			}
		}
		
		super.update(delta);
	}
	
	public Inventory getHotbar() {
		return hotbar;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}
