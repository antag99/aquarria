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
package com.github.antag99.aquarria.tile;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.GameRegistry;
import com.github.antag99.aquarria.TypeLoader;
import com.github.antag99.aquarria.lua.LuaObject;
import com.github.antag99.aquarria.util.FrameSkinLoader.FrameSkinParameter;

public class TileTypeLoader extends TypeLoader<TileType> {
	public TileTypeLoader() {
		super("tile", TileType.class);
	}

	@Override
	public void load(TileType type, JsonValue config) {
		type.setDisplayName(config.getString("displayName", ""));
		type.setSolid(config.getBoolean("solid", true));
		String frameScript = config.getString("style", "blockFrame") + ".lua";
		LuaObject frameFunction = GameRegistry.getLuaEnvironment().loadScript(frameScript).call().get(0);
		type.setStyle(new ScriptFrameStyle(frameFunction));
	}

	@Override
	public void postLoad(TileType type, JsonValue config) {
		if (config.has("drop")) {
			type.setDrop(GameRegistry.getItemType(config.getString("drop")));
		}
	}

	@Override
	public void loadAssets(TileType type, JsonValue config, AssetManager assetManager) {
		if (config.has("skin") && config.has("skinDirectory")) {
			FrameSkinParameter param = new FrameSkinParameter(config.getString("skin"));
			assetManager.load(config.getString("skinDirectory"), FrameSkin.class, param);
		}
	}

	@Override
	public void getAssets(TileType type, JsonValue config, AssetManager assetManager) {
		if (config.has("skin") && config.has("skinDirectory")) {
			FrameSkin skin = assetManager.get(config.getString("skinDirectory"), FrameSkin.class);
			type.setSkin(skin);
		}
	}
}
