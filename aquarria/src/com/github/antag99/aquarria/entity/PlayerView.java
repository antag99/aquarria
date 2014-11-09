package com.github.antag99.aquarria.entity;

import com.github.antag99.aquarria.entity.PlayerEntity.ItemUseState;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.item.ItemUsageStyle;



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
		
		if(player.getUseState() == ItemUseState.ACTIVE || player.getUseState() == ItemUseState.RELASED) {
			ItemType usedItemType = player.getUsedItem().getType();
			ItemUsageStyle style = usedItemType.getUsageStyle();
			float usageProgress = (player.getUseTime() % usedItemType.getUsageAnimationTime()) / usedItemType.getUsageAnimationTime();
			
			useOffsetX = style.getUsedItemOffsetX(this, usageProgress);
			useOffsetY = style.getUsedItemOffsetY(this, usageProgress);
			useRotation = style.getUsedItemRotation(this, usageProgress);
			bodyFrame = style.getPlayerBodyFrame(this, usageProgress);
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
