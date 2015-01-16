package com.github.antag99.aquarria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Stores all game assets to avoid throwing around a single {@link AssetLoader} or something similar.
 */
public class Assets {
	public static TextureRegion[] hairFrames;
	public static TextureRegion[] headFrames;
	public static TextureRegion[] eyesFrames;
	public static TextureRegion[] eyeWhitesFrames;
	public static TextureRegion[] shirtFrames;
	public static TextureRegion[] undershirtFrames;
	public static TextureRegion[] pantsFrames;
	public static TextureRegion[] shoesFrames;

	private static PixmapPacker texturePacker;
	private static TextureAtlas textureAtlas;
	private static FileHandle terrariaAssets;

	static void initialize() {
		texturePacker = new PixmapPacker(2048, 2048, Format.RGBA8888, 2, false);
		textureAtlas = new TextureAtlas();

		terrariaAssets = Gdx.files.local("assets-terraria");

		hairFrames = Util.splitVertically(getTexture("images/player/hair.png"), 14);
		headFrames = Util.splitVertically(getTexture("images/player/head.png"), 20);
		eyesFrames = Util.splitVertically(getTexture("images/player/eyes.png"), 20);
		eyeWhitesFrames = Util.splitVertically(getTexture("images/player/eyeWhites.png"), 20);
		shirtFrames = Util.splitVertically(getTexture("images/player/shirt.png"), 20);
		undershirtFrames = Util.splitVertically(getTexture("images/player/undershirt.png"), 20);
		pantsFrames = Util.splitVertically(getTexture("images/player/pants.png"), 20);
		shoesFrames = Util.splitVertically(getTexture("images/player/shoes.png"), 20);
	}

	static void dispose() {
		textureAtlas.dispose();
	}

	public static TextureRegion getTexture(String path) {
		TextureRegion texture = textureAtlas.findRegion(path);
		if (texture == null) {
			Pixmap pixmap = new Pixmap(terrariaAssets.child(path));
			texturePacker.pack(path, pixmap);
			pixmap.dispose();
			texturePacker.updateTextureAtlas(textureAtlas,
					TextureFilter.Nearest, TextureFilter.Nearest, false);
			texture = textureAtlas.findRegion(path);
		}
		return texture;
	}
}
