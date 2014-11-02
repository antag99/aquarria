package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class EntityType {
	private static Array<EntityType> entityTypes = new Array<EntityType>();
	
	public static Array<EntityType> getEntityTypes() {
		return entityTypes;
	}
	
	public static final EntityType player = new EntityType("entities/player.json");
	public static final EntityType item = new EntityType("entities/item.json");
	
	private String internalName;
	private String displayName;
	private boolean solid;
	private String texturePath;
	private float weight;
	
	private float defaultWidth;
	private float defaultHeight;
	
	private TextureRegion texture;
	
	public EntityType(String path) {
		JsonValue value = new JsonReader().parse(Gdx.files.internal(path));
		internalName = value.getString("internalName");
		displayName = value.getString("displayName", "");
		solid = value.getBoolean("solid", true);
		texturePath = value.getString("texture");
		weight = value.getFloat("weight", 1f);
		
		defaultWidth = value.getFloat("width");
		defaultHeight = value.getFloat("height");
		
		entityTypes.add(this);
	}
	
	public String getInternalName() {
		return internalName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public float getDefaultWidth() {
		return defaultWidth;
	}
	
	public float getDefaultHeight() {
		return defaultHeight;
	}
	
	public String getTexturePath() {
		return texturePath;
	}
	
	public void getTexture(AssetManager assetManager) {
		if(texturePath != null)
			texture = assetManager.get(texturePath);
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
}
