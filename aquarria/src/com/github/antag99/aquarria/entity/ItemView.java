package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ItemView implements EntityView {
	private ItemEntity itemEntity;

	public ItemView(ItemEntity itemEntity) {
		this.itemEntity = itemEntity;
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public void render(Batch batch) {
		TextureRegion texture = itemEntity.getItem().getType().getTexture();

		batch.setColor(Color.WHITE);
		batch.draw(texture, itemEntity.getX(), itemEntity.getY(), itemEntity.getWidth(), itemEntity.getHeight());
	}

}
