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
package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class StatusDisplay extends Widget {
	private float value = 1f;

	private int countX;
	private int countY;
	private float spacing;

	private StatusDisplayStyle style;

	public StatusDisplay() {
	}

	public StatusDisplay(Skin skin) {
		this(skin.get(StatusDisplayStyle.class));
	}

	public StatusDisplay(Skin skin, String name) {
		this(skin.get(name, StatusDisplayStyle.class));
	}

	public StatusDisplay(StatusDisplayStyle style) {
		setStyle(style);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		float itemWidth = (getWidth() + spacing) / countX - spacing;
		float itemHeight = (getHeight() + spacing) / countY - spacing;

		int count = countX * countY;

		for (int y = countY - 1; y >= 0; --y) {
			for (int x = 0; x < countX; ++x) {
				float itemX = x * (itemWidth + spacing);
				float itemY = y * (itemHeight + spacing);

				int index = (countY - y - 1) * countX + x;
				float currentValue = index * (1f / count);

				if (currentValue < value) {
					float percent = 1f;
					if (value - currentValue < 1f / count) {
						percent -= value - 1f / count;
					}
					float color = (percent * 0.9f + 0.1f);
					batch.setColor(color, color, color, color * parentAlpha);
				} else {
					batch.setColor(1f, 1f, 1f, 0f);
				}

				batch.draw(style.itemTexture, getX() + itemX, getY() + itemY, itemWidth, itemHeight);
			}
		}
	}

	public StatusDisplayStyle getStyle() {
		return style;
	}

	public void setStyle(StatusDisplayStyle style) {
		this.style = style;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getCountX() {
		return countX;
	}

	public void setCountX(int heartCountX) {
		this.countX = heartCountX;
	}

	public int getCountY() {
		return countY;
	}

	public void setCountY(int heartCountY) {
		this.countY = heartCountY;
	}

	public float getSpacing() {
		return spacing;
	}

	public void setSpacing(float heartSpacing) {
		this.spacing = heartSpacing;
	}

	public static class StatusDisplayStyle {
		public TextureRegion itemTexture;
	}
}
