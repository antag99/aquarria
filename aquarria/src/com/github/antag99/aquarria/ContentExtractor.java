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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.github.antag99.aquarria.util.FrameSplitter;
import com.github.antag99.aquarria.util.FrameSplitter.SplitType;
import com.github.antag99.aquarria.util.ImageCanvasResizer;
import com.github.antag99.aquarria.xnb.XnbExtractor;
import com.github.antag99.aquarria.xnb.XnbFontExtractor;
import com.github.antag99.aquarria.xnb.XnbSoundExtractor;
import com.github.antag99.aquarria.xnb.XnbTextureExtractor;

public class ContentExtractor {
	private FileHandle contentDirectory;
	private FileHandle outputAssetDirectory;

	private FrameSplitter tileSplitter = new FrameSplitter(SplitType.BLOCK);
	private FrameSplitter wallSplitter = new FrameSplitter(SplitType.WALL);
	private FrameSplitter treeSplitter = new FrameSplitter(SplitType.TREE);
	private FrameSplitter treeBranchSplitter = new FrameSplitter(SplitType.TREE_BRANCH);
	private FrameSplitter treeTopSplitter = new FrameSplitter(SplitType.TREE_TOP);

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
		// Extract XNB files to raw/
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

		FileHandle rawDir = outputAssetDirectory.child("raw");
		FileHandle rawImagesDir = rawDir.child("images");

		// Split tile & wall images into directories
		convertTileImage("Tiles_0.png", "dirt");
		convertTileImage("Tiles_1.png", "stone");
		convertTileImage("Tiles_2.png", "grass");

		convertWallImage("Wall_2.png", "dirt");
		convertWallImage("Wall_1.png", "stone");

		treeSplitter.split(rawImagesDir.child("Tiles_5.png"), outputAssetDirectory.child("images/tiles/tree/"));
		treeBranchSplitter.split(rawImagesDir.child("Tree_Branches_0.png"), outputAssetDirectory.child("images/tiles/tree/"));
		treeTopSplitter.split(rawImagesDir.child("Tree_Tops_0.png"), outputAssetDirectory.child("images/tiles/tree/"));
		createAtlas(outputAssetDirectory.child("images/tiles/tree/"), "tree");

		// Move around images, for easier maintenance
		rawImagesDir.child("Item_1.png").copyTo(outputAssetDirectory.child("images/items/pickaxe.png"));
		rawImagesDir.child("Item_2.png").copyTo(outputAssetDirectory.child("images/items/dirt.png"));
		rawImagesDir.child("Item_3.png").copyTo(outputAssetDirectory.child("images/items/stone.png"));

		rawImagesDir.child("Item_7.png").copyTo(outputAssetDirectory.child("images/items/hammer.png"));
		rawImagesDir.child("Item_30.png").copyTo(outputAssetDirectory.child("images/items/dirtWall.png"));
		rawImagesDir.child("Item_26.png").copyTo(outputAssetDirectory.child("images/items/stoneWall.png"));

		rawImagesDir.child("Background_0.png").copyTo(outputAssetDirectory.child("images/background/sky.png"));
		rawImagesDir.child("Background_1.png").copyTo(outputAssetDirectory.child("images/background/dirtEdge.png"));
		rawImagesDir.child("Background_2.png").copyTo(outputAssetDirectory.child("images/background/dirtLayer.png"));
		rawImagesDir.child("Background_4.png").copyTo(outputAssetDirectory.child("images/background/caveEdge.png"));
		rawImagesDir.child("Background_3.png").copyTo(outputAssetDirectory.child("images/background/cave.png"));
		rawImagesDir.child("Background_5.png").copyTo(outputAssetDirectory.child("images/background/hell.png"));
		rawImagesDir.child("Background_6.png").copyTo(outputAssetDirectory.child("images/background/hellEdge.png"));
		rawImagesDir.child("Background_7.png").copyTo(outputAssetDirectory.child("images/background/surface.png"));
		rawImagesDir.child("Background_9.png").copyTo(outputAssetDirectory.child("images/background/forest.png"));

		rawImagesDir.child("Bubble.png").copyTo(outputAssetDirectory.child("images/ui/bubble.png"));
		rawImagesDir.child("CoolDown.png").copyTo(outputAssetDirectory.child("images/ui/block.png"));
		rawImagesDir.child("CraftButton.png").copyTo(outputAssetDirectory.child("images/ui/craft.png"));
		rawImagesDir.child("Cursor.png").copyTo(outputAssetDirectory.child("images/ui/cursor.png"));
		rawImagesDir.child("Cursor2.png").copyTo(outputAssetDirectory.child("images/ui/cursor2.png"));
		rawImagesDir.child("HealthBar1.png").copyTo(outputAssetDirectory.child("images/ui/healthBarFill.png"));
		rawImagesDir.child("HealthBar2.png").copyTo(outputAssetDirectory.child("images/ui/healthBarEmpty.png"));
		rawImagesDir.child("Heart.png").copyTo(outputAssetDirectory.child("images/ui/heart.png"));
		rawImagesDir.child("Heart2.png").copyTo(outputAssetDirectory.child("images/ui/goldHeart.png"));
		rawImagesDir.child("House_1.png").copyTo(outputAssetDirectory.child("images/ui/house.png"));
		rawImagesDir.child("House_2.png").copyTo(outputAssetDirectory.child("images/ui/disabledHouse.png"));
		rawImagesDir.child("House_Banner_1.png").copyTo(outputAssetDirectory.child("images/ui/npcBanner.png"));
		rawImagesDir.child("Inventory_Back.png").copyTo(outputAssetDirectory.child("images/ui/slot/blue.png"));
		rawImagesDir.child("Inventory_Back14.png").copyTo(outputAssetDirectory.child("images/ui/slot/focus.png"));
		rawImagesDir.child("Inventory_Tick_Off.png").copyTo(outputAssetDirectory.child("images/ui/accessoryDisabled.png"));
		rawImagesDir.child("Inventory_Tick_On.png").copyTo(outputAssetDirectory.child("images/ui/accessoryEnabled.png"));
		rawImagesDir.child("Sun.png").copyTo(outputAssetDirectory.child("images/ui/sun.png"));
		rawImagesDir.child("Team.png").copyTo(outputAssetDirectory.child("images/ui/team.png"));
		rawImagesDir.child("Lock_0.png").copyTo(outputAssetDirectory.child("images/ui/hotbarLocked.png"));
		rawImagesDir.child("Lock_1.png").copyTo(outputAssetDirectory.child("images/ui/hotbarUnlocked.png"));
		rawImagesDir.child("Mana.png").copyTo(outputAssetDirectory.child("images/ui/mana.png"));

		rawImagesDir.child("Player_Hair_1.png").copyTo(outputAssetDirectory.child("images/player/hair.png"));
		rawImagesDir.child("Player_Head.png").copyTo(outputAssetDirectory.child("images/player/head.png"));
		rawImagesDir.child("Player_Eyes.png").copyTo(outputAssetDirectory.child("images/player/eyes.png"));
		rawImagesDir.child("Player_Eye_Whites.png").copyTo(outputAssetDirectory.child("images/player/eyeWhites.png"));
		rawImagesDir.child("Player_Shirt.png").copyTo(outputAssetDirectory.child("images/player/shirt.png"));
		rawImagesDir.child("Player_Undershirt.png").copyTo(outputAssetDirectory.child("images/player/undershirt.png"));
		rawImagesDir.child("Player_Hands.png").copyTo(outputAssetDirectory.child("images/player/hands.png"));
		rawImagesDir.child("Player_Pants.png").copyTo(outputAssetDirectory.child("images/player/pants.png"));
		rawImagesDir.child("Player_Shoes.png").copyTo(outputAssetDirectory.child("images/player/shoes.png"));

		rawImagesDir.child("Liquid_0.png").copyTo(outputAssetDirectory.child("images/tiles/water.png"));
		rawImagesDir.child("Liquid_1.png").copyTo(outputAssetDirectory.child("images/tiles/lava.png"));

		// Some images need to be padded/cropped, either because their sizes are
		// not consequent or only a portion of the image is used by the game.
		ImageCanvasResizer playerAnimationPadder = new ImageCanvasResizer(40, 1120);
		playerAnimationPadder.resize(outputAssetDirectory.child("images/player/head.png"));
		playerAnimationPadder.resize(outputAssetDirectory.child("images/player/eyes.png"));
		playerAnimationPadder.resize(outputAssetDirectory.child("images/player/eyeWhites.png"));
		playerAnimationPadder.resize(outputAssetDirectory.child("images/player/shirt.png"));
		playerAnimationPadder.resize(outputAssetDirectory.child("images/player/undershirt.png"));
		playerAnimationPadder.resize(outputAssetDirectory.child("images/player/hands.png"));
		playerAnimationPadder.resize(outputAssetDirectory.child("images/player/pants.png"));
		playerAnimationPadder.resize(outputAssetDirectory.child("images/player/shoes.png"));

		ImageCanvasResizer liquidImageCropper = new ImageCanvasResizer(16, 16);
		liquidImageCropper.resize(outputAssetDirectory.child("images/tiles/water.png"));
		liquidImageCropper.resize(outputAssetDirectory.child("images/tiles/lava.png"));

		// Create texture atlas for all UI images
		createAtlas(outputAssetDirectory.child("images/ui/"), "ui");
	}

	private void convertTileImage(String imageName, String tileName) {
		FileHandle imageFile = outputAssetDirectory.child("raw/images/" + imageName);
		FileHandle imageDirectory = outputAssetDirectory.child("images/tiles/" + tileName + "/");
		tileSplitter.split(imageFile, imageDirectory);
	}

	private void convertWallImage(String imageName, String wallName) {
		FileHandle imageFile = outputAssetDirectory.child("raw/images/" + imageName);
		FileHandle imageDirectory = outputAssetDirectory.child("images/walls/" + wallName + "/");
		wallSplitter.split(imageFile, imageDirectory);
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
