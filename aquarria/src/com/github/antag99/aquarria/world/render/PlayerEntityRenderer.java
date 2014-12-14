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
package com.github.antag99.aquarria.world.render;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.entity.PlayerView;
import com.github.antag99.aquarria.item.Item;
import com.github.antag99.aquarria.ui.IngameScreen;
import com.github.antag99.aquarria.util.Utils;

public class PlayerEntityRenderer extends EntityRenderer<PlayerEntity, PlayerView> {
	private TextureRegion[] hairFrames;
	private TextureRegion[] headFrames;
	private TextureRegion[] eyesFrames;
	private TextureRegion[] eyeWhitesFrames;
	private TextureRegion[] shirtFrames;
	private TextureRegion[] undershirtFrames;
	private TextureRegion[] pantsFrames;
	private TextureRegion[] shoesFrames;

	public PlayerEntityRenderer() {
		super(PlayerEntity.class);
	}

	@Override
	public void queueAssets(AssetManager assetManager) {
		assetManager.load("images/player/hair.png", TextureRegion.class);
		assetManager.load("images/player/head.png", TextureRegion.class);
		assetManager.load("images/player/eyes.png", TextureRegion.class);
		assetManager.load("images/player/eyeWhites.png", TextureRegion.class);
		assetManager.load("images/player/shirt.png", TextureRegion.class);
		assetManager.load("images/player/undershirt.png", TextureRegion.class);
		assetManager.load("images/player/pants.png", TextureRegion.class);
		assetManager.load("images/player/shoes.png", TextureRegion.class);
	}

	@Override
	public void getAssets(AssetManager assetManager) {
		hairFrames = Utils.splitVertically(assetManager.get("images/player/hair.png", TextureRegion.class), 14);
		headFrames = Utils.splitVertically(assetManager.get("images/player/head.png", TextureRegion.class), 20);
		eyesFrames = Utils.splitVertically(assetManager.get("images/player/eyes.png", TextureRegion.class), 20);
		eyeWhitesFrames = Utils.splitVertically(assetManager.get("images/player/eyeWhites.png", TextureRegion.class), 20);
		shirtFrames = Utils.splitVertically(assetManager.get("images/player/shirt.png", TextureRegion.class), 20);
		undershirtFrames = Utils.splitVertically(assetManager.get("images/player/undershirt.png", TextureRegion.class), 20);
		pantsFrames = Utils.splitVertically(assetManager.get("images/player/pants.png", TextureRegion.class), 20);
		shoesFrames = Utils.splitVertically(assetManager.get("images/player/shoes.png", TextureRegion.class), 20);
	}

	@Override
	public void renderEntity(Batch batch, PlayerView view) {
		PlayerEntity player = view.getEntity();

		float centerX = player.getX() + player.getWidth() / 2f;
		float centerY = player.getY() + player.getHeight() / 2f;

		float scaleX = 40f / (player.getWidth() * IngameScreen.PIXELS_PER_METER);
		float scaleY = 56f / (player.getHeight() * IngameScreen.PIXELS_PER_METER);

		float x = centerX - player.getWidth() * 0.5f * scaleX;
		float y = centerY - player.getHeight() * 0.5f * scaleY;

		float width = player.getWidth() * scaleX;
		float height = player.getHeight() * scaleY;

		boolean flip = player.getDirection() == -1;

		batch.setColor(255 / 255f, 125 / 255f, 90 / 255f, 1f);
		batch.draw(setFlip(headFrames[view.getBodyFrame()], flip), x, y, width, height);
		batch.setColor(105 / 255f, 90 / 255f, 75 / 255f, 1f);
		batch.draw(setFlip(eyesFrames[view.getBodyFrame()], flip), x, y, width, height);
		batch.setColor(Color.WHITE);
		batch.draw(setFlip(eyeWhitesFrames[view.getBodyFrame()], flip), x, y, width, height);
		batch.setColor(255 / 255f, 230 / 255f, 175 / 255f, 1f);
		batch.draw(setFlip(pantsFrames[view.getLegFrame()], flip), x, y, width, height);
		batch.setColor(160 / 255f, 180 / 255f, 215 / 255f, 1f);
		batch.draw(setFlip(undershirtFrames[view.getBodyFrame()], flip), x, y, width, height);
		batch.setColor(215 / 255f, 90 / 255f, 55 / 255f, 1f);
		batch.draw(setFlip(hairFrames[Math.max(view.getBodyFrame() - 6, 0)], flip), x, y, width, height);
		batch.setColor(175 / 255f, 165 / 255f, 140 / 255f, 1f);
		batch.draw(setFlip(shirtFrames[view.getBodyFrame()], flip), x, y, width, height);
		batch.setColor(160 / 255f, 105 / 255f, 60 / 255f, 1f);
		batch.draw(setFlip(shoesFrames[view.getLegFrame()], flip), x, y, width, height);

		if (player.isUsingItem()) {
			Item item = player.getUsedItem();
			TextureRegion itemTexture = item.getType().getTexture();
			float itemOffsetX = view.getUseOffsetX();
			float itemOffsetY = view.getUseOffsetY();
			float itemRotation = view.getUseRotation();

			batch.setColor(Color.WHITE);
			batch.draw(itemTexture, player.getX() + itemOffsetX, player.getY() + itemOffsetY, 0f, 0f,
					item.getType().getWidth() / IngameScreen.PIXELS_PER_METER,
					item.getType().getHeight() / IngameScreen.PIXELS_PER_METER,
					1f, 1f, itemRotation);
		}
	}

	private TextureRegion setFlip(TextureRegion texture, boolean flip) {
		if (texture.isFlipX() != flip)
			texture.flip(true, false);
		return texture;
	}
}
