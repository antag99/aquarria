package com.github.antag99.aquarria;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Utils {
	public static TextureRegion[] splitVertically(TextureRegion target, int count) {
		TextureRegion[] result = new TextureRegion[count];
		int width = target.getRegionWidth();
		int height = target.getRegionHeight() / count;
		
		for(int i = 0; i < count; ++i) {
			result[i] = new TextureRegion(target, 0, i * height, width, height);
		}
		
		return result;
	}
	
	public static Pixmap[] splitVertically(Pixmap target, int count) {
		Pixmap[] result = new Pixmap[count];
		int width = target.getWidth();
		int height = target.getHeight() / count;
		
		for(int i = 0; i < count; ++i) {
			result[i] = new Pixmap(width, height, Format.RGBA8888);
			result[i].drawPixmap(target, 0, 0, 0, i * height, width, height);
		}
		
		return result;
	}
	
	public static Pixmap resizeCanvas(Pixmap pixmap, int width, int height) {
		Pixmap result = new Pixmap(width, height, pixmap.getFormat());
		result.drawPixmap(pixmap, 0, 0, 0, 0, Math.min(pixmap.getWidth(), width), Math.min(pixmap.getHeight(), height));
		return result;
	}
	
	public static Pixmap rotatePixmap(Pixmap pixmap, float degrees) {
		Pixmap result = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
		
		float cos = MathUtils.cosDeg(degrees);
		float sin = MathUtils.sinDeg(degrees);
		
		for(int i = 0; i < pixmap.getWidth(); ++i) {
			for(int j = 0; j < pixmap.getHeight(); ++j) {
				int srcX = (int)(cos * i + sin * j);
				int srcY = (int)(-sin * i + cos * j);
				
				result.drawPixel(i, j, pixmap.getPixel(srcX, srcY));
			}
		}
		
		return result;
	}
	
	public static Pixmap flipPixmap(Pixmap pixmap, boolean flipX, boolean flipY) {
		Pixmap result = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());
		
		for(int i = 0; i < pixmap.getWidth(); ++i) {
			for(int j = 0; j < pixmap.getHeight(); ++j) {
				int srcX = i;
				int srcY = j;
				
				if(flipX) srcX = pixmap.getWidth() - srcX - 1;
				if(flipY) srcY = pixmap.getHeight() - srcY - 1;
				
				result.drawPixel(i, j, pixmap.getPixel(srcX, srcY));
			}
		}
		
		return result;
	}
}
