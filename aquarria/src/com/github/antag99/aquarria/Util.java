package com.github.antag99.aquarria;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class Util {
	public static JsonValue readJson(FileHandle file) {
		return new JsonReader().parse(file);
	}

	public static void writeJson(JsonValue value, FileHandle file) {
		file.writeString(value.prettyPrint(OutputType.json, 0), false);
	}

	public static TextureRegion[] splitVertically(TextureRegion target, int count) {
		TextureRegion[] result = new TextureRegion[count];
		int width = target.getRegionWidth();
		int height = target.getRegionHeight() / count;

		for (int i = 0; i < count; ++i) {
			result[i] = new TextureRegion(target, 0, i * height, width, height);
		}

		return result;
	}

	public static Pixmap[] splitVertically(Pixmap target, int count) {
		Pixmap[] result = new Pixmap[count];
		int width = target.getWidth();
		int height = target.getHeight() / count;

		for (int i = 0; i < count; ++i) {
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

		for (int i = 0; i < pixmap.getWidth(); ++i) {
			for (int j = 0; j < pixmap.getHeight(); ++j) {
				int srcX = (int) (cos * i + sin * j);
				int srcY = (int) (-sin * i + cos * j);

				result.drawPixel(i, j, pixmap.getPixel(srcX, srcY));
			}
		}

		return result;
	}

	public static Pixmap flipPixmap(Pixmap pixmap, boolean flipX, boolean flipY) {
		Pixmap result = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), pixmap.getFormat());

		for (int i = 0; i < pixmap.getWidth(); ++i) {
			for (int j = 0; j < pixmap.getHeight(); ++j) {
				int srcX = i;
				int srcY = j;

				if (flipX)
					srcX = pixmap.getWidth() - srcX - 1;
				if (flipY)
					srcY = pixmap.getHeight() - srcY - 1;

				result.drawPixel(i, j, pixmap.getPixel(srcX, srcY));
			}
		}

		return result;
	}

	public static void forceLoad(Class<?> clazz) {
		if (clazz == null) {
			throw new NullPointerException();
		}

		try {
			Class.forName(clazz.getName(), true, clazz.getClassLoader());
		} catch (Exception ex) {
			throw new AssertionError(ex);
		}
	}

	public static Rectangle asRectangle(JsonValue value) {
		if (!value.isArray()) {
			throw new IllegalArgumentException("array expected");
		}

		float[] bounds = value.asFloatArray();

		if (bounds.length != 4) {
			throw new IllegalArgumentException("Improper array length, expected 4 elements: "
					+ "x, y, width, height; got " + bounds.length + " elements.");
		}

		return new Rectangle(bounds[0], bounds[1], bounds[2], bounds[3]);
	}
}
