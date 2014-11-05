package com.github.antag99.aquarria.entity;


public class EntityView<T extends Entity> {
	private T entity;
	
	public EntityView(T entity) {
		this.entity = entity;
	}
	
	public void update(float delta) {
	}
	
	public T getEntity() {
		return entity;
	}
}
