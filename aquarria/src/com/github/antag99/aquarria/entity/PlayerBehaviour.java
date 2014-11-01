package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.github.antag99.aquarria.item.Inventory;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.item.ItemType;

public class PlayerBehaviour extends EntityBehaviour {
	private Inventory hotbar;
	private Inventory inventory;
	
	public PlayerBehaviour(Entity entity) {
		super(entity);
		
		hotbar = new Inventory(10);
		inventory = new Inventory(40);
		hotbar.addItem(new Item(ItemType.dirt, ItemType.dirt.getMaxStack()));
		hotbar.addItem(new Item(ItemType.stone, ItemType.stone.getMaxStack()));
	}
	
	@Override
	public void update(float delta) {
		Entity entity = getEntity();
		
		Vector2 vel = entity.getVelocity();
		
		boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean jump = Gdx.input.isKeyPressed(Input.Keys.SPACE);
		
		if(moveLeft && !moveRight) {
			vel.x = -2f;
		} else if(moveRight && !moveLeft) {
			vel.x = 2f;
		} else {
			vel.x = 0f;
		}
		
		// FIXME: This will cause errors when in mid-air and velocity gets 0
		if(jump && vel.y == 0f) {
			vel.y = 5f;
		}
	}
	
	public Inventory getHotbar() {
		return hotbar;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}
