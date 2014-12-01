package com.github.antag99.aquarria.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.AbstractType;
import com.github.antag99.aquarria.entity.PlayerEntity;

public class ItemType extends AbstractType {
	public static Array<ItemType> getItemTypes() {
		return AbstractType.getTypes(ItemType.class);
	}

	public static ItemType forName(String internalName) {
		return AbstractType.forName(ItemType.class, internalName);
	}

	public static final ItemType air = new ItemType("items/air.json");
	public static final ItemType dirt = new TileItemType("items/dirt.json");
	public static final ItemType stone = new TileItemType("items/stone.json");
	public static final ItemType pickaxe = new PickaxeItemType("items/pickaxe.json");
	public static final ItemType hammer = new HammerItemType("items/hammer.json");

	public static final ItemType dirtWall = new WallItemType("items/dirtWall.json");
	public static final ItemType stoneWall = new WallItemType("items/stoneWall.json");

	private String internalName;
	private String displayName;
	private int maxStack;
	private float width;
	private float height;
	private String texturePath;
	private float usageTime;
	private float usageAnimationTime;
	private boolean usageRepeat;
	private boolean consumable;
	private ItemUsageStyle usageStyle;

	private TextureRegion texture;

	public ItemType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public ItemType(JsonValue properties) {
		super(properties.getString("internalName"));

		displayName = properties.getString("displayName");
		maxStack = properties.getInt("maxStack", 1);
		width = properties.getFloat("width", 0f);
		height = properties.getFloat("height", 0f);
		texturePath = properties.getString("texture", null);
		usageTime = properties.getFloat("usageTime", 0f);
		usageAnimationTime = properties.getFloat("usageAnimationTime", usageTime);
		usageRepeat = properties.getBoolean("usageRepeat", false);
		consumable = properties.getBoolean("consumable", false);
		usageStyle = ItemUsageStyle.forName(properties.getString("usageStyle", "swing"));
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

	@Override
	protected void queueAssets(AssetManager assetManager) {
		if (texturePath != null) {
			assetManager.load(texturePath, TextureRegion.class);
		}
	}

	@Override
	protected void getAssets(AssetManager assetManager) {
		if (texturePath != null) {
			texture = assetManager.get(texturePath);
		}
	}

	@Override
	protected Class<? extends AbstractType> getTypeClass() {
		return ItemType.class;
	}

	public TextureRegion getTexture() {
		return texture;
	}

	public float getUsageTime() {
		return usageTime;
	}

	public float getUsageAnimationTime() {
		return usageAnimationTime;
	}

	public boolean getUsageRepeat() {
		return usageRepeat;
	}

	public boolean isConsumable() {
		return consumable;
	}

	public ItemUsageStyle getUsageStyle() {
		return usageStyle;
	}

	public boolean canUseItem(PlayerEntity player, Item item) {
		return false;
	}

	public void beginUseItem(PlayerEntity player, Item item) {
	}

	public void updateUseItem(PlayerEntity player, Item item, float delta) {
	}

	public boolean useItem(PlayerEntity player, Item item) {
		return false;
	}
}
