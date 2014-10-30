package com.github.antag99.aquarria.entity;

public class EntityBehaviour {
	private Entity entity;
	
	public EntityBehaviour(Entity entity) {
		this.entity = entity;
	}
	
	public void update(float delta) {
	}
	
	public Entity getEntity() {
		return entity;
	}
}
