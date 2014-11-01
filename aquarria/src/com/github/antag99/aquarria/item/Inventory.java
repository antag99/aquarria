package com.github.antag99.aquarria.item;

/** Fixed-size item container */
public class Inventory {
	private Item[] items;
	
	/** Creates a new inventory with the given capacity */
	public Inventory(int size) {
		items = new Item[size];
		for(int i = 0; i < size; ++i)
			items[i] = new Item();
	}
	
	/** Stacks an item to an existing item with the same type in this inventory
	 * @return A new item stack containing the items that could not be added */
	public Item stackItem(Item item) {
		item = item.copy();
		for(int i = 0; i < items.length; ++i) {
			Item slot = items[i];
			if(slot.getType() == item.getType() && !slot.isEmpty() && slot.getStack() != slot.getType().getMaxStack()) {
				item.stackTo(slot);
				if(item.isEmpty()) break;
			}
		}
		return item;
	}
	
	/** Adds the given item to a new slot in this inventory
	 * @return A new item stack containing the items that could not be added */
	public Item putItem(Item item) {
		item = item.copy();
		for(int i = 0; i < items.length; ++i) {
			Item slot = items[i];
			if(slot.isEmpty()) {
				item.stackTo(slot);
				break;
			}
		}
		return item;
	}
	
	/** First attempts to stack the item to an existing item with the same type
	 * in this inventory, then tries to add it to an empty slot
	 * @return A new item stack containing the items that could not be added */
	public Item addItem(Item item) {
		item = stackItem(item);
		if(!item.isEmpty()) {
			item = putItem(item);
		}
		return item;
	}
	
	/** Gets the item at the specified index */
	public Item getItem(int index) {
		return items[index];
	}
	
	/** Sets the item at the specified index to
	 * the value of the given item */
	public void setItem(int index, Item item) {
		items[index].set(item);
	}
}
