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
package com.github.antag99.aquarria;

import com.github.antag99.aquarria.entity.PlayerEntity;

/**
 * ItemAnimation is used to implement different animations
 * that are played when using items. These animations need to
 * set the offset of the held item and take care of the player's
 * body animation, which is different for swinging, stabbing and so on.
 * </p>
 * Note that animations will have to respect the players direction when
 * computing item offset and rotation, this is not done automatically.
 */
public abstract class ItemAnimation {
	public ItemAnimation() {
	}

	/** Gets the rendering offset of the held item with this animation */
	public abstract float getHeldItemOffsetX(PlayerEntity player);

	/** Gets the rendering offset of the held item with this animation */
	public abstract float getHeldItemOffsetY(PlayerEntity player);

	/** Gets the rotation of the held item with this animation */
	public abstract float getHeldItemRotation(PlayerEntity player);

	/**
	 * Gets the animation for the players body when using this item.
	 * This animation will receive the progress of using the item, which
	 * is in the range [0, 1], plus 1 for every time the animation has been run.
	 */
	public abstract SpriteAnimation getBodyAnimation(PlayerEntity player);

	/** Swinging animation, used for pickaxe/other swung tools */
	public static final ItemAnimation swing = new ItemAnimation() {
		@Override
		public float getHeldItemOffsetX(PlayerEntity player) {
			Item heldItem = player.getUsedItem();
			float animationTime = heldItem.getType().getUsageDelay();
			float usageProgress = (player.getUseTime() % animationTime) / animationTime;
			float useOffsetX;
			if (usageProgress < 1f / 4f) {
				useOffsetX = 1.15f;
			} else if (usageProgress < 2f / 4f) {
				useOffsetX = 0.2f;
			} else if (usageProgress < 3f / 4f) {
				useOffsetX = 0.1f;
			} else {
				useOffsetX = 0.2f;
			}

			if (player.getDirectionX() == 1) {
				useOffsetX = 1.25f - useOffsetX;
			}

			return useOffsetX;
		}

		@Override
		public float getHeldItemOffsetY(PlayerEntity player) {
			Item heldItem = player.getUsedItem();
			float animationTime = heldItem.getType().getUsageDelay();
			float usageProgress = (player.getUseTime() % animationTime) / animationTime;
			if (usageProgress < 1f / 4f) {
				return -(1f - 24f / 56f) + 2.625f;
			} else if (usageProgress < 2f / 4f) {
				return 2.625f / 2f + 0.5f;
			} else if (usageProgress < 3f / 4f) {
				return 0.7f;
			} else {
				return 0.3f;
			}
		}

		@Override
		public float getHeldItemRotation(PlayerEntity player) {
			Item heldItem = player.getUsedItem();
			float animationTime = heldItem.getType().getUsageDelay();
			float usageProgress = (player.getUseTime() % animationTime) / animationTime;
			if (player.getDirectionX() == 1) {
				return 90f - 180f * usageProgress;
			} else {
				return 180f * usageProgress;
			}
		}

		@Override
		public SpriteAnimation getBodyAnimation(PlayerEntity player) {
			return Assets.playerSwing;
		}
	};
}
