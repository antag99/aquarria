package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

public class EntityView<T extends Entity> {
	private T entity;
	
	public EntityView(T entity) {
		this.entity = entity;
	}
	
	public void update(float delta) {
	}
	
	public void draw(Batch batch) {
		batch.draw(entity.getType().getTexture(), entity.getX(), entity.getY(),
				entity.getWidth(), entity.getHeight());
	}
	
	public T getEntity() {
		return entity;
	}
}
