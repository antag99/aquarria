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
package com.github.antag99.aquarria;

import com.github.antag99.aquarria.entity.PlayerEntity;

public interface ItemType extends Type {

	/**
	 * Gets the physical width of this item type
	 */
	public float getWidth();

	/**
	 * Gets the physical height of this item type
	 */
	public float getHeight();

	/**
	 * Gets the maximum amount of items of this type that can be stored in one
	 * slot.
	 */
	public int getMaxStack();

	/**
	 * Gets the texture of the item type.
	 */
	public Sprite getIcon();

	/**
	 * Gets the interval in which this item may be used.
	 */
	public float getUsageDelay();

	/**
	 * Whether this item can be repeatedly used
	 */
	public boolean getUsageRepeat();

	/**
	 * Gets the animation for this item
	 */
	public ItemAnimation getUsageAnimation();

	/**
	 * Gets whether this item should be consumed when used. This reduces the item
	 * stack by 1 per usage until the stack is depleted. (Of course, this <em>could</em> be
	 * implemented in {@link #usageEffect(PlayerEntity, Item)}, but it's nice
	 * to have a standard way to do it.)
	 */
	public boolean isConsumable();

	/**
	 * Called to check if this item can be used by the given player.
	 * 
	 * @param player The player that used the item
	 * @param stack The item stack
	 * @return Whether this item can be used by the given player.
	 */
	public boolean canUse(PlayerEntity player, Item stack);

	/**
	 * Called when this item is used by a player.
	 * 
	 * @param player The player that used the item
	 * @param stack The item stack
	 * @return Whether the item was successfully used
	 */
	public boolean usageEffect(PlayerEntity player, Item stack);
}
