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
		if (item.type == type && item.stack != item.type.getMaxStack() && stack != type.getMaxStack()) {
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

	@Override
	public String toString() {
		return "Item[type=" + type.getId() + ", stack=" + stack + "]";
	}
}
