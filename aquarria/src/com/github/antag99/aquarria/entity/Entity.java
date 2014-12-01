package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.github.antag99.aquarria.world.World;

public class Entity {
	private float x, y;
	private float velocityX, velocityY;
	private int direction = 1;

	private EntityView<?> view;
	private EntityType type;

	private World world;

	private boolean active = true;

	private Rectangle tmpRectangle = new Rectangle();

	private int health;

	public Entity(EntityType type) {
		this.type = type;
		view = createView();
		health = type.getMaxHealth();
	}

	public void update(float delta) {
		boolean inWater = inWater();

		// Apply gravity
		velocityY = velocityY - (35f * type.getWeight()) * delta;

		// It works...
		float moveX = velocityX * delta * (inWater ? 0.5f : 1f);
		x += moveX;
		if (type.isSolid() && inCollision()) {
			x -= moveX;
			while (!inCollision())
				x += Math.signum(moveX) * 0.1f;
			x -= Math.signum(moveX) * 0.1f;
			velocityX = 0f;
		}

		float moveY = velocityY * delta * (inWater ? 0.5f : 1f);
		y += moveY;
		if (type.isSolid() && inCollision()) {
			if (velocityY < -50f) {
				int fallDamage = (int) (-velocityY * 2) - 100;
				setHealth(Math.max(getHealth() - fallDamage, 0));
			}

			y -= moveY;
			while (!inCollision())
				y += Math.signum(moveY) * 0.1f;
			y -= Math.signum(moveY) * 0.1f;
			velocityY = 0f;
		}

		if (velocityX > 0f) {
			direction = 1;
		} else if (velocityX < 0f) {
			direction = -1;
		}

		view.update(delta);
	}

	private Rectangle tmpBounds = new Rectangle();
	private Rectangle tmpBounds2 = new Rectangle();

	public boolean inCollision() {
		if (x < 0f || y < 0f || x + getWidth() > world.getWidth() ||
				y + getHeight() > world.getHeight()) {
			return true;
		}

		tmpBounds.set(x, y, getWidth(), getHeight());
		tmpBounds2.set(0f, 0f, 1f, 1f);

		int startX = MathUtils.floor(x);
		int startY = MathUtils.floor(y);

		int endX = MathUtils.ceil(x + getWidth());
		int endY = MathUtils.ceil(y + getHeight());

		for (int i = startX; i < endX; ++i) {
			tmpBounds2.x = i;
			for (int j = startY; j < endY; ++j) {
				tmpBounds2.y = j;

				if (world.getTileType(i, j).isSolid() && tmpBounds.overlaps(tmpBounds2)) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean inWater() {
		int startX = MathUtils.floor(x);
		int startY = MathUtils.floor(y);

		int endX = MathUtils.ceil(x + getWidth());
		int endY = MathUtils.ceil(y + getHeight());

		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				if (world.getLiquidManager().getLiquid(i, j) >= 64) {
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

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
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

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
