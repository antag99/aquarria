package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.antag99.aquarria.world.World;


public class Entity {
	private Vector2 position = new Vector2();
	private Vector2 velocity = new Vector2();
	private Vector2 size = new Vector2();
	
	private EntityBehaviour behaviour;
	private EntityView view;
	private EntityType type;
	
	private World world;
	
	private boolean active = true;
	
	public Entity(EntityType type) {
		this.type = type;
		behaviour = type.createBehaviour(this);
		view = type.createView(this);
		size.x = type.getWeight();
		size.y = type.getDefaultHeight();
	}
	
	public void update(float delta) {
		behaviour.update(delta);
		
		// Apply gravity
		velocity.y = velocity.y - (4f * type.getWeight()) * delta;
		if(velocity.y < -8f * type.getWeight() && type.getWeight() != 0f) {
			velocity.y = -8f * type.getWeight();
		}
		
		// It works...
		float moveX = velocity.x * delta;
		position.x += moveX;
		if(type.isSolid() && inCollision()) {
			position.x -= moveX;
			velocity.y = 0f;
		}
		
		float moveY = velocity.y * delta;
		position.y += moveY;
		if(type.isSolid() && inCollision()) {
			position.y -= moveY;
			velocity.y = 0f;
		}
		
		view.update(delta);
	}
	
	private Rectangle tmpBounds = new Rectangle();
	private Rectangle tmpBounds2 = new Rectangle();
	
	public boolean inCollision() {
		tmpBounds.set(position.x, position.y, size.x, size.y);
		tmpBounds2.set(0f, 0f, 1f, 1f);
		
		int startX = MathUtils.floor(position.x);
		int startY = MathUtils.floor(position.y);
		
		int endX = MathUtils.floor(position.x + size.x);
		int endY = MathUtils.floor(position.y + size.y);
		
		for(int i = startX; i < endX; ++i) {
			tmpBounds2.x = i;
			for(int j = startY; j < endY; ++j) {
				tmpBounds2.y = j;
				
				if(world.getTileType(i, j).isSolid() && tmpBounds.overlaps(tmpBounds2)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getSize() {
		return size;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}
	
	public EntityType getType() {
		return type;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EntityBehaviour> T getBehaviour() {
		return (T) behaviour;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EntityView> T getView() {
		return (T) view;
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
