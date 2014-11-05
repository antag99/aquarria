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
		
		animationCounter += delta;
		
		if(player.getVelocityX() != 0f) {
			if (animationCounter > 0.2f) {
				legFrame++;
				
				if(legFrame > 19) {
					legFrame = 7;
				} else if(legFrame < 7) {
					legFrame = 19;
				}
				
				bodyFrame = legFrame;
				
				animationCounter -= 0.2f;
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
