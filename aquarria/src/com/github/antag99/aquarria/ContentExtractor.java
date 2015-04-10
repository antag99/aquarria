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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.github.antag99.aquarria.xnb.XnbExtractor;
import com.github.antag99.aquarria.xnb.XnbFontExtractor;
import com.github.antag99.aquarria.xnb.XnbSoundExtractor;
import com.github.antag99.aquarria.xnb.XnbTextureExtractor;

public class ContentExtractor {
	private FileHandle contentDirectory;
	private FileHandle outputAssetDirectory;

	public ContentExtractor(FileHandle contentDirectory, FileHandle outputAssetDirectory) {
		this.contentDirectory = contentDirectory;
		this.outputAssetDirectory = outputAssetDirectory;
	}

	/**
	 * Extracts vanilla assets. This is only called once, as the raw
	 * assets are not modified in any way. Always called if vanilla
	 * assets have not been extracted when {@link ContentExtractor#convert()} is called.
	 */
	public void extract() {
		FileHandle rawDir = outputAssetDirectory.child("raw");

		XnbExtractor textureExtractor = new XnbTextureExtractor();
		FileHandle rawImagesDir = rawDir.child("images");

		for (FileHandle image : contentDirectory.child("Images").list(".xnb")) {
			textureExtractor.extract(image, rawImagesDir.child(image.nameWithoutExtension() + ".png"));
		}

		XnbExtractor fontExtractor = new XnbFontExtractor();
		FileHandle rawFontsDir = rawDir.child("fonts");

		for (FileHandle font : contentDirectory.child("Fonts").list(".xnb")) {
			fontExtractor.extract(font, rawFontsDir.child(font.nameWithoutExtension() + ".png"));
		}

		XnbExtractor soundExtractor = new XnbSoundExtractor();
		FileHandle rawSoundDir = rawDir.child("sound");

		for (FileHandle sound : contentDirectory.child("Sounds").list(".xnb")) {
			soundExtractor.extract(sound, rawSoundDir.child(sound.nameWithoutExtension() + ".wav"));
		}
	}

	/**
	 * Converts the raw vanilla assets into a more suitable format.
	 * For example, splitting sprite sheets into the individual
	 * images and moving files to proper locations.
	 */
	public void convert() {
		if (!outputAssetDirectory.child("raw").exists()) {
			// Extract vanilla assets if not present
			extract();
		} else {
			// Delete already existing processed assets
			for (FileHandle directory : outputAssetDirectory.list()) {
				if (!directory.name().equals("raw")) {
					directory.deleteDirectory();
				}
			}
		}

		copy("images/Tiles_0.png", "images/tiles/dirt.png");
		copy("images/Tiles_1.png", "images/tiles/stone.png");
		copy("images/Tiles_2.png", "images/tiles/grass.png");
		copy("images/Tiles_5.png", "images/tiles/tree.png");
		copy("images/Tree_Tops_1.png", "images/tiles/treeTop.png");

		copy("images/Wall_2.png", "images/walls/dirt.png");
		copy("images/Wall_1.png", "images/walls/stone.png");

		copy("images/Item_1.png", "images/items/pickaxe.png");
		copy("images/Item_2.png", "images/items/dirt.png");
		copy("images/Item_3.png", "images/items/stone.png");

		copy("images/Item_7.png", "images/items/hammer.png");
		copy("images/Item_30.png", "images/items/dirtWall.png");
		copy("images/Item_26.png", "images/items/stoneWall.png");

		copy("images/Background_0.png", "images/background/sky.png");
		copy("images/Background_1.png", "images/background/dirtEdge.png");
		copy("images/Background_2.png", "images/background/dirtLayer.png");
		copy("images/Background_4.png", "images/background/caveEdge.png");
		copy("images/Background_3.png", "images/background/cave.png");
		copy("images/Background_5.png", "images/background/hell.png");
		copy("images/Background_6.png", "images/background/hellEdge.png");
		copy("images/Background_7.png", "images/background/surface.png");
		copy("images/Background_9.png", "images/background/forest.png");

		copy("images/Bubble.png", "images/ui/bubble.png");
		copy("images/CoolDown.png", "images/ui/block.png");
		copy("images/CraftButton.png", "images/ui/craft.png");
		copy("images/Cursor.png", "images/ui/cursor.png");
		copy("images/Cursor2.png", "images/ui/cursor2.png");
		copy("images/HealthBar1.png", "images/ui/healthBarFill.png");
		copy("images/HealthBar2.png", "images/ui/healthBarEmpty.png");
		copy("images/Heart.png", "images/ui/heart.png");
		copy("images/Heart2.png", "images/ui/goldHeart.png");
		copy("images/House_1.png", "images/ui/house.png");
		copy("images/House_2.png", "images/ui/disabledHouse.png");
		copy("images/House_Banner_1.png", "images/ui/npcBanner.png");
		copy("images/Inventory_Back.png", "images/ui/slot/blue.png");
		copy("images/Inventory_Back14.png", "images/ui/slot/focus.png");
		copy("images/Inventory_Tick_Off.png", "images/ui/accessoryDisabled.png");
		copy("images/Inventory_Tick_On.png", "images/ui/accessoryEnabled.png");
		copy("images/Sun.png", "images/ui/sun.png");
		copy("images/Team.png", "images/ui/team.png");
		copy("images/Lock_0.png", "images/ui/hotbarLocked.png");
		copy("images/Lock_1.png", "images/ui/hotbarUnlocked.png");
		copy("images/Mana.png", "images/ui/mana.png");

		copy("images/Player_Hair_1.png", "images/player/hair.png");
		copy("images/Player_Head.png", "images/player/head.png");
		copy("images/Player_Eyes.png", "images/player/eyes.png");
		copy("images/Player_Eye_Whites.png", "images/player/eyeWhites.png");
		copy("images/Player_Shirt.png", "images/player/shirt.png");
		copy("images/Player_Undershirt.png", "images/player/undershirt.png");
		copy("images/Player_Hands.png", "images/player/hands.png");
		copy("images/Player_Pants.png", "images/player/pants.png");
		copy("images/Player_Shoes.png", "images/player/shoes.png");

		copy("images/Liquid_0.png", "images/tiles/water.png");
		copy("images/Liquid_1.png", "images/tiles/lava.png");

		// Some images need to be padded/cropped, either because their sizes are
		// not consequent or only a portion of the image is used by the game.
		// ImageCanvasResizer playerAnimationPadder = new ImageCanvasResizer(40, 1120);
		// playerAnimationPadder.resize(outputAssetDirectory.child("images/player/head.png"));
		// playerAnimationPadder.resize(outputAssetDirectory.child("images/player/eyes.png"));
		// playerAnimationPadder.resize(outputAssetDirectory.child("images/player/eyeWhites.png"));
		// playerAnimationPadder.resize(outputAssetDirectory.child("images/player/shirt.png"));
		// playerAnimationPadder.resize(outputAssetDirectory.child("images/player/undershirt.png"));
		// playerAnimationPadder.resize(outputAssetDirectory.child("images/player/hands.png"));
		// playerAnimationPadder.resize(outputAssetDirectory.child("images/player/pants.png"));
		// playerAnimationPadder.resize(outputAssetDirectory.child("images/player/shoes.png"));
		//
		// ImageCanvasResizer liquidImageCropper = new ImageCanvasResizer(16, 16);
		// liquidImageCropper.resize(outputAssetDirectory.child("images/tiles/water.png"));
		// liquidImageCropper.resize(outputAssetDirectory.child("images/tiles/lava.png"));

		// Create texture atlas for all UI images
		createAtlas(outputAssetDirectory.child("images/ui/"), "ui");
	}

	private void copy(String rawSource, String destination) {
		outputAssetDirectory.child("raw/" + rawSource).copyTo(outputAssetDirectory.child(destination));
	}

	private void createAtlas(FileHandle directory, String atlasName) {
		Settings settings = new Settings();
		settings.minWidth = 32;
		settings.minHeight = 32;
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.pot = true;
		settings.paddingX = 0;
		settings.paddingY = 0;

		String inputDirectory = directory.path();
		String outputDirectory = directory.path();
		String packFileName = atlasName;

		TexturePacker.process(settings, inputDirectory, outputDirectory, packFileName);
	}
}
