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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.Type;

public final class ItemType extends Type {
	// Static fields that are automatically set by GameRegistry via reflection.
	public static ItemType air;
	public static ItemType dirt;
	public static ItemType stone;
	public static ItemType pickaxe;
	public static ItemType hammer;
	public static ItemType dirtWall;
	public static ItemType stoneWall;

	// Item properties
	private int maxStack;
	private float width;
	private float height;
	private float usageTime;
	private float usageAnimationTime;
	private boolean usageRepeat;
	private boolean consumable;
	private ItemUsageStyle usageStyle;
	private TextureRegion texture;

	public ItemType() {
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	public float getUsageTime() {
		return usageTime;
	}

	public void setUsageTime(float usageTime) {
		this.usageTime = usageTime;
	}

	public float getUsageAnimationTime() {
		return usageAnimationTime;
	}

	public void setUsageAnimationTime(float usageAnimationTime) {
		this.usageAnimationTime = usageAnimationTime;
	}

	public boolean getUsageRepeat() {
		return usageRepeat;
	}

	public void setUsageRepeat(boolean usageRepeat) {
		this.usageRepeat = usageRepeat;
	}

	public boolean isConsumable() {
		return consumable;
	}

	public void setConsumable(boolean consumable) {
		this.consumable = consumable;
	}

	public ItemUsageStyle getUsageStyle() {
		return usageStyle;
	}

	public void setUsageStyle(ItemUsageStyle usageStyle) {
		this.usageStyle = usageStyle;
	}
}
