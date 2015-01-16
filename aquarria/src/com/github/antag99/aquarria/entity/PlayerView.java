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
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.item.ItemType;
import com.github.antag99.aquarria.item.ItemUsageStyle;
import com.github.antag99.aquarria.world.World;

public class PlayerView implements EntityView {
	private PlayerEntity player;
	private float animationCounter;

	private int bodyFrame;
	private int legFrame;

	private float useRotation;
	private float useOffsetX;
	private float useOffsetY;

	public PlayerView(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void update(float delta) {
		animationCounter += Math.abs(player.getVelocityX()) * delta;

		boolean advance = animationCounter > 0.4f;
		if (advance)
			animationCounter -= 0.4f;

		if (!player.isGrounded()) {
			legFrame = 5;
			bodyFrame = 5;
		} else if (player.getVelocityX() != 0f) {
			if (legFrame < 7) {
				legFrame = 7;
			}

			if (advance) {
				legFrame++;

				if (legFrame > 19) {
					legFrame = 7;
				}

				bodyFrame = legFrame;
			}
		} else {
			legFrame = 0;
			bodyFrame = 0;
		}

		if (player.isUsingItem()) {
			ItemType usedItemType = player.getUsedItem().getType();
			ItemUsageStyle style = usedItemType.getUsageStyle();
			float usageProgress = (player.getUseTime() % usedItemType.getUsageAnimationTime()) / usedItemType.getUsageAnimationTime();

			useOffsetX = style.getUsedItemOffsetX(this, usageProgress);
			useOffsetY = style.getUsedItemOffsetY(this, usageProgress);
			useRotation = style.getUsedItemRotation(this, usageProgress);
			bodyFrame = style.getPlayerBodyFrame(this, usageProgress);
		}
	}

	@Override
	public void render(Batch batch) {
		float centerX = player.getX() + player.getWidth() / 2f;
		float centerY = player.getY() + player.getHeight() / 2f;

		float scaleX = 40f / (player.getWidth() * World.PIXELS_PER_METER);
		float scaleY = 56f / (player.getHeight() * World.PIXELS_PER_METER);

		float x = centerX - player.getWidth() * 0.5f * scaleX;
		float y = centerY - player.getHeight() * 0.5f * scaleY;

		float width = player.getWidth() * scaleX;
		float height = player.getHeight() * scaleY;

		boolean flip = player.getDirectionX() == -1;

		batch.setColor(255 / 255f, 125 / 255f, 90 / 255f, 1f);
		batch.draw(setFlip(Assets.headFrames[getBodyFrame()], flip), x, y, width, height);
		batch.setColor(105 / 255f, 90 / 255f, 75 / 255f, 1f);
		batch.draw(setFlip(Assets.eyesFrames[getBodyFrame()], flip), x, y, width, height);
		batch.setColor(Color.WHITE);
		batch.draw(setFlip(Assets.eyeWhitesFrames[getBodyFrame()], flip), x, y, width, height);
		batch.setColor(255 / 255f, 230 / 255f, 175 / 255f, 1f);
		batch.draw(setFlip(Assets.pantsFrames[getLegFrame()], flip), x, y, width, height);
		batch.setColor(160 / 255f, 180 / 255f, 215 / 255f, 1f);
		batch.draw(setFlip(Assets.undershirtFrames[getBodyFrame()], flip), x, y, width, height);
		batch.setColor(215 / 255f, 90 / 255f, 55 / 255f, 1f);
		batch.draw(setFlip(Assets.hairFrames[Math.max(getBodyFrame() - 6, 0)], flip), x, y, width, height);
		batch.setColor(175 / 255f, 165 / 255f, 140 / 255f, 1f);
		batch.draw(setFlip(Assets.shirtFrames[getBodyFrame()], flip), x, y, width, height);
		batch.setColor(160 / 255f, 105 / 255f, 60 / 255f, 1f);
		batch.draw(setFlip(Assets.shoesFrames[getLegFrame()], flip), x, y, width, height);

		if (player.isUsingItem()) {
			Item item = player.getUsedItem();
			TextureRegion itemTexture = item.getType().getTexture();
			float itemOffsetX = getUseOffsetX();
			float itemOffsetY = getUseOffsetY();
			float itemRotation = getUseRotation();

			batch.setColor(Color.WHITE);
			batch.draw(itemTexture, player.getX() + itemOffsetX, player.getY() + itemOffsetY, 0f, 0f,
					item.getType().getWidth() / World.PIXELS_PER_METER,
					item.getType().getHeight() / World.PIXELS_PER_METER,
					1f, 1f, itemRotation);
		}
	}

	private TextureRegion setFlip(TextureRegion texture, boolean flip) {
		if (texture.isFlipX() != flip)
			texture.flip(true, false);
		return texture;
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
	
	public PlayerEntity getPlayer() {
		return player;
	}
}
