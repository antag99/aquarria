package com.github.antag99.aquarria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class EntityType {
	public static final EntityType player = new EntityType("entities/player.json");
	
	private String internalName;
	private String displayName;
	private boolean solid;
	private String texturePath;
	private float weight;
	
	private float width;
	private float height;
	
	private Class<? extends EntityView> viewClass;
	private Class<? extends EntityBehaviour> behaviourClass;
	
	private TextureRegion texture;
	private TexturePack texturePack;
	
	public EntityType(String path) {
		JsonValue value = new JsonReader().parse(Gdx.files.internal(path));
		internalName = value.getString("internalName");
		displayName = value.getString("displayName", "");
		solid = value.getBoolean("solid", true);
		texturePath = value.getString("texture");
		weight = value.getFloat("weight", 1f);
		
		width = value.getFloat("width");
		height = value.getFloat("height");
		
		viewClass = ReflectionHelper.forName(value.getString("view", EntityView.class.getName()));
		behaviourClass = ReflectionHelper.forName(value.getString("behaviour", EntityBehaviour.class.getName()));
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
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public TextureRegion getTexture() {
		if(texture == null && texturePath != null) {
			if(texturePack == null) {
				texturePack = new TexturePack();
			}
			texture = texturePack.getTexture(texturePath);
		}
		
		return texture;
	}
	
	public EntityBehaviour createBehaviour(Entity entity) {
		return ReflectionHelper.newInstance(behaviourClass, entity);
	}
	
	public EntityView createView(Entity entity) {
		return ReflectionHelper.newInstance(viewClass, entity);
	}
}
