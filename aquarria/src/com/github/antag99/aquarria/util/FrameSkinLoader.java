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

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.tile.Frame;
import com.github.antag99.aquarria.tile.FrameSkin;
import com.github.antag99.aquarria.util.FrameSkinLoader.FrameSkinParameter;

/**
 * Loads a {@link FrameSkin} from a specified directory as the asset file,
 * with a {@link FrameSkinParameter} pointing to the configuration file.
 */
public class FrameSkinLoader extends SynchronousAssetLoader<FrameSkin, FrameSkinParameter> {
	public FrameSkinLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	public static class FrameSkinParameter extends AssetLoaderParameters<FrameSkin> {
		public String configFile;

		public FrameSkinParameter() {
		}

		public FrameSkinParameter(String configFile) {
			this.configFile = configFile;
		}
	}

	@Override
	public FrameSkin load(AssetManager assetManager, String fileName, FileHandle file, FrameSkinParameter parameter) {
		JsonValue config = new JsonReader().parse(resolve(parameter.configFile));
		FrameSkin skin = new FrameSkin();
		for (JsonValue frame : config) {
			// Entries that start with a '~' are ignored as they
			// are used as templates for other entries to minimize
			// configuration duplication for similar frames.
			if (!frame.name.startsWith("~")) {
				JsonValue value = frame;

				// Entries can use other entries configuration.
				// This is only supported in one step, however.
				if (frame.isString()) {
					value = config.get(frame.asString());
				}

				String frameName = value.getString("name", frame.name);
				TextureRegion texture = assetManager.get(file.child(frame.name + ".png").path());
				float[] offset = value.get("offset").asFloatArray();
				float[] size = value.get("size").asFloatArray();
				skin.addFrame(frameName, new Frame(texture, offset[0], offset[1], size[0], size[1]));
			}
		}
		return skin;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, FrameSkinParameter parameter) {
		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		JsonValue config = new JsonReader().parse(resolve(parameter.configFile));
		for (JsonValue image : config) {
			if (!image.name.startsWith("~")) {
				dependencies.add(new AssetDescriptor<TextureRegion>(file.child(image.name + ".png"), TextureRegion.class));
			}
		}
		return dependencies;
	}
}
