package com.github.antag99.aquarria.item;

public class Item {
	private ItemType type;
	private int stack;
	
	public Item() {
		this(ItemType.air, 0);
	}
	
	public Item(ItemType type) {
		this(type, 1);
	}
	
	public Item(ItemType type, int stack) {
		this.type = type;
		this.stack = stack;
	}
	
	public ItemType getType() {
		return type;
	}
	
	public void setType(ItemType type) {
		this.type = type;
	}
	
	public int getStack() {
		return stack;
	}
	
	public void setStack(int stack) {
		this.stack = stack;
	}
}
