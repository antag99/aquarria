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
	
	public Item copy() {
		return new Item(type, stack);
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
	
	public boolean isEmpty() {
		return stack == 0;
	}
	
	public void set(Item item) {
		type = item.type;
		stack = item.stack;
	}
	
	public void stackTo(Item item) {
		// If the type is the same and none of the stacks are full
		if(item.type == type && item.stack != item.type.getMaxStack() && stack != type.getMaxStack()) {
			// Stack the current item onto the other item
			int free = item.type.getMaxStack() - item.stack;
			int add = Math.min(stack, free);
			item.stack += add;
			stack -= add;
		} else { // Otherwise, simply swap the stacks
			ItemType tmpType = item.type;
			int tmpStack = item.stack;
			item.type = type;
			item.stack = stack;
			type = tmpType;
			stack = tmpStack;
		}
	}
}
