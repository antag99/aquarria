package com.github.antag99.aquarria.entity;

import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.ui.IngameScreen;


public class PlayerView extends EntityView<PlayerEntity> {
	private float animationCounter;
	
	private int bodyFrame;
	private int legFrame;
	
	private float useRotation;
	private float useOffsetX;
	private float useOffsetY;
	
	public PlayerView(PlayerEntity entity) {
		super(entity);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		PlayerEntity player = getEntity();
		
		animationCounter += Math.abs(player.getVelocityX()) * delta;
		
		boolean advance = animationCounter > 0.4f;
		if(advance) animationCounter -= 0.4f;
		
		if(!player.isGrounded()) {
			legFrame = 5;
			bodyFrame = 5;
		} else if(player.getVelocityX() != 0f) {
			if(legFrame < 7) {
				legFrame = 7;
			}
			
			if(advance) {
				legFrame++;
				
				if(legFrame > 19) {
					legFrame = 7;
				}
				
				bodyFrame = legFrame;
			}
		} else {
			legFrame = 0;
			bodyFrame = 0;
		}
		
		if(player.getUseTime() != 0f) {
			float useTime = player.getUseTime();
			while(useTime > 0.2f) useTime -= 0.2f;
			
			bodyFrame = (int) (useTime * 5f * 4) + 1;
			useRotation = useTime * 5f * 180f;

			if(useTime < 0.2f * 1f / 4f) {
				useOffsetX = player.getWidth() - 0.1f;
				useOffsetY = -(1f - 24f / 56f) + player.getHeight();
			} else if(useTime < 0.2f * 2f / 4f) {
				useOffsetX = 0.2f;
				useOffsetY = player.getHeight() / 2f + 0.5f;
			} else if(useTime < 0.2f * 3f / 4f) {
				useOffsetX = 0.1f;
				useOffsetY = 0.7f;
			} else {
				useOffsetX = 0.2f;
				useOffsetY = 0.3f;
			}
			
			if(player.getDirection() != -1) {
				useRotation = 360f - useRotation + 90f;
				useOffsetX = player.getWidth() - useOffsetX;
			}
		}
	}
	
	public int getBodyFrame() {
		return bodyFrame;
	}
	
	public int getLegFrame() {
		return legFrame;
	}
	
	public float getUseRotation() {
		return useRotation;
	}
	
	public float getUseOffsetX() {
		return useOffsetX;
	}
	
	public float getUseOffsetY() {
		return useOffsetY;
	}
}
