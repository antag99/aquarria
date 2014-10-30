package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class PlayerBehaviour extends EntityBehaviour {
	public PlayerBehaviour(Entity entity) {
		super(entity);
	}
	
	@Override
	public void update(float delta) {
		Entity entity = getEntity();
		
		Vector2 vel = entity.getVelocity();
		
		boolean moveLeft = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean moveRight = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean jump = Gdx.input.isKeyPressed(Input.Keys.SPACE);
		
		if(moveLeft && !moveRight) {
			vel.x = -2f;
		} else if(moveRight && !moveLeft) {
			vel.x = 2f;
		} else {
			vel.x = 0f;
		}
		
		// FIXME: This will cause errors when in mid-air and velocity gets 0
		if(jump && vel.y == 0f) {
			vel.y = 5f;
		}
	}
}
