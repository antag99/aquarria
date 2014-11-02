package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.github.antag99.aquarria.world.World;


public class Entity {
	private float x, y;
	private float velocityX, velocityY;
	
	private EntityView<?> view;
	private EntityType type;
	
	private World world;
	
	private boolean active = true;
	
	private Rectangle tmpRectangle = new Rectangle();
	
	public Entity(EntityType type) {
		this.type = type;
		view = createView();
	}
	
	public void update(float delta) {
		// Apply gravity
		velocityY = velocityY - (4f * type.getWeight()) * delta;
		if(velocityY < -8f * type.getWeight() && type.getWeight() != 0f) {
			velocityY = -8f * type.getWeight();
		}
		
		// It works...
		float moveX = velocityX * delta;
		x += moveX;
		if(type.isSolid() && inCollision()) {
			x -= moveX;
			velocityX = 0f;
		}
		
		float moveY = velocityY * delta;
		y += moveY;
		if(type.isSolid() && inCollision()) {
			y -= moveY;
			velocityY = 0f;
		}
		
		view.update(delta);
	}
	
	private Rectangle tmpBounds = new Rectangle();
	private Rectangle tmpBounds2 = new Rectangle();
	
	public boolean inCollision() {
		tmpBounds.set(x, y, getWidth(), getHeight());
		tmpBounds2.set(0f, 0f, 1f, 1f);
		
		int startX = MathUtils.floor(x);
		int startY = MathUtils.floor(y);
		
		int endX = MathUtils.floor(x + getWidth());
		int endY = MathUtils.floor(y + getHeight());
		
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
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getVelocityX() {
		return velocityX;
	}
	
	public float getVelocityY() {
		return velocityY;
	}
	
	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}
	
	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}
	
	public float getWidth() {
		return type.getDefaultWidth();
	}
	
	public float getHeight() {
		return type.getDefaultHeight();
	}

	public Rectangle getBounds() {
		return tmpRectangle.set(x, y, getWidth(), getHeight());
	}
	
	public EntityType getType() {
		return type;
	}
	
	public EntityView<?> getView() {
		return view;
	}
	
	protected EntityView<?> createView() {
		return new EntityView<Entity>(this);
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
