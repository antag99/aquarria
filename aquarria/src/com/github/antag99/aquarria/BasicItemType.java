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

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.entity.PlayerEntity;

/**
 * Implements basic item types, loaded from json files.
 */
public class BasicItemType extends BasicType
		implements ItemType, Json.Serializable {
	private float width;
	private float height;

	private int maxStack;

	private Sprite icon;

	public BasicItemType() {
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		super.read(json, jsonData);

		width = jsonData.getFloat("width");
		height = jsonData.getFloat("height");

		maxStack = jsonData.getInt("maxStack", 99);
		icon = Assets.getSprite(jsonData.getString("icon", "null.png"));
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	/**
	 * Sets the physical width of this item type
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Sets the physical height of this item type
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public int getMaxStack() {
		return maxStack;
	}

	/**
	 * Sets the maximum amount of items of this type that can be stored in one slot.
	 */
	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}

	@Override
	public Sprite getIcon() {
		return icon;
	}

	/**
	 * Sets the texture of the item type.
	 */
	public void setIcon(Sprite icon) {
		this.icon = icon;
	}

	@Override
	public float getUsageDelay() {
		return 0f;
	}

	@Override
	public boolean getUsageRepeat() {
		return false;
	}

	@Override
	public ItemAnimation getUsageAnimation() {
		return null;
	}

	@Override
	public boolean isConsumable() {
		return false;
	}

	@Override
	public boolean canUse(PlayerEntity player, Item stack) {
		return false;
	}

	@Override
	public boolean usageEffect(PlayerEntity player, Item stack) {
		return false;
	}
}
