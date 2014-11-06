package com.github.antag99.aquarria.entity;

public class PlayerView extends EntityView<PlayerEntity> {
	private float animationCounter;
	
	private int bodyFrame;
	private int legFrame;
	
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
	}
	
	public int getBodyFrame() {
		return bodyFrame;
	}
	
	public int getLegFrame() {
		return legFrame;
	}
}
