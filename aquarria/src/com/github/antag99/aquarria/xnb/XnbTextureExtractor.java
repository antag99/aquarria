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

		if (!primaryType.equals("Texture2D")) {
			throw new RuntimeException("Expected primary type to be Texture2D, was " + primaryType);
		}

		int surfaceFormat = buffer.getInt();
		int width = buffer.getInt();
		int height = buffer.getInt();

		if (surfaceFormat != COLOR) {
			throw new RuntimeException("Expected surface format to be COLOR, was " + surfaceFormat);
		}

		int mipCount = buffer.getInt();
		int size = buffer.getInt();

		if (mipCount != 1) {
			throw new RuntimeException("Invalid mipmap count: " + mipCount);
		}

		if (size != width * height * 4) {
			throw new RuntimeException("Invalid size: " + size + "(width=" + width + ", height=" + height + ")");
		}

		// To read RGBA values as integers
		buffer.order(ByteOrder.BIG_ENDIAN);

		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				pixmap.drawPixel(x, y, buffer.getInt());
			}
		}
		PixmapIO.writePNG(dest, pixmap);
		pixmap.dispose();
	}
}
