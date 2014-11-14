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
	private boolean grounded;

	private float useTime = 0f;
	private Item usedItem;
	
	private boolean usingItem;
	private boolean repeatUsingItem;
	
	public PlayerEntity() {
		super(EntityType.player);
		
		hotbar = new Inventory(10);
		inventory = new Inventory(40);

		hotbar.addItem(new Item(ItemType.pickaxe));
		hotbar.addItem(new Item(ItemType.dirt, ItemType.dirt.getMaxStack()));
		hotbar.addItem(new Item(ItemType.stone, ItemType.stone.getMaxStack()));
	}
	
	@Override
	public void update(float delta) {
		boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean jump = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
		
		if(moveLeft && !moveRight) {
			setVelocityX(Math.min(getVelocityX(), 4f));
			setVelocityX(Math.max(getVelocityX() - 5f * delta, -12f));
		} else if(moveRight && !moveLeft) {
			setVelocityX(Math.max(getVelocityX(), 4f));
			setVelocityX(Math.min(getVelocityX() + 5f * delta, 12f));
		} else {
			setVelocityX(0f);
		}
		
		setY(getY() - getHeight() / 50f);
		grounded = inCollision();
		setY(getY() + getHeight() / 50f);
		
		if(jump && grounded) {
			setVelocityY(20f);
		}
		
		super.update(delta);
		
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
		
		if(repeatUsingItem && !usingItem) {
			if(!usedItem.isEmpty() && usedItem.getType().canUseItem(this, usedItem)) {
				usingItem = true;
				usedItem.getType().beginUseItem(this, usedItem);
			} else {
				usingItem = false;
				repeatUsingItem = false;
			}
		}
		
		if(usingItem) {
			useTime += delta;
			usedItem.getType().updateUseItem(this, usedItem, delta);
			if(useTime % usedItem.getType().getUsageTime() < delta) {
				if(usedItem.getType().useItem(this, usedItem) && usedItem.getType().isConsumable()) {
					usedItem.setStack(usedItem.getStack() - 1);
				}
				
				if(usedItem.isEmpty() || !usedItem.getType().getUsageRepeat() || !repeatUsingItem) {
					usingItem = false;
					repeatUsingItem = false;
					useTime = 0f;
				}
			}
		} else {
			useTime = 0f;
		}
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
	
	public boolean isGrounded() {
		return grounded;
	}

	public void setWorldFocus(Vector2 worldFocus) {
		if(worldFocus != null) {
			setWorldFocus(worldFocus.x, worldFocus.y);
			hasWorldFocus = true;
		} else {
			hasWorldFocus = false;
		}
	}
	
	public boolean isUsingItem() {
		return usingItem;
	}
	
	public void setUsingItem(boolean usingItem) {
		this.usingItem = usingItem;
	}
	
	public boolean getRepeatUsingItem() {
		return repeatUsingItem;
	}
	
	public void setRepeatUsingItem(boolean repeatUsingItem) {
		this.repeatUsingItem = repeatUsingItem;
	}
	
	public float getUseTime() {
		return useTime;
	}
	
	public void setUseTime(float useTime) {
		this.useTime = useTime;
	}
	
	public Item getUsedItem() {
		return usedItem;
	}
	
	public void setUsedItem(Item usedItem) {
		this.usedItem = usedItem;
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
