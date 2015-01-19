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
package com.github.antag99.aquarria;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.json.JsonObject;

/**
 * Stores all game assets to avoid throwing around a single {@link AssetLoader} or something similar.
 */
public class Assets {
	public static SpriteGrid playerGrid;

	public static SpriteSheet playerHairSheet;
	public static SpriteSheet playerHeadSheet;
	public static SpriteSheet playerEyesSheet;
	public static SpriteSheet playerEyeWhitesSheet;
	public static SpriteSheet playerHandsSheet;
	public static SpriteSheet playerPantsSheet;
	public static SpriteSheet playerUndershirtSheet;
	public static SpriteSheet playerShirtSheet;
	public static SpriteSheet playerShoesSheet;

	public static SpriteAnimation playerWalk;
	public static SpriteAnimation playerJump;
	public static SpriteAnimation playerStand;
	public static SpriteAnimation playerSwing;

	private static void loadPlayerAssets() {
		playerGrid = new SpriteGrid(1, 20/* spriteCountX, spriteCountY */,
				0, 0/* spriteOffsetX, spriteOffsetY */,
				40, 56/* spriteWidth, spriteHeight */,
				0, 0/* spriteSpacingX, spriteSpacingY */);

		playerHairSheet = new SpriteSheet(getTexture("images/player/hair.png"), playerGrid) {
			@Override
			public TextureRegion getSprite(int x, int y) {
				// Hair frames are offset by 6 to avoid duplication
				return super.getSprite(x, Math.max(y - 6, 0));
			}
		};
		playerHeadSheet = new SpriteSheet(getTexture("images/player/head.png"), playerGrid);
		playerEyesSheet = new SpriteSheet(getTexture("images/player/eyes.png"), playerGrid);
		playerEyeWhitesSheet = new SpriteSheet(getTexture("images/player/eyeWhites.png"), playerGrid);
		playerHandsSheet = new SpriteSheet(getTexture("images/player/hands.png"), playerGrid);
		playerUndershirtSheet = new SpriteSheet(getTexture("images/player/undershirt.png"), playerGrid);
		playerShirtSheet = new SpriteSheet(getTexture("images/player/shirt.png"), playerGrid);
		playerPantsSheet = new SpriteSheet(getTexture("images/player/pants.png"), playerGrid);
		playerShoesSheet = new SpriteSheet(getTexture("images/player/shoes.png"), playerGrid);

		playerWalk = new SpriteAnimation(0, 6/* frameStartX, frameStartY */, 13/* frameCount */, 5.2f /* animationTime */);
		playerJump = new SpriteAnimation(0, 5/* frameStartX, frameStartY */, 1/* frameCount */, 1f /* animationTime */);
		playerStand = new SpriteAnimation(0, 0/* frameStartX, frameStartY */, 1/* frameCount */, 1f /* animationTime */);
		playerSwing = new SpriteAnimation(0, 1/* frameStartX, frameStartY */, 5/* frameCount */, 1f /* animationTime */);
	}

	public static SpriteGrid createSpriteGrid(JsonObject config) {
		int spriteOffsetX = config.getInteger("spriteOffsetX", 0);
		int spriteOffsetY = config.getInteger("spriteOffsetY", 0);
		int spriteSpacingX = config.getInteger("spriteSpacingX", 0);
		int spriteSpacingY = config.getInteger("spriteSpacingY", 0);
		int spriteWidth = config.getInteger("spriteWidth");
		int spriteHeight = config.getInteger("spriteHeight");
		int spriteCountX = config.getInteger("spriteCountX", 1);
		int spriteCountY = config.getInteger("spriteCountY", 1);

		return new SpriteGrid(
				spriteCountX, spriteCountY,
				spriteOffsetX, spriteOffsetY,
				spriteWidth, spriteHeight,
				spriteSpacingX, spriteSpacingY);
	}

	public static SpriteSheet createSpriteSheet(String configPath) {
		return createSpriteSheet(Util.parseJson(findFile(configPath)));
	}

	public static SpriteSheet createSpriteSheet(JsonObject config) {
		TextureRegion texture = Assets.getTexture(config.getString("texture"));

		int renderOffsetX = config.getInteger("renderOffsetX", 0);
		int renderOffsetY = config.getInteger("renderOffsetY", 0);
		int renderWidth = config.getInteger("renderWidth", 0);
		int renderHeight = config.getInteger("renderHeight", 0);

		SpriteGrid grid = createSpriteGrid(config.getObject("grid"));

		SpriteSheet sheet = new SpriteSheet(texture, grid);
		sheet.setRenderOffsetX(renderOffsetX);
		sheet.setRenderOffsetY(renderOffsetY);
		sheet.setRenderWidth(renderWidth);
		sheet.setRenderHeight(renderHeight);

		return sheet;
	}

	private static PixmapPacker texturePacker;
	private static TextureAtlas textureAtlas;
	private static FileHandle terrariaAssets;

	static void initialize() {
		texturePacker = new PixmapPacker(2048, 2048, Format.RGBA8888, 2, false);
		textureAtlas = new TextureAtlas();

		terrariaAssets = Gdx.files.local("assets-terraria");

		loadPlayerAssets();
	}

	static void dispose() {
		textureAtlas.dispose();
	}

	public static FileHandle findFile(String path) {
		FileHandle result = terrariaAssets.child(path);
		if (result.exists()) {
			return result;
		}
		return Gdx.files.internal(path);
	}

	public static TextureRegion getTexture(String path) {
		TextureRegion texture = textureAtlas.findRegion(path);
		if (texture == null) {
			Pixmap pixmap = new Pixmap(findFile(path));
			texturePacker.pack(path, pixmap);
			pixmap.dispose();
			texturePacker.updateTextureAtlas(textureAtlas,
					TextureFilter.Nearest, TextureFilter.Nearest, false);
			texture = textureAtlas.findRegion(path);
		}
		return texture;
	}
}
