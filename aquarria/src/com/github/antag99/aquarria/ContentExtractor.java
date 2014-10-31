package com.github.antag99.aquarria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.xnb.XnbExtractor;
import com.github.antag99.aquarria.xnb.XnbFontExtractor;
import com.github.antag99.aquarria.xnb.XnbSoundExtractor;
import com.github.antag99.aquarria.xnb.XnbTextureExtractor;

public class ContentExtractor {
	
	private JsonValue xnbMap;
	
	private FileHandle contentDirectory;
	private FileHandle outputAssetDirectory;
	
	public ContentExtractor(FileHandle contentDirectory, FileHandle outputAssetDirectory) {
		this.contentDirectory = contentDirectory;
		this.outputAssetDirectory = outputAssetDirectory;
		xnbMap = new JsonReader().parse(Gdx.files.internal("xnbmap.json"));
	}
	
	public void extract() {
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
		
		for(JsonValue value : xnbMap) {
			FileHandle src = rawDir.child(value.name);
			FileHandle dest = outputAssetDirectory.child(value.asString());
			src.moveTo(dest);
		}
	}
	
	public FileHandle getContentDirectory() {
		return contentDirectory;
	}
	
	public FileHandle getOutputAssetDirectory() {
		return outputAssetDirectory;
	}
}
