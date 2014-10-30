package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.TexturePack;

public class TileType {
	public static final TileType air = new TileType("tiles/air.json");
	public static final TileType dirt = new TileType("tiles/dirt.json");
	public static final TileType stone = new TileType("tiles/stone.json");
	public static final TileType grass = new TileType("tiles/grass.json");

	private String internalName;
	private String displayName;
	private boolean solid;
	private String texturePath;
	
	private TextureRegion texture;
	
	private static TexturePack texturePack;
	

	public TileType(String path) {
		JsonValue value = new JsonReader().parse(Gdx.files.internal(path));
		internalName = value.getString("internalName");
		displayName = value.getString("displayName", "");
		texturePath = value.getString("texture", null);
		solid = value.getBoolean("solid", true);
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

	public TextureRegion getTexture() {
		if(texture == null && texturePath != null) {
			if(texturePack == null) {
				texturePack = new TexturePack();
			}
			texture = texturePack.getTexture(texturePath);
		}
		
		return texture;
	}
}
