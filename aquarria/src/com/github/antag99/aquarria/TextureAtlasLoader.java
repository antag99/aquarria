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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

/** Texture atlas loader that merges TextureAtlases, and removes unused spacing */
public class TextureAtlasLoader extends AsynchronousAssetLoader<TextureAtlas, TextureAtlasLoader.TextureAtlasParameters> {
	private Array<String> loadingAtlasNames = new Array<String>();
	private int loadingAtlasId = 0;

	private PixmapPacker packer;
	private TextureAtlas atlas;

	private int pageWidth;
	private int pageHeight;

	public TextureAtlasLoader(FileHandleResolver resolver, int pageWidth, int pageHeight) {
		super(resolver);
		
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		
		this.packer = new PixmapPacker(pageWidth, pageHeight, Format.RGBA8888, 0, false);
		this.atlas = new TextureAtlas();
	}

	public class TextureAtlasParameters extends AssetLoaderParameters<TextureAtlas> {
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureAtlasParameters parameter) {
		TextureAtlasData loadingTextureAtlasData = new TextureAtlasData(file, file.parent(), false);
		
		ObjectMap<Page, Pixmap> pages = new ObjectMap<>();
		
		for(Page page : loadingTextureAtlasData.getPages()) {
			Pixmap pixmap = new Pixmap(page.textureFile);
			pages.put(page, pixmap);
		}
		
		ObjectMap<Region, Pixmap> regions = new ObjectMap<>();
		
		for(Region region : loadingTextureAtlasData.getRegions()) {
			Pixmap pixmap = new Pixmap(region.width, region.height, Format.RGBA8888);
			Pixmap pagePixmap = pages.get(region.page);
			pixmap.drawPixmap(pagePixmap, 0, 0, region.left, region.top, region.width, region.height);
			regions.put(region, pixmap);
			loadingAtlasNames.add(region.name);
		}
		
		for(Pixmap page : pages.values()) {
			page.dispose();
		}
		
		for(Entry<Region, Pixmap> region : regions) {
			packer.pack(loadingAtlasId + "_" + region.key.name, region.value);
			region.value.dispose();
		}
	}

	@Override
	public TextureAtlas loadSync(AssetManager manager, String fileName, FileHandle file, TextureAtlasParameters parameter) {
		packer.updateTextureAtlas(atlas, TextureFilter.Nearest, TextureFilter.Nearest, false);
		
		TextureAtlas textureAtlas = new TextureAtlas();
		
		for(String regionName : loadingAtlasNames) {
			textureAtlas.addRegion(regionName, atlas.findRegion(loadingAtlasId + "_" + regionName));
		}
		
		loadingAtlasNames.clear();
		loadingAtlasId++;
		
		return textureAtlas;
	}
	
	public PixmapPacker getPacker() {
		return packer;
	}
	
	public TextureAtlas getAtlas() {
		return atlas;
	}
	
	public int getPageWidth() {
		return pageWidth;
	}
	
	public int getPageHeight() {
		return pageHeight;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureAtlasParameters parameter) {
		return null;
	}
}
