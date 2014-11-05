package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.github.antag99.aquarria.item.Inventory;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.item.ItemType;

public class PlayerEntity extends Entity {
	private Inventory hotbar;
	private Inventory inventory;
	private Vector2 worldFocus = new Vector2();
	private boolean hasWorldFocus = false;

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
					Item item = hotbar.stackItem(itemEntity.getItem());
					if(!item.isEmpty()) {
						item = inventory.stackItem(item);
						if(!item.isEmpty()) {
							item = hotbar.putItem(item);
							if(!item.isEmpty()) {
								item = inventory.putItem(item);
							}
						}
					}
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
	
	public Vector2 getWorldFocus() {
		return hasWorldFocus ? worldFocus : null;
	}

	public void setWorldFocus(Vector2 worldFocus) {
		setWorldFocus(worldFocus.x, worldFocus.y);
	}
	
	public void setWorldFocus(float x, float y) {
		if(worldFocus != null) {
			this.worldFocus.set(x, y);
			hasWorldFocus = true;
		} else {
			hasWorldFocus = false;
		}
	}
	
	@Override
	protected EntityView<?> createView() {
		return new PlayerView(this);
	}
}
