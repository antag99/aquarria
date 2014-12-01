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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.antag99.aquarria.AbstractType;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.tile.FrameStyle.FrameSkin;
import com.github.antag99.aquarria.ui.world.TileRenderer;

public class TileType extends AbstractType {
	public static Array<TileType> getTileTypes() {
		return AbstractType.getTypes(TileType.class);
	}

	public static TileType forName(String internalName) {
		return AbstractType.forName(TileType.class, internalName);
	}

	public static final TileType air = new TileType("tiles/air.json");
	public static final TileType dirt = new DropTileType("tiles/dirt.json");
	public static final TileType stone = new DropTileType("tiles/stone.json");
	public static final TileType grass = new DropTileType("tiles/grass.json");

	private String displayName;
	private boolean solid;
	private String skinPath;
	private FrameStyle style;
	private FrameSkin skin;
	private TileRenderer renderer;

	public TileType(String path) {
		this(new JsonReader().parse(Gdx.files.internal(path)));
	}

	public TileType(JsonValue properties) {
		super(properties.getString("internalName"));

		displayName = properties.getString("displayName", "");
		skinPath = properties.getString("skin", null);
		solid = properties.getBoolean("solid", true);
		style = FrameStyle.forName(properties.getString("style", "block"));
		renderer = TileRenderer.forName(properties.getString("renderer", "normal"));
	}

	public void destroyed(PlayerEntity player, int x, int y) {
	}

	public String getDisplayName() {
		return displayName;
	}

	public boolean isSolid() {
		return solid;
	}

	public FrameSkin getSkin() {
		return skin;
	}

	public FrameStyle getStyle() {
		return style;
	}

	public TileRenderer getRenderer() {
		return renderer;
	}

	@Override
	protected void queueAssets(AssetManager assetManager) {
		if (skinPath != null) {
			assetManager.load(skinPath, TextureAtlas.class);
		}
	}

	@Override
	protected void getAssets(AssetManager assetManager) {
		if (skinPath != null) {
			skin = new FrameSkin(assetManager.get(skinPath, TextureAtlas.class));
		}
	}

	@Override
	protected Class<? extends AbstractType> getTypeClass() {
		return TileType.class;
	}
}
