/*******************************************************************************
 * Copyright (c) 2014-2015, Anton Gustafsson
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Aquarria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.Assets;
import com.github.antag99.aquarria.SpriteAnimation;
import com.github.antag99.aquarria.entity.PlayerEntity.PlayerState;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.item.ItemAnimation;
import com.github.antag99.aquarria.world.World;

public class PlayerView implements EntityView {
	private PlayerEntity player;
	private float animationCounter;
	private SpriteAnimation animation = null;
	private PlayerState knownState = null;

	public PlayerView(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void update(float delta) {
		if (knownState != player.getState()) {
			System.out.println("resetting state");
			knownState = player.getState();
			animationCounter = 0f;

			switch (knownState) {
			case JUMPING:
				animation = Assets.playerJump;
				break;
			case STANDING:
				animation = Assets.playerStand;
				break;
			case WALKING:
				animation = Assets.playerWalk;
				break;
			}
		}

		if (knownState == PlayerState.WALKING) {
			delta *= Math.abs(player.getVelocityX());
		}

		animationCounter += delta;
	}

	@Override
	public void render(Batch batch) {
		float x = player.getX();
		float y = player.getY();
		float width = player.getWidth();
		float height = player.getHeight();
		float originX = width / 2f;
		float originY = height / 2f;

		/* compute the scale to render the player at - this is the original player sprite scale */
		float scaleX = (Assets.playerGrid.getSpriteWidth() / World.PIXELS_PER_METER) / player.getWidth();
		float scaleY = (Assets.playerGrid.getSpriteHeight() / World.PIXELS_PER_METER) / player.getHeight();

		/* flip the sprite on the x axis based on the players direction */
		scaleX = scaleX * player.getDirectionX();

		/* using an item replaces the body animation */
		SpriteAnimation bodyAnimation = animation;
		float bodyAnimationCounter = animationCounter;

		if (player.isUsingItem()) {
			bodyAnimation = player.getUsedItem().getType().getAnimation().getBodyAnimation(player);
			float animationTime = player.getUsedItem().getType().getAnimationTime();
			bodyAnimationCounter = (player.getUseTime() % animationTime) / animationTime;
		}

		batch.setColor(255 / 255f, 125 / 255f, 90 / 255f, 1f);
		batch.draw(animation.getFrame(Assets.playerHeadSheet, animationCounter), x, y, originX, originY, width, height, scaleX, scaleY, 0f /* rotation */);
		batch.setColor(105 / 255f, 90 / 255f, 75 / 255f, 1f);
		batch.draw(animation.getFrame(Assets.playerEyesSheet, animationCounter), x, y, originX, originY, width, height, scaleX, scaleY, 0f /* rotation */);
		batch.setColor(Color.WHITE);
		batch.draw(animation.getFrame(Assets.playerEyeWhitesSheet, animationCounter), x, y, originX, originY, width, height, scaleX, scaleY, 0f /* rotation */);
		batch.setColor(255 / 255f, 230 / 255f, 175 / 255f, 1f);
		batch.draw(animation.getFrame(Assets.playerPantsSheet, animationCounter), x, y, originX, originY, width, height, scaleX, scaleY, 0f /* rotation */);
		batch.setColor(160 / 255f, 180 / 255f, 215 / 255f, 1f);
		batch.draw(bodyAnimation.getFrame(Assets.playerUndershirtSheet, bodyAnimationCounter), x, y, originX, originY, width, height, scaleX, scaleY, 0f /* rotation */);
		batch.setColor(215 / 255f, 90 / 255f, 55 / 255f, 1f);
		batch.draw(animation.getFrame(Assets.playerHairSheet, animationCounter), x, y, originX, originY, width, height, scaleX, scaleY, 0f /* rotation */);
		batch.setColor(175 / 255f, 165 / 255f, 140 / 255f, 1f);
		batch.draw(bodyAnimation.getFrame(Assets.playerShirtSheet, bodyAnimationCounter), x, y, originX, originY, width, height, scaleX, scaleY, 0f /* rotation */);
		batch.setColor(160 / 255f, 105 / 255f, 60 / 255f, 1f);
		batch.draw(animation.getFrame(Assets.playerShoesSheet, animationCounter), x, y, originX, originY, width, height, scaleX, scaleY, 0f /* rotation */);

		if (player.isUsingItem()) {
			Item item = player.getUsedItem();
			ItemAnimation animation = item.getType().getAnimation();
			TextureRegion itemTexture = item.getType().getTexture();

			float useOffsetX = animation.getHeldItemOffsetX(player);
			float useOffsetY = animation.getHeldItemOffsetY(player);
			float useRotation = animation.getHeldItemRotation(player);

			batch.setColor(Color.WHITE);
			batch.draw(itemTexture, player.getX() + useOffsetX, player.getY() + useOffsetY, 0f, 0f,
					item.getType().getWidth() / World.PIXELS_PER_METER,
					item.getType().getHeight() / World.PIXELS_PER_METER,
					1f, 1f, useRotation);
		}
	}
}
