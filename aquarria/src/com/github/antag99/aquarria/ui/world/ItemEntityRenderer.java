package com.github.antag99.aquarria.ui.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.entity.EntityView;
import com.github.antag99.aquarria.entity.ItemEntity;

public class ItemEntityRenderer extends EntityRenderer<ItemEntity, EntityView<ItemEntity>> {
	public ItemEntityRenderer() {
		super(ItemEntity.class);
	}

	@Override
	public void renderEntity(Batch batch, EntityView<ItemEntity> view) {
		ItemEntity item = view.getEntity();
		TextureRegion texture = item.getItem().getType().getTexture();

		batch.setColor(Color.WHITE);
		batch.draw(texture, item.getX(), item.getY(), item.getWidth(), item.getHeight());
	}
}
