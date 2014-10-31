package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.antag99.aquarria.item.Item;

public class ItemView extends EntityView {
	public ItemView(Entity entity) {
		super(entity);
	}

	@Override
	public void draw(Batch batch) {
		Entity entity = getEntity();
		ItemBehaviour itemBehaviour = entity.getBehaviour();
		Item item = itemBehaviour.getItem();
		
		batch.draw(item.getType().getTexture(), entity.getPosition().x,
				entity.getPosition().y, entity.getSize().x, entity.getSize().y);
	}
}
