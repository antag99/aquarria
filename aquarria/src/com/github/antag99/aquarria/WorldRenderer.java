package com.github.antag99.aquarria;

import static com.badlogic.gdx.math.MathUtils.ceil;
import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.floor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;


public class WorldRenderer extends Widget {
	
	private WorldView view;
	
	public WorldRenderer() {
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		OrthographicCamera cam = view.getCamera();
		batch.setProjectionMatrix(cam.combined);
		World world = view.getWorld();
		
		float width = cam.viewportWidth * cam.zoom;
		float height = cam.viewportHeight * cam.zoom;
		
		int startX = clamp(floor(cam.position.x - width), 0, world.getWidth() - 1);
		int startY = clamp(floor(cam.position.y - height), 0, world.getHeight() - 1);
		
		int endX = clamp(ceil(cam.position.x + width), 0, world.getWidth());
		int endY = clamp(ceil(cam.position.y + height), 0, world.getHeight());
		
		for(int i = startX; i < endX; ++i) {
			for(int j = startY; j < endY; ++j) {
				TileType type = world.getTileType(i, j);
				TextureRegion texture = type.getTexture();
				
				if(texture != null) {
					batch.draw(texture, i, j, 1f, 1f);
				}
			}
		}
	}
	
	public WorldView getView() {
		return view;
	}
	
	public void setView(WorldView view) {
		this.view = view;
	}
}
