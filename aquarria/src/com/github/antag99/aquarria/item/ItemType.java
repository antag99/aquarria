package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.entity.PlayerEntity;

public class ItemType {
	private static Array<ItemType> itemTypes = new Array<ItemType>();
	private static ObjectMap<String, ItemType> itemTypesByName = new ObjectMap<String, ItemType>();
	
	public static Array<ItemType> getItemTypes() {
		return itemTypes;
	}
	
	public static ItemType forName(String internalName) {
		return itemTypesByName.get(internalName);
	}
	
	public static final ItemType air = new ItemType("items/air.json");
	public static final ItemType dirt = new TileItemType("items/dirt.json");
	public static final ItemType stone = new TileItemType("items/stone.json");
	public static final ItemType pickaxe = new PickaxeItemType("items/pickaxe.json");
	
	private String internalName;
	private String displayName;
	private int maxStack;
	private float width;
	private float height;
	private String texturePath;
	
	private TextureRegion texture;
	
	public ItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}
	
	public ItemType(JsonValue properties) {
		internalName = properties.getString("internalName");
		displayName = properties.getString("displayName");
		maxStack = properties.getInt("maxStack", 1);
		width = properties.getFloat("width", 0f);
		height = properties.getFloat("height", 0f);
		texturePath = properties.getString("texture", null);
		
		itemTypes.add(this);
		itemTypesByName.put(internalName, this);
	}
	
	public String getInternalName() {
		return internalName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public int getMaxStack() {
		return maxStack;
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
	
	public boolean useItem(PlayerEntity player, Item item) {
		return false;
	}
}
