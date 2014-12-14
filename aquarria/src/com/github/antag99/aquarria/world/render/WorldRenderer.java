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

import static com.badlogic.gdx.math.MathUtils.ceil;
import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.floor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Array;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.world.World;
import com.github.antag99.aquarria.world.WorldView;

public class WorldRenderer extends Widget {
	private WorldView view;
	private boolean drawTileGrid = false;
	private boolean drawEntityBoxes = false;
	private ShapeRenderer shapeRenderer = null;

	private Array<WorldRendererCallback> worldRendererCallbacks = new Array<>();

	public WorldRenderer() {
		setTouchable(Touchable.disabled);

		worldRendererCallbacks.add(new WallRenderer());
		worldRendererCallbacks.add(new TileRenderer());
		worldRendererCallbacks.add(new EntityRenderer());
		worldRendererCallbacks.add(new LiquidRenderer());
		worldRendererCallbacks.add(new LightRenderer());
	}

	public void queueAssets(AssetManager assetManager) {
		for (WorldRendererCallback callback : worldRendererCallbacks) {
			callback.queueAssets(assetManager);
		}
	}

	public void getAssets(AssetManager assetManager) {
		for (WorldRendererCallback callback : worldRendererCallbacks) {
			callback.getAssets(assetManager);
		}
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

		for (WorldRendererCallback callback : worldRendererCallbacks) {
			callback.render(batch, world, startX, startY, endX, endY);
		}

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

	public WorldView getView() {
		return view;
	}

	public void setView(WorldView view) {
		this.view = view;
	}
}
