package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.entity.PlayerBehaviour;
import com.github.antag99.aquarria.item.Item;

public class IngameInterface extends Table {
	private InventoryDisplay hotbarDisplay;
	private InventoryDisplay inventoryDisplay;

	private Item swapItem;
	private Entity player;

	public IngameInterface(Skin skin) {
		swapItem = new Item();
		
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
	}

	public void setPlayer(Entity player) {
		this.player = player;
		
		if(player != null) {
			PlayerBehaviour behaviour = player.getBehaviour();
			hotbarDisplay.setInventory(behaviour.getHotbar());
			inventoryDisplay.setInventory(behaviour.getInventory());
		} else {
			hotbarDisplay.setInventory(null);
			inventoryDisplay.setInventory(null);
		}
	}
}
