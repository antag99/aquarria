package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.AbstractType;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.FrameStyle.FrameSkin;

public class WallType extends AbstractType {
	public static Array<WallType> getWallTypes() {
		return AbstractType.getTypes(WallType.class);
	}

	public static WallType forName(String internalName) {
		return AbstractType.forName(WallType.class, internalName);
	}

	public static final WallType air = new WallType("walls/air.json");
	public static final WallType dirt = new DropWallType("walls/dirt.json");
	public static final WallType stone = new DropWallType("walls/stone.json");

	private String displayName;
	private String skinPath;

	private FrameSkin skin;

	public WallType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public WallType(JsonValue properties) {
		super(properties.getString("internalName"));

		displayName = properties.getString("displayName", "");
		skinPath = properties.getString("skin", null);
	}

	public String getDisplayName() {
		return displayName;
	}

	public FrameSkin getSkin() {
		return skin;
	}

	public void destroyed(PlayerEntity player, int x, int y) {
	}

	@Override
	protected void queueAssets(AssetManager assetManager) {
		if (skinPath != null) {
			assetManager.load(skinPath, TextureAtlas.class);
		}
	}

	@Override
	protected void getAssets(AssetManager assetManager) {
		if (skinPath != null) {
			skin = new FrameSkin(assetManager.get(skinPath, TextureAtlas.class));
		}
	}

	@Override
	protected Class<? extends AbstractType> getTypeClass() {
		return WallType.class;
	}
}
