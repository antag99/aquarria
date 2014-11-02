package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.IntIntMap;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.item.Item;

public class IngameInterface extends Table {
	private InventoryDisplay hotbarDisplay;
	private InventoryDisplay inventoryDisplay;
	private int hotbarSelectedIndex = 0;

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
		
		hotbarDisplay = new InventoryDisplay(10, 1, skin, "hotbar");
		inventoryDisplay = new InventoryDisplay(10, 4, skin, "inventory");
		hotbarDisplay.setSwapItem(swapItem);
		inventoryDisplay.setSwapItem(swapItem);
		
		add(hotbarDisplay).width(50f * 10).height(50f);
		row();
		add(inventoryDisplay).width(50f * 10).height(50f * 4).left();
		hotbarDisplay.setSelectedIndex(0);
		
		
		addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if(keycode == Input.Keys.ESCAPE) {
					setInventoryOpen(!inventoryOpen);
					
					return true;
				} else if(keysToIndex.containsKey(keycode)) {
					if(hotbarDisplay.getSelectedIndex() != -1) {
						hotbarDisplay.setSelectedIndex(keysToIndex.get(keycode, -1));
					}
					
					return true;
				}
				
				return false;
			}
			
			@Override
			public boolean scrolled(InputEvent event, float x, float y, int amount) {
				int selectedIndex = hotbarDisplay.getSelectedIndex();
				
				if(selectedIndex != -1) {
					selectedIndex += amount;
					int slotCount = hotbarDisplay.getSlotCount();
					if(selectedIndex < 0) selectedIndex += slotCount;
					if(selectedIndex >= slotCount) selectedIndex -= slotCount;
					
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
		super.draw(batch, parentAlpha);
		
		if(!swapItem.isEmpty()) {
			TextureRegion itemTexture = swapItem.getType().getTexture();
			if(itemTexture != null) {
				Vector2 mousePosition = tmpVector2.set(Gdx.input.getX(), Gdx.input.getY());
				mousePosition = getStage().screenToStageCoordinates(mousePosition);
				batch.draw(itemTexture, mousePosition.x, mousePosition.y, swapItem.getType().getWidth(), swapItem.getType().getHeight());
			}
		}
	}
	
	private void setInventoryOpen(boolean inventoryOpen) {
		if(this.inventoryOpen != inventoryOpen) {
			this.inventoryOpen = inventoryOpen;
			if(inventoryOpen) {
				hotbarSelectedIndex = hotbarDisplay.getSelectedIndex();
				hotbarDisplay.setSelectedIndex(-1);
				hotbarDisplay.setSwapItem(swapItem);
				inventoryDisplay.setVisible(true);
			} else {
				if(!swapItem.isEmpty()) {
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
		
		if(player != null) {
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
		
		if(index != -1) {
			return player.getHotbar().getItem(index);
		}
		
		return null;
	}
	
	public Item getFocusItem() {
		Item selectedItem = getSelectedItem();
		return selectedItem != null ? selectedItem : getHeldItem();
	}
}
