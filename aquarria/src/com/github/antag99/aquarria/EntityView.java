package com.github.antag99.aquarria;

import com.badlogic.gdx.graphics.g2d.Batch;

public class EntityView {
	private Entity entity;
	
	public EntityView(Entity entity) {
		this.entity = entity;
	}
	
	public void update(float delta) {
	}
	
	public void draw(Batch batch) {
		batch.draw(entity.getType().getTexture(), entity.getPosition().x, entity.getPosition().y,
				entity.getType().getWidth(), entity.getType().getHeight());
	}
	
	public Entity getEntity() {
		return entity;
	}
}
