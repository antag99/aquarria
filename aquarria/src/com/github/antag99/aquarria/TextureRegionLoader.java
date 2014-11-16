package com.github.antag99.aquarria;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** Loads images from files and packs them into a texture atlas */
public class TextureRegionLoader extends AsynchronousAssetLoader<TextureRegion,
		TextureRegionLoader.TextureRegionLoaderParameters> implements Disposable {
	
	private PixmapPacker packer;
	private TextureAtlas atlas;
	
	private int pageWidth;
	private int pageHeight;
	
	public TextureRegionLoader(FileHandleResolver resolver, int pageWidth, int pageHeight) {
		super(resolver);
		
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		
		this.packer = new PixmapPacker(pageWidth, pageHeight, Format.RGBA8888, 0, false);
		this.atlas = new TextureAtlas();
	}

	@Override
	public void loadAsync(AssetManager assetManager, String path, FileHandle file, TextureRegionLoaderParameters param) {
		Pixmap pixmap = new Pixmap(file);
		packer.pack(path, pixmap);
		pixmap.dispose();
	}

	@Override
	public TextureRegion loadSync(AssetManager assetManager, String path, FileHandle file, TextureRegionLoaderParameters param) {
		packer.updateTextureAtlas(atlas, TextureFilter.Nearest, TextureFilter.Nearest, false);
		return atlas.findRegion(path);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Array<AssetDescriptor> getDependencies(String path, FileHandle file, TextureRegionLoaderParameters param) {
		return null;
	}
	
	@Override
	public void dispose() {
		// The packer's resources are disposed when the atlas is disposed
		atlas.dispose();
	}
	
	public TextureAtlas getAtlas() {
		return atlas;
	}
	
	public PixmapPacker getPacker() {
		return packer;
	}
	
	public int getPageWidth() {
		return pageWidth;
	}
	
	public int getPageHeight() {
		return pageHeight;
	}

	public static class TextureRegionLoaderParameters extends AssetLoaderParameters<TextureRegion> {
	}
}
