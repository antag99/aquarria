package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
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
	private float weight;
	
	private float defaultWidth;
	private float defaultHeight;
	
	public EntityType(String path) {
		JsonValue value = new JsonReader().parse(Gdx.files.internal(path));
		internalName = value.getString("internalName");
		displayName = value.getString("displayName", "");
		solid = value.getBoolean("solid", true);
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
}
