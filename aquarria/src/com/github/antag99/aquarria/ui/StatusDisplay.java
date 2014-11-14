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
		
		for(int y = countY - 1; y >= 0; --y) {
			for(int x = 0; x < countX; ++x) {
				float itemX = x * (itemWidth + spacing);
				float itemY = y * (itemHeight + spacing);
				
				int index = (countY - y - 1) * countX + x;
				float currentValue = index * (1f / count);
				
				if(currentValue < value) {
					float percent = 1f;
					if(value - currentValue < 1f / count) {
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
