/*******************************************************************************
 * Copyright (c) 2014-2015, Anton Gustafsson
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
package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntIntMap;
import com.github.antag99.aquarria.Item;
import com.github.antag99.aquarria.Sprite;
import com.github.antag99.aquarria.entity.PlayerEntity;

public class IngameInterface extends Table {
	private Table leftTable;
	private Table rightTable;

	private InventoryDisplay hotbarDisplay;
	private InventoryDisplay inventoryDisplay;
	private int hotbarSelectedIndex = 0;
	private StatusDisplay healthDisplay;
	private StatusDisplay manaDisplay;

	private Label hotbarText;
	private Label healthText;
	private Label manaText;

	private Item swapItem;
	private PlayerEntity player;

	private boolean inventoryOpen;
	private Vector2 tmpVector2 = new Vector2();

	private static IntIntMap keysToIndex = new IntIntMap();

	static {
		keysToIndex.put(Input.Keys.NUM_1, 0);
		keysToIndex.put(Input.Keys.NUM_2, 1);
		keysToIndex.put(Input.Keys.NUM_3, 2);
		keysToIndex.put(Input.Keys.NUM_4, 3);
		keysToIndex.put(Input.Keys.NUM_5, 4);
		keysToIndex.put(Input.Keys.NUM_6, 5);
		keysToIndex.put(Input.Keys.NUM_7, 6);
		keysToIndex.put(Input.Keys.NUM_8, 7);
		keysToIndex.put(Input.Keys.NUM_9, 8);
		keysToIndex.put(Input.Keys.NUM_0, 9);
	}

	public IngameInterface(Skin skin) {
		swapItem = new Item();
		setTouchable(Touchable.childrenOnly);

		left();
		top();
		pad(4f);

		leftTable = new Table();
		leftTable.left().top();
		rightTable = new Table();
		rightTable.right().top();

		add(leftTable).fill().expand();
		add(rightTable).fill().expand();

		hotbarText = new Label("Items", skin, "hotbarText");

		hotbarDisplay = new InventoryDisplay(10, 1, skin, "hotbar");
		hotbarDisplay.setSwapItem(swapItem);
		hotbarDisplay.setSelectedIndex(0);

		inventoryDisplay = new InventoryDisplay(10, 4, skin, "inventory");
		inventoryDisplay.setSwapItem(swapItem);

		leftTable.add(hotbarText).left().expandX();
		leftTable.row();
		leftTable.add(hotbarDisplay).size(50f * 10, 50f).left().expandX();
		leftTable.row();
		leftTable.add(inventoryDisplay).size(50f * 10, 50f * 4).left().expandX();
		leftTable.row();

		healthDisplay = new StatusDisplay(skin, "health");
		healthDisplay.setCountX(10);
		healthDisplay.setCountY(2);
		healthDisplay.setSpacing(2f);

		manaDisplay = new StatusDisplay(skin, "mana");
		manaDisplay.setCountX(1);
		manaDisplay.setCountY(20);
		manaDisplay.setSpacing(2f);

		healthText = new Label("Life", skin, "healthText");
		manaText = new Label("Mana", skin, "manaText");

		rightTable.add(healthText).space(4f);
		rightTable.add(manaText).right().space(4f);
		rightTable.row();
		rightTable.add(healthDisplay).size(22f * 10, 22f * 2).right().top();
		rightTable.add(manaDisplay).size(22f, 24f * 20).center().top();

		addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ESCAPE) {
					setInventoryOpen(!inventoryOpen);

					return true;
				} else if (keysToIndex.containsKey(keycode)) {
					if (hotbarDisplay.getSelectedIndex() != -1) {
						hotbarDisplay.setSelectedIndex(keysToIndex.get(keycode, -1));
					}

					return true;
				}

				return false;
			}

			@Override
			public boolean scrolled(InputEvent event, float x, float y, int amount) {
				int selectedIndex = hotbarDisplay.getSelectedIndex();

				if (selectedIndex != -1) {
					selectedIndex += amount;
					int slotCount = hotbarDisplay.getSlotCount();
					if (selectedIndex < 0)
						selectedIndex += slotCount;
					if (selectedIndex >= slotCount)
						selectedIndex -= slotCount;

					hotbarDisplay.setSelectedIndex(selectedIndex);
				}

				return true;
			}
		});

		inventoryOpen = true;
		setInventoryOpen(false);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		healthText.setText("Life: " + player.getHealth() + "/" + player.getMaxHealth());
		if (hotbarDisplay.getSelectedIndex() == -1) {
			hotbarText.setText("Inventory");
		} else {
			Item heldItem = player.getHotbar().getItem(hotbarDisplay.getSelectedIndex());
			if (heldItem.isEmpty()) {
				hotbarText.setText("Items");
			} else {
				hotbarText.setText(heldItem.getType().getName());
			}
		}

		super.draw(batch, parentAlpha);

		if (!swapItem.isEmpty()) {
			Sprite itemTexture = swapItem.getType().getIcon();
			if (itemTexture != null) {
				Vector2 mousePosition = tmpVector2.set(Gdx.input.getX(), Gdx.input.getY());
				mousePosition = getStage().screenToStageCoordinates(mousePosition);
				itemTexture.draw(batch, mousePosition.x, mousePosition.y, swapItem.getType().getWidth(), swapItem.getType().getHeight());
			}
		}
	}

	private void setInventoryOpen(boolean inventoryOpen) {
		if (this.inventoryOpen != inventoryOpen) {
			this.inventoryOpen = inventoryOpen;
			if (inventoryOpen) {
				hotbarSelectedIndex = hotbarDisplay.getSelectedIndex();
				hotbarDisplay.setSelectedIndex(-1);
				hotbarDisplay.setSwapItem(swapItem);
				inventoryDisplay.setVisible(true);
			} else {
				if (!swapItem.isEmpty()) {
					player.getWorld().dropItem(swapItem.copy(), player.getX(), player.getY());
					swapItem.setStack(0);
				}
				hotbarDisplay.setSelectedIndex(hotbarSelectedIndex);
				hotbarDisplay.setSwapItem(null);
				inventoryDisplay.setVisible(false);
			}
		}
	}

	public void setPlayer(PlayerEntity player) {
		this.player = player;

		if (player != null) {
			hotbarDisplay.setInventory(player.getHotbar());
			inventoryDisplay.setInventory(player.getInventory());
		} else {
			hotbarDisplay.setInventory(null);
			inventoryDisplay.setInventory(null);
		}
	}

	public Item getHeldItem() {
		return swapItem;
	}

	public Item getSelectedItem() {
		int index = hotbarDisplay.getSelectedIndex();

		if (index != -1) {
			return player.getHotbar().getItem(index);
		}

		return null;
	}

	public Item getFocusItem() {
		Item selectedItem = getSelectedItem();
		return selectedItem != null ? selectedItem : getHeldItem();
	}
}
