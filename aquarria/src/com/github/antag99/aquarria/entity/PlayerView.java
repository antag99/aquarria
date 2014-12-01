/*******************************************************************************
 * Copyright (c) 2014, Anton Gustafsson
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
