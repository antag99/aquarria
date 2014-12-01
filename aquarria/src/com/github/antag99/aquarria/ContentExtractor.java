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
package com.github.antag99.aquarria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.util.Utils;
import com.github.antag99.aquarria.xnb.XnbExtractor;
import com.github.antag99.aquarria.xnb.XnbFontExtractor;
import com.github.antag99.aquarria.xnb.XnbSoundExtractor;
import com.github.antag99.aquarria.xnb.XnbTextureExtractor;

public class ContentExtractor {
	private JsonValue xnbMap;
	private JsonValue canvasMap;
	private JsonValue atlases;

	private FileHandle contentDirectory;
	private FileHandle outputAssetDirectory;

	public ContentExtractor(FileHandle contentDirectory, FileHandle outputAssetDirectory) {
		this.contentDirectory = contentDirectory;
		this.outputAssetDirectory = outputAssetDirectory;
		xnbMap = new JsonReader().parse(Gdx.files.internal("xnbmap.json"));
		canvasMap = new JsonReader().parse(Gdx.files.internal("canvasmap.json"));
		atlases = new JsonReader().parse(Gdx.files.internal("atlases.json"));
	}

	public void extract() {
		// Extract XNB files to raw/
		FileHandle rawDir = outputAssetDirectory.child("raw");

		XnbExtractor textureExtractor = new XnbTextureExtractor();
		FileHandle rawImagesDir = rawDir.child("images");

		for (FileHandle image : contentDirectory.child("Images").list(".xnb")) {
			textureExtractor.extract(image, rawImagesDir.child(image.nameWithoutExtension() + ".png"));
		}

		XnbExtractor fontExtractor = new XnbFontExtractor();
		FileHandle rawFontsDir = rawDir.child("fonts");

		for (FileHandle font : contentDirectory.child("Fonts").list(".xnb")) {
			fontExtractor.extract(font, rawFontsDir.child(font.nameWithoutExtension() + ".png"));
		}

		XnbExtractor soundExtractor = new XnbSoundExtractor();
		FileHandle rawSoundDir = rawDir.child("sound");

		for (FileHandle sound : contentDirectory.child("Sounds").list(".xnb")) {
			soundExtractor.extract(sound, rawSoundDir.child(sound.nameWithoutExtension() + ".wav"));
		}

		// Some animations are 40x1120, and some 40x1118; fix that.
		for (JsonValue value : canvasMap) {
			FileHandle file = rawDir.child(value.name);
			int width = value.asIntArray()[0];
			int height = value.asIntArray()[1];

			Pixmap pixmap = new Pixmap(file);
			Pixmap newPixmap = Utils.resizeCanvas(pixmap, width, height);
			pixmap.dispose();
			PixmapIO.writePNG(file, newPixmap);
			newPixmap.dispose();
		}

		// Move image files from the raw/ directory to the target directory,
		// according to the configuration.
		for (JsonValue value : xnbMap) {
			FileHandle src = rawDir.child(value.name);
			FileHandle dest = outputAssetDirectory.child(value.asString());
			src.moveTo(dest);
		}

		// Create texture atlases for tile spritesheets
		for (JsonValue value : atlases) {
			String atlasImagePath = value.name;
			String templatePath = value.getString("template");

			String template = Gdx.files.internal(templatePath).readString();
			FileHandle atlasImageFile = outputAssetDirectory.child(atlasImagePath);
			FileHandle atlasDataFile = atlasImageFile.sibling(atlasImageFile.nameWithoutExtension() + ".atlas");

			Pixmap pixmap = new Pixmap(atlasImageFile);
			ObjectMap<String, String> replacements = new ObjectMap<String, String>();
			replacements.put("imageFile", atlasImageFile.name());
			replacements.put("imageWidth", Integer.toString(pixmap.getWidth()));
			replacements.put("imageHeight", Integer.toString(pixmap.getHeight()));

			atlasDataFile.writeString(Utils.replaceFormat(template, replacements, null), false);
		}

		// Create texture atlas for all UI images
		Settings settings = new Settings();
		settings.minWidth = 32;
		settings.minHeight = 32;
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.pot = true;
		settings.paddingX = 0;
		settings.paddingY = 0;

		String inputDirectory = outputAssetDirectory.child("images/ui").path();
		String outputDirectory = outputAssetDirectory.child("images/ui/atlas").path();
		String packFileName = "ui";

		TexturePacker.process(settings, inputDirectory, outputDirectory, packFileName);
	}
}
