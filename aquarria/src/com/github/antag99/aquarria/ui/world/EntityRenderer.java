package com.github.antag99.aquarria.ui.world;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.entity.EntityView;

public abstract class EntityRenderer<T extends Entity, V extends EntityView<T>> {
	protected Class<T> entityClass;
	
	public EntityRenderer(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	public Class<T> getEntityClass() {
		return entityClass;
	}
	
	public void queueAssets(AssetManager assetManager) {
	}
	
	public void getAssets(AssetManager assetManager) {
	}
	
	public abstract void renderEntity(Batch batch, V view);
}
