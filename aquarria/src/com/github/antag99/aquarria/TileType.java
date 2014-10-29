package com.github.antag99.aquarria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class TileType {
	public static final float SIZE = 16f;
	
	public static final TileType air = new TileType("tiles/air.json");
	public static final TileType dirt = new TileType("tiles/dirt.json");
	public static final TileType stone = new TileType("tiles/stone.json");
	public static final TileType grass = new TileType("tiles/grass.json");

	private String internalName;
	private String displayName;
	private boolean solid;
	private String texturePath;
	
	private TextureRegion texture;
	
	private static PixmapPacker packer;
	private static TextureAtlas atlas;

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
			if(packer == null || atlas == null) {
				packer = new PixmapPacker(2048, 2048, Format.RGBA8888, 0, false);
				atlas = new TextureAtlas();
			}
			
			texture = atlas.findRegion(texturePath);
			if(texture == null) {
				Pixmap pixmap = new Pixmap(Gdx.files.internal(texturePath));
				packer.pack(texturePath, pixmap);
				pixmap.dispose();
				packer.updateTextureAtlas(atlas, TextureFilter.Nearest, TextureFilter.Nearest, false);
				texture = atlas.findRegion(texturePath);
			}
		}
		
		return texture;
	}
}
