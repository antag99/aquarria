package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.antag99.aquarria.item.Item;

public class ItemView extends EntityView<ItemEntity> {
	public ItemView(ItemEntity entity) {
		super(entity);
	}

	@Override
	public void draw(Batch batch) {
		ItemEntity entity = getEntity();
		Item item = entity.getItem();
		
		batch.draw(item.getType().getTexture(), entity.getX(),
				entity.getY(), entity.getWidth(), entity.getHeight());
	}
}
