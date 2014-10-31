package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.ReflectionHelper;

public class EntityType {
	private static Array<EntityType> entityTypes = new Array<EntityType>();
	
	public static Array<EntityType> getEntityTypes() {
		return entityTypes;
	}
	
	public static final EntityType player = new EntityType("entities/player.json");
	
	
	private String internalName;
	private String displayName;
	private boolean solid;
	private String texturePath;
	private float weight;
	
	private float defaultWidth;
	private float defaultHeight;
	
	private Class<? extends EntityView> viewClass;
	private Class<? extends EntityBehaviour> behaviourClass;
	
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
		
		viewClass = ReflectionHelper.forName(value.getString("view", EntityView.class.getName()));
		behaviourClass = ReflectionHelper.forName(value.getString("behaviour", EntityBehaviour.class.getName()));
		
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
	
	public EntityBehaviour createBehaviour(Entity entity) {
		return ReflectionHelper.newInstance(behaviourClass, entity);
	}
	
	public EntityView createView(Entity entity) {
		return ReflectionHelper.newInstance(viewClass, entity);
	}
}
