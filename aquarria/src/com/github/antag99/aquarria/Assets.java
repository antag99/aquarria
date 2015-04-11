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

/**
 * Stores all game assets to avoid throwing around a single {@link AssetLoader} or something similar.
 */
public class Assets {
	public static SpriteGrid playerGrid;
	public static SpriteGrid tileGrid;
	public static SpriteGrid wallGrid;

	public static SpriteGrid trunkGrid;
	public static SpriteGrid topsGrid;
	public static SpriteGrid branchesGrid;

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
		playerGrid = new SpriteGrid(
				1, 20/* count */,
				0, 0/* offset */,
				40, 56/* size */,
				0, 0/* spacing */,
				0, 0/* draw offset */,
				40, 56/* draw size */);

		playerHairSheet = new SpriteSheet(getTexture("images/player/hair.png"), playerGrid) {
			@Override
			public Sprite getSprite(int x, int y) {
				// Hair frames are offset by -6; the first 7 frames are packed into one.
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

		playerWalk = new SpriteAnimation(6, 13, 5.2f);
		playerJump = new SpriteAnimation(5, 1, 1f);
		playerStand = new SpriteAnimation(0, 1, 1f);
		playerSwing = new SpriteAnimation(1, 5, 1f);

		tileGrid = new SpriteGrid(16, 10, 0, 0, 16, 16, 2, 2, 0, 0, 16, 16);
		wallGrid = new SpriteGrid(16, 10, 0, 0, 32, 32, 4, 4, -8, -8, 32, 32);

		trunkGrid = new SpriteGrid(8, 12, 0, 0, 20, 20, 2, 2, -2, -2, 20, 20);
		topsGrid = new SpriteGrid(3, 1, 0, 0, 80, 80, 2, 2, -32, 0, 80, 80);
		branchesGrid = new SpriteGrid(2, 3, 0, 0, 40, 40, 2, 2, -2, -10, 40, 40);
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

	public static Sprite getSprite(String path) {
		// TODO: Add support for sprite .json files
		TextureRegion texture = getTexture(path);
		return new Sprite(texture, 0f, 0f, texture.getRegionWidth(), texture.getRegionHeight());
	}
}
