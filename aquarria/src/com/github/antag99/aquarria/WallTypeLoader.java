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
package com.github.antag99.aquarria;

import org.luaj.vm2.LuaFunction;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.tile.ScriptFrameStyle;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.util.Utils;

public class WallTypeLoader extends TypeLoader<WallType> {
	public WallTypeLoader() {
		super("wall", WallType.class);
	}

	@Override
	public void load(WallType type, JsonValue config) {
		type.setDisplayName(config.getString("displayName", ""));

		if (config.has("frame")) {
			type.setFrame(Utils.asRectangle(config.get("frame")));
		}

		String frameScript = config.getString("style", "wallFrame") + ".lua";
		LuaFunction frameFunction = GameRegistry.getGlobals().loadfile(frameScript).call().checkfunction();
		type.setStyle(new ScriptFrameStyle(frameFunction));
	}

	@Override
	public void loadAssets(WallType type, JsonValue config, AssetManager assetManager) {
		if (config.has("skin")) {
			assetManager.load(config.getString("skin"), TextureAtlas.class);
		}
	}

	@Override
	public void getAssets(WallType type, JsonValue config, AssetManager assetManager) {
		if (config.has("skin")) {
			TextureAtlas atlas = assetManager.get(config.getString("skin"), TextureAtlas.class);
			type.setAtlas(atlas);
		}
	}

	@Override
	public void postLoad(WallType type, JsonValue config) {
		if (config.has("drop")) {
			type.setDrop(GameRegistry.getItemType(config.getString("drop")));
		}
	}
}
