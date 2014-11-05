package com.github.antag99.aquarria.ui.world;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.aquarria.entity.EntityView;
import com.github.antag99.aquarria.entity.PlayerEntity;

public class PlayerEntityRenderer extends EntityRenderer<PlayerEntity, EntityView<PlayerEntity>> {
	private TextureRegion playerTexture;
	
	public PlayerEntityRenderer() {
		super(PlayerEntity.class);
	}
	
	@Override
	public void queueAssets(AssetManager assetManager) {
		assetManager.load("images/white.png", TextureRegion.class);
	}
	
	@Override
	public void getAssets(AssetManager assetManager) {
		playerTexture = assetManager.get("images/white.png");
	}

	@Override
	public void renderEntity(Batch batch, EntityView<PlayerEntity> view) {
		PlayerEntity player = view.getEntity();
		
		batch.setColor(Color.GREEN);
		batch.draw(playerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());
	}
}
