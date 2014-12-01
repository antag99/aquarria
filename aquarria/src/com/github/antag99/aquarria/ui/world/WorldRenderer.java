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
package com.github.antag99.aquarria.ui.world;

import static com.badlogic.gdx.math.MathUtils.ceil;
import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.floor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.tile.FrameStyle;
import com.github.antag99.aquarria.tile.FrameStyle.Frame;
import com.github.antag99.aquarria.tile.FrameStyle.FrameSkin;
import com.github.antag99.aquarria.tile.WallType;
import com.github.antag99.aquarria.world.LightManager;
import com.github.antag99.aquarria.world.LiquidManager;
import com.github.antag99.aquarria.world.World;
import com.github.antag99.aquarria.world.WorldView;

public class WorldRenderer extends Widget {
	private WorldView view;
	private boolean drawTileGrid = false;
	private boolean drawEntityBoxes = false;
	private ShapeRenderer shapeRenderer = null;
	private TextureRegion lightTexture;
	private TextureRegion waterTopTexture;
	private TextureRegion waterFullTexture;

	@SuppressWarnings("rawtypes")
	private ObjectMap<Class, EntityRenderer> entityRenderers = new ObjectMap<Class, EntityRenderer>();

	public WorldRenderer() {
		setTouchable(Touchable.disabled);

		addEntityRenderer(new PlayerEntityRenderer());
		addEntityRenderer(new ItemEntityRenderer());
	}

	public void queueAssets(AssetManager assetManager) {
		for (EntityRenderer<?, ?> entityRenderer : entityRenderers.values()) {
			entityRenderer.queueAssets(assetManager);
		}
		assetManager.load("images/white.png", TextureRegion.class);
		assetManager.load("images/tiles/water.png", TextureRegion.class);
	}

	public void getAssets(AssetManager assetManager) {
		for (EntityRenderer<?, ?> entityRenderer : entityRenderers.values()) {
			entityRenderer.getAssets(assetManager);
		}
		lightTexture = assetManager.get("images/white.png");

		TextureRegion waterTexture = assetManager.get("images/tiles/water.png");
		waterTopTexture = new TextureRegion(waterTexture, 0, 0, waterTexture.getRegionWidth(), 4);
		waterFullTexture = new TextureRegion(waterTexture, 0, 4, waterTexture.getRegionWidth(), waterTexture.getRegionHeight() - 4);
	}

	public void addEntityRenderer(EntityRenderer<?, ?> entityRenderer) {
		entityRenderers.put(entityRenderer.getEntityClass(), entityRenderer);
	}

	public void setDrawEntityBoxes(boolean drawEntityBoxes) {
		this.drawEntityBoxes = drawEntityBoxes;
	}

	public void setDrawTileGrid(boolean drawTileGrid) {
		this.drawTileGrid = drawTileGrid;
	}

	public boolean getDrawEntityBoxes() {
		return drawEntityBoxes;
	}

	public boolean getDrawTileGrid() {
		return drawTileGrid;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		OrthographicCamera cam = view.getCamera();
		Matrix4 stageProjection = batch.getProjectionMatrix().cpy();
		batch.setProjectionMatrix(cam.combined);
		World world = view.getWorld();

		float width = cam.viewportWidth * cam.zoom;
		float height = cam.viewportHeight * cam.zoom;

		float margin = Math.max(width, height);

		int startX = clamp(floor(cam.position.x - margin), 0, world.getWidth() - 1);
		int startY = clamp(floor(cam.position.y - margin), 0, world.getHeight() - 1);

		int endX = clamp(ceil(cam.position.x + margin), 0, world.getWidth());
		int endY = clamp(ceil(cam.position.y + margin), 0, world.getHeight());

		drawWalls(batch, world, startX, startY, endX, endY);
		drawTiles(batch, world, startX, startY, endX, endY);
		drawEntities(batch, world, startX, startY, endX, endY);
		drawLiquid(batch, world, startX, startY, endX, endY);
		drawLight(batch, world, startX, startY, endX, endY);

		boolean useShapeRenderer = drawEntityBoxes || drawTileGrid;
		if (useShapeRenderer) {
			if (shapeRenderer == null) {
				shapeRenderer = new ShapeRenderer();
			}
			batch.end();
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.begin(ShapeType.Line);
		}

		if (drawEntityBoxes) {
			shapeRenderer.setColor(Color.GREEN);
			for (Entity entity : world.getEntities()) {
				shapeRenderer.rect(entity.getX(), entity.getY(),
						entity.getWidth(), entity.getHeight());
			}
		}

		if (drawTileGrid) {
			shapeRenderer.setColor(Color.RED);
			for (int i = startX; i < endX; ++i) {
				shapeRenderer.line(i, startY, i, endY);
			}
			for (int j = startY; j < endY; ++j) {
				shapeRenderer.line(startX, j, endX, j);
			}
		}

		if (useShapeRenderer) {
			shapeRenderer.end();
			batch.begin();
		}

		batch.setProjectionMatrix(stageProjection);
	}

	private void drawLiquid(Batch batch, World world, int startX, int startY, int endX, int endY) {
		LiquidManager liquidManager = world.getLiquidManager();

		batch.setColor(1f, 1f, 1f, 0.8f);
		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				int liquid = liquidManager.getLiquid(i, j);
				if (liquid != 0) {
					float liquidPercentage = liquid / 255f;

					boolean hasTopLiquid = j < world.getHeight() && (liquidManager.getLiquid(i, j + 1) != 0 || (liquid == 255 && world.getTileType(i, j + 1).isSolid()));

					if (hasTopLiquid) {
						batch.draw(waterFullTexture, i, j, 1f, liquidPercentage);
					} else {
						batch.draw(waterFullTexture, i, j, 1f, liquidPercentage - 0.25f);
						batch.draw(waterTopTexture, i, j + liquidPercentage - 0.25f, 1f, 0.25f);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void drawEntities(Batch batch, World world, int startX, int startY, int endX, int endY) {
		for (Entity entity : world.getEntities()) {
			if (entity.isActive()) {
				batch.setColor(Color.WHITE);

				EntityRenderer entityRenderer = entityRenderers.get(entity.getClass());

				entityRenderer.renderEntity(batch, entity.getView());
			}
		}
	}

	private void drawTiles(Batch batch, World world, int startX, int startY, int endX, int endY) {
		batch.setColor(Color.WHITE);

		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				world.getTileType(i, j).getRenderer().drawTile(batch, world, i, j);
			}
		}
	}

	private void drawWalls(Batch batch, World world, int startX, int startY, int endX, int endY) {
		batch.setColor(Color.WHITE);

		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				WallType type = world.getWallType(i, j);
				FrameSkin skin = type.getSkin();

				if (skin != null) {
					Frame frame = FrameStyle.wall.findFrame(world, i, j);
					TextureRegion texture = skin.getFrameTexture(frame);
					batch.draw(texture, i - 0.5f, j - 0.5f, 2f, 2f);
				}
			}
		}
	}

	private void drawLight(Batch batch, World world, int startX, int startY, int endX, int endY) {
		LightManager lightManager = world.getLightManager();
		lightManager.computeLight(startX, startY, endX - startX, endY - startY);

		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				float light = lightManager.getLight(i, j);

				float topLeftLight = i > 0 && j + 1 < world.getHeight() ? lightManager.getLight(i - 1, j + 1) : light;
				float bottomLeftLight = i > 0 && j > 0 ? lightManager.getLight(i - 1, j - 1) : light;
				float bottomRightLight = j > 0 && i + 1 < world.getWidth() ? lightManager.getLight(i + 1, j - 1) : light;
				float topRightLight = i + 1 < world.getWidth() && j + 1 < world.getHeight() ? lightManager.getLight(i + 1, j + 1) : light;

				drawGradient(batch, i, j, 1f, 1f,
						Color.toFloatBits(0f, 0f, 0f, (1f - topLeftLight) * (1f - light)),
						Color.toFloatBits(0f, 0f, 0f, (1f - topRightLight) * (1f - light)),
						Color.toFloatBits(0f, 0f, 0f, (1f - bottomLeftLight) * (1f - light)),
						Color.toFloatBits(0f, 0f, 0f, (1f - bottomRightLight) * (1f - light)));
			}
		}
	}

	final float[] vertices = new float[20];

	// http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=9361#p42550
	private void drawGradient(Batch batch,
			float x, float y, float width, float height,
			float topLeftColor, float topRightColor,
			float bottomLeftColor, float bottomRightColor) {
		int idx = 0;
		float u = lightTexture.getU();
		float v = lightTexture.getV2();
		float u2 = lightTexture.getU2();
		float v2 = lightTexture.getV();

		// bottom left
		vertices[idx++] = x;
		vertices[idx++] = y;
		vertices[idx++] = bottomLeftColor;
		vertices[idx++] = u;
		vertices[idx++] = v;

		// top left
		vertices[idx++] = x;
		vertices[idx++] = y + height;
		vertices[idx++] = topLeftColor;
		vertices[idx++] = u;
		vertices[idx++] = v2;

		// top right
		vertices[idx++] = x + width;
		vertices[idx++] = y + height;
		vertices[idx++] = topRightColor;
		vertices[idx++] = u2;
		vertices[idx++] = v2;

		// bottom right
		vertices[idx++] = x + width;
		vertices[idx++] = y;
		vertices[idx++] = bottomRightColor;
		vertices[idx++] = u2;
		vertices[idx++] = v;

		batch.draw(lightTexture.getTexture(), vertices, 0, vertices.length);
	}

	public WorldView getView() {
		return view;
	}

	public void setView(WorldView view) {
		this.view = view;
	}
}
