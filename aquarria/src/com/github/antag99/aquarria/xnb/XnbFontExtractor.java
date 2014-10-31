package com.github.antag99.aquarria.xnb;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonWriter;

public class XnbFontExtractor extends XnbExtractor {
	private static final int SURFACEFORMAT_DXT3 = 5;

	@Override
	public void extract(FileHandle source, FileHandle dest) {
		super.extract(source, dest);
		
		if(!primaryType.equals("SpriteFont")) {
			throw new RuntimeException("Expected primary type to be SpriteFont, was " + primaryType);
		}
		
		if(get7BitEncodedInt(buffer) == 0) {
			throw new RuntimeException("Sprite font texture is null");
		} else {
			int surfaceFormat = buffer.getInt();
			
			int width = buffer.getInt();
			int height = buffer.getInt();
			
			int mipCount = buffer.getInt();
			int size = buffer.getInt();
			
			System.out.println("font texture width=" + width + ", height=" + height + ", mipCount=" + mipCount + ", size=" + size);
			
			FileHandle textureFile = dest.sibling(dest.nameWithoutExtension() + ".png");

			if(surfaceFormat != SURFACEFORMAT_DXT3) {
				throw new RuntimeException("Unexpected surface format: " + surfaceFormat);
			}
			
			try {
				BufferedImage image = Dxt3.getBufferedImage(width, height, buffer);
				OutputStream textureFileStream = textureFile.write(false);
				ImageIO.write(image, "png", textureFileStream);
				textureFileStream.close();
			} catch(IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		
		FileHandle jsonFile = dest.sibling(dest.nameWithoutExtension() + ".json");
		
		List<Rectangle> glyphs = new ArrayList<Rectangle>();
		getList(buffer, glyphs, Rectangle.class);
		
		List<Rectangle> cropping = new ArrayList<Rectangle>();
		getList(buffer, cropping, Rectangle.class);
		
		get7BitEncodedInt(buffer);
		int mapSize = buffer.getInt();
		List<Character> characterMap = new ArrayList<Character>(mapSize);
		for(int k = 0; k < mapSize; ++k) {
			characterMap.add(getCSharpChar(buffer));
		}
		
		int verticalLineSpacing = buffer.getInt();
		float horozontalSpacing = buffer.getFloat();
		
		List<Vector3> kerning = new ArrayList<Vector3>();
		getList(buffer, kerning, Vector3.class);
		
		char defaultCharacter = ' ';
		
		if(buffer.get() != 0) { // Boolean
			defaultCharacter = getCSharpChar(buffer);
		}
		
		if(glyphs.size() != cropping.size() || cropping.size() != characterMap.size()) {
			throw new RuntimeException("Character information size mismatch");
		}
		
		try {
			// Store the properties in a JSON file. Might need some tweaking before being usable.
			JsonWriter jsonWriter = new JsonWriter(jsonFile.writer(false));
			jsonWriter.object();
			jsonWriter.set("defaultCharacter", defaultCharacter);
			jsonWriter.set("verticalLineSpacing", verticalLineSpacing);
			jsonWriter.set("horozontalLineSpacing", horozontalSpacing);
			jsonWriter.object("characters");
			for(int i = 0; i < characterMap.size(); ++i) {
				jsonWriter.object(characterMap.get(i).toString());
				Rectangle glyph = glyphs.get(i);
				Rectangle crop = cropping.get(i);
				jsonWriter.set("x", (int)glyph.x);
				jsonWriter.set("y", (int)glyph.y);
				jsonWriter.set("width", (int)glyph.width);
				jsonWriter.set("height", (int)glyph.height);
				jsonWriter.set("padLeft", (int)crop.x);
				jsonWriter.set("padTop", (int)crop.y);
				jsonWriter.set("padRight", (int)crop.width);
				jsonWriter.set("padBottom", (int)crop.height);
			}
			jsonWriter.pop();
			jsonWriter.array("kerning");
			for(int i = 0; i < kerning.size(); ++i) {
				jsonWriter.object();
				Vector3 kern = kerning.get(i);
				jsonWriter.set("a", (int)kern.x);
				jsonWriter.set("b", (int)kern.y);
				jsonWriter.set("c", (int)kern.z);
				jsonWriter.pop();
			}
			jsonWriter.pop();
			jsonWriter.pop();
			jsonWriter.close();
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
