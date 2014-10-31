package com.github.antag99.aquarria.xnb;

import java.nio.ByteOrder;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;

public class XnbTextureExtractor extends XnbExtractor {
	private static int COLOR = 0;
	
	@Override
	public void extract(FileHandle source, FileHandle dest) {
		super.extract(source, dest);
		
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
		
		// To read RGBA values as integers
		buffer.order(ByteOrder.BIG_ENDIAN);
		
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				pixmap.drawPixel(x, y, buffer.getInt());
			}
		}
		PixmapIO.writePNG(dest, pixmap);
		pixmap.dispose();
	}
}
