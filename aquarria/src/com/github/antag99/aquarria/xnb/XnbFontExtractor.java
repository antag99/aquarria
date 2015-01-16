/*******************************************************************************
 * Copyright (c) 2014-2015, Anton Gustafsson
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

		if (!primaryType.equals("SpriteFont")) {
			throw new RuntimeException("Expected primary type to be SpriteFont, was " + primaryType);
		}

		if (get7BitEncodedInt(buffer) == 0) {
			throw new RuntimeException("Sprite font texture is null");
		} else {
			int surfaceFormat = buffer.getInt();

			int width = buffer.getInt();
			int height = buffer.getInt();

			int mipCount = buffer.getInt();
			buffer.getInt();

			if (mipCount != 1) {
				throw new RuntimeException("Invalid mipmap count: " + mipCount);
			}

			FileHandle textureFile = dest.sibling(dest.nameWithoutExtension() + ".png");

			if (surfaceFormat != SURFACEFORMAT_DXT3) {
				throw new RuntimeException("Unexpected surface format: " + surfaceFormat);
			}

			try {
				BufferedImage image = Dxt3.getBufferedImage(width, height, buffer);
				OutputStream textureFileStream = textureFile.write(false);
				ImageIO.write(image, "png", textureFileStream);
				textureFileStream.close();
			} catch (IOException ex) {
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
		for (int k = 0; k < mapSize; ++k) {
			characterMap.add(getCSharpChar(buffer));
		}

		int verticalLineSpacing = buffer.getInt();
		float horozontalSpacing = buffer.getFloat();

		List<Vector3> kerning = new ArrayList<Vector3>();
		getList(buffer, kerning, Vector3.class);

		char defaultCharacter = ' ';

		if (buffer.get() != 0) { // Boolean
			defaultCharacter = getCSharpChar(buffer);
		}

		if (glyphs.size() != cropping.size() || cropping.size() != characterMap.size()) {
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
			for (int i = 0; i < characterMap.size(); ++i) {
				jsonWriter.object(characterMap.get(i).toString());
				Rectangle glyph = glyphs.get(i);
				Rectangle crop = cropping.get(i);
				jsonWriter.set("x", (int) glyph.x);
				jsonWriter.set("y", (int) glyph.y);
				jsonWriter.set("width", (int) glyph.width);
				jsonWriter.set("height", (int) glyph.height);
				jsonWriter.set("padLeft", (int) crop.x);
				jsonWriter.set("padTop", (int) crop.y);
				jsonWriter.set("padRight", (int) crop.width);
				jsonWriter.set("padBottom", (int) crop.height);
			}
			jsonWriter.pop();
			jsonWriter.array("kerning");
			for (int i = 0; i < kerning.size(); ++i) {
				jsonWriter.object();
				Vector3 kern = kerning.get(i);
				jsonWriter.set("a", (int) kern.x);
				jsonWriter.set("b", (int) kern.y);
				jsonWriter.set("c", (int) kern.z);
				jsonWriter.pop();
			}
			jsonWriter.pop();
			jsonWriter.pop();
			jsonWriter.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
