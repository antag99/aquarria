package com.github.antag99.aquarria.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.AbstractType;

public class EntityType extends AbstractType {
	public static Array<EntityType> getEntityTypes() {
		return AbstractType.getTypes(EntityType.class);
	}

	public static EntityType forName(String internalName) {
		return AbstractType.forName(EntityType.class, internalName);
	}

	public static final EntityType player = new EntityType("entities/player.json");
	public static final EntityType item = new EntityType("entities/item.json");

	private String displayName;
	private boolean solid;
	private float weight;
	private int maxHealth;

	private float defaultWidth;
	private float defaultHeight;

	public EntityType(JsonValue properties) {
		super(properties.getString("internalName"));

		displayName = properties.getString("displayName", "");
		solid = properties.getBoolean("solid", true);
		weight = properties.getFloat("weight", 1f);
		maxHealth = properties.getInt("maxHealth", 0);

		defaultWidth = properties.getFloat("width");
		defaultHeight = properties.getFloat("height");
	}

	public EntityType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
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

	public int getMaxHealth() {
		return maxHealth;
	}

	public float getDefaultWidth() {
		return defaultWidth;
	}

	public float getDefaultHeight() {
		return defaultHeight;
	}

	@Override
	protected Class<? extends AbstractType> getTypeClass() {
		return EntityType.class;
	}
}
