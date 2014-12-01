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

			if (player.getEntity().getDirection() == 1) {
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
			if (player.getEntity().getDirection() == 1) {
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
