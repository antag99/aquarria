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
package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.github.antag99.aquarria.item.Inventory;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.item.ItemType;

public class InventoryDisplay extends Table {
	private Inventory inventory;
	private int slotColumns;
	private int slotRows;

	private float slotSpacing = 2f;

	private int selectedIndex = -1;
	private Item swapItem = null;

	private InventoryDisplayStyle style;

	public InventoryDisplay(int slotColumns, int slotRows, Skin skin) {
		this(slotColumns, slotRows, skin.get(InventoryDisplayStyle.class));
	}

	public InventoryDisplay(int slotColumns, int slotRows, Skin skin, String name) {
		this(slotColumns, slotRows, skin.get(name, InventoryDisplayStyle.class));
	}

	public InventoryDisplay(int slotColumns, int slotRows, InventoryDisplayStyle style) {
		this.slotColumns = slotColumns;
		this.slotRows = slotRows;
		this.style = style;
		setTouchable(Touchable.enabled);
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public float getSlotSpacing() {
		return slotSpacing;
	}

	public void setSlotSpacing(float slotSpacing) {
		this.slotSpacing = slotSpacing;
	}

	public int getSlotColumns() {
		return slotColumns;
	}

	public int getSlotRows() {
		return slotRows;
	}

	public void setSlotColumns(int slotColumns) {
		this.slotColumns = slotColumns;
		invalidate();
	}

	public void setSlotRows(int slotRows) {
		this.slotRows = slotRows;
		invalidate();
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * Sets the selected item index. The default -1 indicates no selection,
	 * and will prevent any slot from being selected.
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	/**
	 * Sets the item that will be swapped when a slot is clicked.
	 * null indicates that items shouldn't be swapped when clicked.
	 */
	public void setSwapItem(Item swapItem) {
		this.swapItem = swapItem;
	}

	public Item getSwapItem() {
		return swapItem;
	}

	public int getSlotCount() {
		return slotColumns * slotRows;
	}

	// Hack to prevent layout every frame; invalidate() is called when the table is modified,
	// causing a new layout. Keeping a flag to skip layout after the table is modified prevents this.
	// There is probably a better solution to this, but none that i know of.
	private boolean skipLayout = false;

	@Override
	public void layout() {
		if (!skipLayout) {
			clearChildren();

			int index = 0;
			for (int y = 0; y < slotRows; ++y) {
				for (int x = 0; x < slotColumns; ++x) {
					add(new ItemSlot(index)).width(getWidth() / slotColumns - slotSpacing)
							.height(getHeight() / slotRows - slotSpacing).space(slotSpacing);

					++index;
				}

				row();
			}

			skipLayout = true;

			super.layout();
		} else {
			skipLayout = false;
		}
	}

	public static class InventoryDisplayStyle {
		public Drawable slotBackground;
		public Drawable selectedSlotBackground;
		public BitmapFont stackFont;
	}

	public class ItemSlot extends Widget {
		int index;

		ItemSlot(final int index) {
			this.index = index;

			addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					if (selectedIndex != -1) {
						if (selectedIndex != index) {
							selectedIndex = index;
							return true;
						}
					}

					if (swapItem != null) {
						swapItem.stackTo(inventory.getItem(index));
					}

					return true;
				}
			});
		}

		/** Gets the item of this slot */
		public Item getItem() {
			return inventory.getItem(index);
		}

		/** Sets the item of this slot to the value of the given item */
		public void setItem(Item item) {
			inventory.setItem(index, item);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);

			batch.setColor(1f, 1f, 1f, parentAlpha);

			Drawable slotBackground = index == selectedIndex ? style.selectedSlotBackground : style.slotBackground;
			slotBackground.draw(batch, getX(), getY(), getWidth(), getHeight());

			batch.setColor(1f, 1f, 1f, parentAlpha);

			Item item = getItem();
			if (!item.isEmpty()) {
				ItemType type = item.getType();
				if (type.getTexture() != null) {
					TextureRegion itemTexture = type.getTexture();

					float centerX = getX() + getWidth() / 2f;
					float centerY = getY() + getHeight() / 2f;

					batch.draw(itemTexture, centerX - type.getWidth() / 2f, centerY - type.getHeight() / 2f, type.getWidth(), type.getHeight());

					int stack = item.getStack();
					if (type.getMaxStack() != 1 || stack != 1) {
						String stackText = Integer.toString(stack);

						style.stackFont.setColor(1f, 1f, 1f, parentAlpha);
						style.stackFont.draw(batch, stackText, getX() + 2f, getY() + getHeight() - 2f);
					}
				}
			}
		}
	}
}
