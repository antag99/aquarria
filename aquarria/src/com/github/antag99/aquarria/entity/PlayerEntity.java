/*******************************************************************************
 * Copyright (c) 2014, Anton Gustafsson
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Aquarria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.github.antag99.aquarria.event.BeginUseItemEvent;
import com.github.antag99.aquarria.event.CanUseItemEvent;
import com.github.antag99.aquarria.event.UpdateUseItemEvent;
import com.github.antag99.aquarria.event.UseItemEvent;
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
		hotbar.addItem(new Item(ItemType.hammer));
		hotbar.addItem(new Item(ItemType.dirt, ItemType.dirt.getMaxStack()));
		hotbar.addItem(new Item(ItemType.stone, ItemType.stone.getMaxStack()));
		hotbar.addItem(new Item(ItemType.dirtWall, ItemType.dirtWall.getMaxStack()));
		hotbar.addItem(new Item(ItemType.stoneWall, ItemType.stoneWall.getMaxStack()));
	}

	@Override
	public void update(float delta) {
		boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean jump = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);

		if (moveLeft && !moveRight) {
			setVelocityX(Math.min(getVelocityX(), 4f));
			setVelocityX(Math.max(getVelocityX() - 5f * delta, -12f));
		} else if (moveRight && !moveLeft) {
			setVelocityX(Math.max(getVelocityX(), 4f));
			setVelocityX(Math.min(getVelocityX() + 5f * delta, 12f));
		} else {
			setVelocityX(0f);
		}

		setY(getY() - getHeight() / 50f);
		grounded = inCollision();
		setY(getY() + getHeight() / 50f);

		if (jump && grounded) {
			setVelocityY(20f);
		}

		super.update(delta);

		for (Entity otherEntity : getWorld().getEntities()) {
			if (otherEntity instanceof ItemEntity) {
				if (getBounds().overlaps(otherEntity.getBounds())) {
					ItemEntity itemEntity = (ItemEntity) otherEntity;
					Item item = hotbar.stackItem(itemEntity.getItem());
					if (!item.isEmpty()) {
						item = inventory.stackItem(item);
						if (!item.isEmpty()) {
							item = hotbar.putItem(item);
							if (!item.isEmpty()) {
								item = inventory.putItem(item);
							}
						}
					}
					itemEntity.setItem(item);
					if (item.isEmpty()) {
						itemEntity.setActive(false);
					}
				}
			}
		}

		if (usingItem || repeatUsingItem) {
			if (repeatUsingItem && !usingItem) {
				// Set to true again if the item starts being used
				repeatUsingItem = false;

				if (!usedItem.isEmpty()) {
					CanUseItemEvent canUseItemEvent = new CanUseItemEvent();
					canUseItemEvent.setPlayer(this);
					canUseItemEvent.setItem(usedItem);
					usedItem.getType().getEvents().fire(canUseItemEvent);

					if (!canUseItemEvent.isHandled()) {
						usingItem = true;
						repeatUsingItem = true;

						BeginUseItemEvent beginUseItemEvent = new BeginUseItemEvent();
						beginUseItemEvent.setPlayer(this);
						beginUseItemEvent.setItem(usedItem);
						usedItem.getType().getEvents().fire(beginUseItemEvent);
					}
				} else {
					usingItem = false;
					repeatUsingItem = false;
				}
			}

			if (usingItem) {
				useTime += delta;
				UpdateUseItemEvent updateUseItemEvent = new UpdateUseItemEvent();
				updateUseItemEvent.setPlayer(this);
				updateUseItemEvent.setItem(usedItem);
				updateUseItemEvent.setDelta(delta);
				usedItem.getType().getEvents().fire(updateUseItemEvent);

				if (useTime % usedItem.getType().getUsageTime() < delta) {
					UseItemEvent useItemEvent = new UseItemEvent();
					useItemEvent.setPlayer(this);
					useItemEvent.setItem(usedItem);
					usedItem.getType().getEvents().fire(useItemEvent);

					if (useItemEvent.isHandled() && usedItem.getType().isConsumable()) {
						usedItem.setStack(usedItem.getStack() - 1);
					}

					if (usedItem.isEmpty() || !usedItem.getType().getUsageRepeat() || !repeatUsingItem) {
						usingItem = false;
						repeatUsingItem = false;
						useTime = 0f;
					}
				}
			} else {
				useTime = 0f;
			}
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
		if (worldFocus != null) {
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
		if (worldFocus != null) {
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
