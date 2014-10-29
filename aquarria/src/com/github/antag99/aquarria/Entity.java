package com.github.antag99.aquarria;

import com.badlogic.gdx.math.Vector2;


public class Entity {
	private Vector2 position = new Vector2();
	private Vector2 velocity = new Vector2();
	
	private EntityBehaviour behaviour;
	private EntityView view;
	private EntityType type;
	
	private World world;
	
	private boolean active = true;
	
	public Entity(EntityType type) {
		this.type = type;
		behaviour = type.createBehaviour(this);
		view = type.createView(this);
	}
	
	public void update(float delta) {
		position.x += velocity.x * delta;
		position.y += velocity.y * delta;
		
		behaviour.update(delta);
		view.update(delta);
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public EntityType getType() {
		return type;
	}
	
	public EntityBehaviour getBehaviour() {
		return behaviour;
	}
	
	public EntityView getView() {
		return view;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
