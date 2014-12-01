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

/** Fixed-size item container */
public class Inventory {
	private Item[] items;

	/** Creates a new inventory with the given capacity */
	public Inventory(int size) {
		items = new Item[size];
		for (int i = 0; i < size; ++i)
			items[i] = new Item();
	}

	/**
	 * Stacks an item to an existing item with the same type in this inventory
	 * 
	 * @return A new item stack containing the items that could not be added
	 */
	public Item stackItem(Item item) {
		item = item.copy();
		for (int i = 0; i < items.length; ++i) {
			Item slot = items[i];
			if (slot.getType() == item.getType() && !slot.isEmpty() && slot.getStack() != slot.getType().getMaxStack()) {
				item.stackTo(slot);
				if (item.isEmpty())
					break;
			}
		}
		return item;
	}

	/**
	 * Adds the given item to a new slot in this inventory
	 * 
	 * @return A new item stack containing the items that could not be added
	 */
	public Item putItem(Item item) {
		item = item.copy();
		for (int i = 0; i < items.length; ++i) {
			Item slot = items[i];
			if (slot.isEmpty()) {
				item.stackTo(slot);
				break;
			}
		}
		return item;
	}

	/**
	 * First attempts to stack the item to an existing item with the same type
	 * in this inventory, then tries to add it to an empty slot
	 * 
	 * @return A new item stack containing the items that could not be added
	 */
	public Item addItem(Item item) {
		item = stackItem(item);
		if (!item.isEmpty()) {
			item = putItem(item);
		}
		return item;
	}

	/** Gets the item at the specified index */
	public Item getItem(int index) {
		return items[index];
	}

	/**
	 * Sets the item at the specified index to
	 * the value of the given item
	 */
	public void setItem(int index, Item item) {
		items[index].set(item);
	}
}
