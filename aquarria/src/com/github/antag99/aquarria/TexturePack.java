package com.github.antag99.aquarria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class TexturePack implements Disposable {
	private PixmapPacker packer;
	private TextureAtlas atlas;
	
	public TexturePack() {
		packer = new PixmapPacker(2048, 2048, Format.RGBA8888, 0, false);
		atlas = new TextureAtlas();
	}
	
	public TextureRegion getTexture(String path) {
		TextureRegion texture = atlas.findRegion(path);
		if(texture == null) {
			Pixmap pixmap = new Pixmap(Gdx.files.internal(path));
			packer.pack(path, pixmap);
			pixmap.dispose();
			packer.updateTextureAtlas(atlas, TextureFilter.Nearest, TextureFilter.Nearest, false);
			texture = atlas.findRegion(path);
		}
		
		return texture;
	}

	@Override
	public void dispose() {
		atlas.dispose();
		atlas = null;
		// Packer shouldn't be disposed
		packer = null;
	}
}
