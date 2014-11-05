package com.github.antag99.aquarria.ui.world;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.Utils;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.entity.PlayerView;
import com.github.antag99.aquarria.ui.IngameScreen;

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
		hairFrames = Utils.splitVertically(assetManager.get("images/player/hair.png"), 14);
		headFrames = Utils.splitVertically(assetManager.get("images/player/head.png"), 20);
		eyesFrames = Utils.splitVertically(assetManager.get("images/player/eyes.png"), 20);
		eyeWhitesFrames = Utils.splitVertically(assetManager.get("images/player/eyeWhites.png"), 20);
		shirtFrames = Utils.splitVertically(assetManager.get("images/player/shirt.png"), 20);
		undershirtFrames = Utils.splitVertically(assetManager.get("images/player/undershirt.png"), 20);
		pantsFrames = Utils.splitVertically(assetManager.get("images/player/pants.png"), 20);
		shoesFrames = Utils.splitVertically(assetManager.get("images/player/shoes.png"), 20);
	}

	@Override
	public void renderEntity(Batch batch, PlayerView view) {
		PlayerEntity player = view.getEntity();
		
		float x = player.getX();
		float y = player.getY() - 2f / IngameScreen.PIXELS_PER_METER;
		float width = player.getWidth();
		float height = player.getHeight();
		
		batch.setColor(Color.YELLOW);
		batch.draw(headFrames[view.getBodyFrame()], x, y, width, height);
		batch.setColor(Color.GREEN);
		batch.draw(hairFrames[Math.max(view.getBodyFrame() - 6, 0)], x, y, width, height);
		batch.setColor(Color.BLACK);
		batch.draw(eyesFrames[view.getBodyFrame()], x, y, width, height);
		batch.setColor(Color.WHITE);
		batch.draw(eyeWhitesFrames[view.getBodyFrame()], x, y, width, height);
		batch.setColor(Color.RED);
		batch.draw(shirtFrames[view.getBodyFrame()], x, y, width, height);
		batch.draw(undershirtFrames[view.getBodyFrame()], x, y, width, height);
		batch.setColor(Color.BLUE);
		batch.draw(pantsFrames[view.getLegFrame()], x, y, width, height);
		batch.setColor(Color.BLACK);
		batch.draw(shoesFrames[view.getLegFrame()], x, y, width, height);
	}
}
