package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.AbstractType;
import com.github.antag99.aquarria.entity.PlayerEntity;

public class TileType extends AbstractType {
	public static Array<TileType> getTileTypes() {
		return AbstractType.getTypes(TileType.class);
	}
	
	public static TileType forName(String internalName) {
		return AbstractType.forName(TileType.class, internalName);
	}
	
	public static final TileType air = new TileType("tiles/air.json");
	public static final TileType dirt = new DropTileType("tiles/dirt.json");
	public static final TileType stone = new DropTileType("tiles/stone.json");
	public static final TileType grass = new DropTileType("tiles/grass.json");

	private String displayName;
	private boolean solid;
	private String texturePath;
	
	private TextureRegion texture;

	public TileType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}
	
	public TileType(JsonValue properties) {
		super(properties.getString("internalName"));
		
		displayName = properties.getString("displayName", "");
		texturePath = properties.getString("texture", null);
		solid = properties.getBoolean("solid", true);
	}
	
	public void destroyed(PlayerEntity player, int x, int y) {
	}

	public String getDisplayName() {
		return displayName;
	}

	public boolean isSolid() {
		return solid;
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
		return TileType.class;
	}
}
