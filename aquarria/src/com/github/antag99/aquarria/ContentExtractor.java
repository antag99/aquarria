package com.github.antag99.aquarria;

import com.badlogic.gdx.files.FileHandle;
import com.github.antag99.aquarria.xnb.XnbExtractor;
import com.github.antag99.aquarria.xnb.XnbFontExtractor;
import com.github.antag99.aquarria.xnb.XnbSoundExtractor;
import com.github.antag99.aquarria.xnb.XnbTextureExtractor;

public class ContentExtractor {
	
	private FileHandle contentDirectory;
	private FileHandle outputAssetDirectory;
	
	public ContentExtractor(FileHandle contentDirectory, FileHandle outputAssetDirectory) {
		this.contentDirectory = contentDirectory;
		this.outputAssetDirectory = outputAssetDirectory;
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
			fontExtractor.extract(font, rawFontsDir.child(font.name()));
		}

		XnbExtractor soundExtractor = new XnbSoundExtractor();
		FileHandle rawSoundDir = rawDir.child("sound");
		
		for(FileHandle sound : contentDirectory.child("Sound").list(".xnb")) {
			soundExtractor.extract(sound, rawSoundDir.child(sound.name()));
		}
	}
	
	public FileHandle getContentDirectory() {
		return contentDirectory;
	}
	
	public FileHandle getOutputAssetDirectory() {
		return outputAssetDirectory;
	}
}
