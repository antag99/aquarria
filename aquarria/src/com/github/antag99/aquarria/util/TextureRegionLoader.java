/*******************************************************************************
 * Copyright (c) 2014, Anton Gustafsson
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Aquarria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.github.antag99.aquarria.util;

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
