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
		
		for(FileHandle image : contentDirectory.child("Images").list(".xnb")) {
			textureExtractor.extract(image, rawImagesDir.child(image.nameWithoutExtension() + ".png"));
		}

		XnbExtractor fontExtractor = new XnbFontExtractor();
		FileHandle rawFontsDir = rawDir.child("fonts");
		
		for(FileHandle font : contentDirectory.child("Fonts").list(".xnb")) {
			fontExtractor.extract(font, rawFontsDir.child(font.nameWithoutExtension() + ".png"));
		}

		XnbExtractor soundExtractor = new XnbSoundExtractor();
		FileHandle rawSoundDir = rawDir.child("sound");
		
		for(FileHandle sound : contentDirectory.child("Sounds").list(".xnb")) {
			soundExtractor.extract(sound, rawSoundDir.child(sound.nameWithoutExtension() + ".wav"));
		}
		
		// Some animations are 40x1120, and some 40x1118; fix that.
		for(JsonValue value : canvasMap) {
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
		for(JsonValue value : xnbMap) {
			FileHandle src = rawDir.child(value.name);
			FileHandle dest = outputAssetDirectory.child(value.asString());
			src.moveTo(dest);
		}
		
		// Create texture atlases for tile spritesheets
		for(JsonValue value : atlases) {
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
