package com.github.antag99.aquarria.entity;

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
				entity.getSize().x, entity.getSize().y);
	}
	
	public Entity getEntity() {
		return entity;
	}
}
