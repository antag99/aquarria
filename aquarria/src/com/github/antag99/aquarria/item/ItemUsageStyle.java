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
package com.github.antag99.aquarria.item;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.entity.PlayerView;

public abstract class ItemUsageStyle {
	private static ObjectMap<String, ItemUsageStyle> usageStyles = new ObjectMap<String, ItemUsageStyle>();

	public static final ItemUsageStyle swing = new ItemUsageStyle("swing") {
		@Override
		public float getUsedItemOffsetX(PlayerView player, float usageProgress) {
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

			if (player.getPlayer().getDirectionX() == 1) {
				useOffsetX = 1.25f - useOffsetX;
			}

			return useOffsetX;
		}

		@Override
		public float getUsedItemOffsetY(PlayerView player, float usageProgress) {
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
		public float getUsedItemRotation(PlayerView player, float usageProgress) {
			if (player.getPlayer().getDirectionX() == 1) {
				return 90f - 180f * usageProgress;
			} else {
				return 180f * usageProgress;
			}
		}

		@Override
		public int getPlayerBodyFrame(PlayerView player, float usageProgress) {
			return (int) (usageProgress * 4) + 1;
		}
	};

	private String internalName;

	public ItemUsageStyle(String internalName) {
		this.internalName = internalName;
		usageStyles.put(internalName, this);
	}

	public abstract float getUsedItemOffsetX(PlayerView player, float usageProgress);

	public abstract float getUsedItemOffsetY(PlayerView player, float usageProgress);

	public abstract float getUsedItemRotation(PlayerView player, float usageProgress);

	public int getPlayerBodyFrame(PlayerView player, float usageProgress) {
		return player.getBodyFrame();
	}

	public String getInternalName() {
		return internalName;
	}

	public static ItemUsageStyle forName(String internalName) {
		return usageStyles.get(internalName);
	}
}
