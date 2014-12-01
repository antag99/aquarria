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

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;

public class Utils {
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

	public static String replaceFormat(String formatString, ObjectMap<String, String> replacements, String defaultReplacement) {
		StringBuilder result = new StringBuilder();
		StringBuilder replacementName = new StringBuilder();
		boolean inFormat = false;

		for (int i = 0; i < formatString.length(); ++i) {
			char ch = formatString.charAt(i);
			if (ch == '%') {
				if (inFormat) {
					if (replacementName.length() == 0) {
						result.append('%');
					} else {
						String replacementNameAsString = replacementName.toString();
						String replacement = replacements.get(replacementNameAsString);
						if (replacement == null) {
							if (defaultReplacement == null) {
								throw new RuntimeException("No replacement for " + replacementNameAsString);
							} else {
								replacement = defaultReplacement;
							}
						}
						result.append(replacement);
						replacementName.setLength(0);
					}
				}

				inFormat = !inFormat;
			} else {
				if (inFormat) {
					replacementName.append(ch);
				} else {
					result.append(ch);
				}
			}
		}

		if (inFormat) {
			throw new RuntimeException("Unended format");
		}

		return result.toString();
	}
}
