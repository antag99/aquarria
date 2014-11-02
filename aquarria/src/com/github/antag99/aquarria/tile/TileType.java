package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class TileType {
	private static Array<TileType> tileTypes = new Array<TileType>();
	private static ObjectMap<String, TileType> tileTypesByName = new ObjectMap<String, TileType>();
	
	public static Array<TileType> getTileTypes() {
		return tileTypes;
	}
	
	public static TileType forName(String internalName) {
		return tileTypesByName.get(internalName);
	}
	
	public static final TileType air = new TileType("tiles/air.json");
	public static final TileType dirt = new TileType("tiles/dirt.json");
	public static final TileType stone = new TileType("tiles/stone.json");
	public static final TileType grass = new TileType("tiles/grass.json");

	private String internalName;
	private String displayName;
	private boolean solid;
	private String texturePath;
	
	private TextureRegion texture;

	public TileType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}
	
	public TileType(JsonValue properties) {
		internalName = properties.getString("internalName");
		displayName = properties.getString("displayName", "");
		texturePath = properties.getString("texture", null);
		solid = properties.getBoolean("solid", true);
		
		tileTypes.add(this);
		tileTypesByName.put(internalName, this);
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
	
	public String getTexturePath() {
		return texturePath;
	}
	
	public void getTexture(AssetManager assetManager) {
		if(texturePath != null) {
			texture = assetManager.get(texturePath);
		}
	}
	
	public TextureRegion getTexture() {
		return texture;
	}
}
