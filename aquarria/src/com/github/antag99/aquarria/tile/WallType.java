package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.AbstractType;

public class WallType extends AbstractType {
	public static Array<WallType> getWallTypes() {
		return AbstractType.getTypes(WallType.class);
	}
	
	public static WallType forName(String internalName) {
		return AbstractType.forName(WallType.class, internalName);
	}
	
	private String displayName;
	private String texturePath;
	
	private TextureRegion texture;
	
	public WallType(FileHandle file) {
		this(new JsonReader().parse(file));
	}
	
	public WallType(JsonValue properties) {
		super(properties.getString("internalName"));
		
		displayName = properties.getString("displayName", "");
		texturePath = properties.getString("texturePath", null);
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
	
	@Override
	protected void queueAssets(AssetManager assetManager) {
		if(texturePath != null) {
			assetManager.load(texturePath, TextureRegion.class);
		}
	}
	
	@Override
	protected void getAssets(AssetManager assetManager) {
		if(texturePath != null) {
			texture = assetManager.get(texturePath);
		}
	}
	
	@Override
	protected Class<? extends AbstractType> getTypeClass() {
		return WallType.class;
	}
}
