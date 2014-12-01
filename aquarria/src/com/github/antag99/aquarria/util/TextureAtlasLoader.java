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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

/** Texture atlas loader that merges TextureAtlases, and removes unused spacing */
public class TextureAtlasLoader extends AsynchronousAssetLoader<TextureAtlas, TextureAtlasLoader.TextureAtlasParameters> {
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
	}

	@Override
	public TextureAtlas loadSync(AssetManager manager, String fileName, FileHandle file, TextureAtlasParameters parameter) {
		// Asynchronous
		TextureAtlasData loadingTextureAtlasData = new TextureAtlasData(file, file.parent(), false);

		ObjectMap<Page, Pixmap> pages = new ObjectMap<>();

		for (Page page : loadingTextureAtlasData.getPages()) {
			Pixmap pixmap = new Pixmap(page.textureFile);
			pages.put(page, pixmap);
		}

		ObjectMap<Region, Pixmap> regions = new ObjectMap<>();

		for (Region region : loadingTextureAtlasData.getRegions()) {
			Pixmap regionPixmap = new Pixmap(region.width, region.height, Format.RGBA8888);
			Pixmap pagePixmap = pages.get(region.page);
			regionPixmap.drawPixmap(pagePixmap, 0, 0, region.left, region.top, region.width, region.height);
			regions.put(region, regionPixmap);
		}

		for (Entry<Region, Pixmap> region : regions) {
			packer.pack(loadingAtlasId + "_" + region.key.name, region.value);
			region.value.dispose();
		}

		// Synchronous
		packer.updateTextureAtlas(atlas, TextureFilter.Nearest, TextureFilter.Nearest, false);

		TextureAtlas textureAtlas = new TextureAtlas();

		for (Region region : regions.keys()) {
			textureAtlas.addRegion(region.name, atlas.findRegion(loadingAtlasId + "_" + region.name));
		}

		loadingAtlasId++;

		for (Pixmap page : pages.values()) {
			page.dispose();
		}

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
