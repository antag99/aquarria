package com.github.antag99.aquarria.xnb;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.utils.Array;

public class XnbTextureAssetLoader extends XnbAssetLoader<Pixmap, AssetLoaderParameters<Pixmap>> {
	private Pixmap pixmap;
	private static int COLOR = 0;
	
	public XnbTextureAssetLoader(FileHandleResolver resolver) {
		super(resolver);
	}
	
	@Override
	public void loadAsync(AssetManager assetManager, String path, FileHandle file, AssetLoaderParameters<Pixmap> params) {
		super.loadAsync(assetManager, path, file, params);
		
		if(!primaryType.equals("Texture2D")) {
			throw new RuntimeException("Expected primary type to be Texture2D, was " + primaryType);
		}
		
		int surfaceFormat = buffer.getInt();
		int width = buffer.getInt();
		int height = buffer.getInt();
		
		if(surfaceFormat != COLOR) {
			throw new RuntimeException("Expected surface format to be COLOR, was " + surfaceFormat);
		}
		
		int mipCount = buffer.getInt();
		int size = buffer.getInt();
		
		if(mipCount != 1) {
			throw new RuntimeException("Invalid mipmap count: " + mipCount);
		}
		
		if(size != width * height * 4) {
			throw new RuntimeException("Invalid size: " + size + "(width=" + width + ", height=" + height + ")");
		}
		
		pixmap = new Pixmap(width, height, Format.RGBA8888);
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				pixmap.drawPixel(x, y, buffer.getInt());
			}
		}
	}

	@Override
	public Pixmap loadSync(AssetManager assetManager, String path, FileHandle file, AssetLoaderParameters<Pixmap> params) {
		Pixmap result = pixmap;
		pixmap = null;
		buffer = null;
		return result;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Array<AssetDescriptor> getDependencies(String path, FileHandle file, AssetLoaderParameters<Pixmap> params) {
		return null;
	}
}
