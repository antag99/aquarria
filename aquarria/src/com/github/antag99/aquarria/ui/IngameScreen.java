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
package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.antag99.aquarria.Aquarria;
import com.github.antag99.aquarria.Assets;
import com.github.antag99.aquarria.entity.PlayerEntity;
import com.github.antag99.aquarria.world.World;
import com.github.antag99.aquarria.world.WorldGenerator;
import com.github.antag99.aquarria.world.WorldView;

public class IngameScreen extends AquarriaScreen {
	private World world;
	private WorldGenerator worldGenerator;
	private WorldView worldView;
	private WorldRenderer worldRenderer;

	private PlayerEntity player;
	private Skin skin;
	private IngameInterface ingameInterface;

	private Vector2 tmpVector2 = new Vector2();
	private Vector3 tmpVector3 = new Vector3();

	private boolean debugStage;

	public IngameScreen(Aquarria aquarria) {
		super(aquarria);

		TextureAtlas atlas = new TextureAtlas(Assets.findFile("images/ui/ui.atlas"));
		skin = new Skin(Assets.findFile("ingameskin.json"), atlas);

		worldRenderer = new WorldRenderer();

		world = new World(1024, 512);
		worldGenerator = new WorldGenerator(world, MathUtils.random.nextLong());
		worldGenerator.generate();

		player = new PlayerEntity();
		player.setX(world.getSpawnX());
		player.setY(world.getSpawnY());
		world.addEntity(player);

		worldView = new WorldView();
		worldView.setWorld(world);

		worldRenderer.setView(worldView);

		ingameInterface = new IngameInterface(skin);
		ingameInterface.setPlayer(player);

		root.stack(worldRenderer, ingameInterface).expand().fill();
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			worldRenderer.setDrawTileGrid(!worldRenderer.getDrawTileGrid());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
			worldRenderer.setDrawEntityBoxes(!worldRenderer.getDrawEntityBoxes());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
			debugStage = !debugStage;
			root.getStage().setDebugAll(debugStage);
		}

		delta = Gdx.graphics.getDeltaTime();

		world.update(delta);

		OrthographicCamera cam = worldView.getCamera();

		cam.position.x = MathUtils.clamp(player.getX() + player.getWidth() * 0.5f, cam.viewportWidth / 2f, world.getWidth() - cam.viewportWidth / 2f);
		cam.position.y = MathUtils.clamp(player.getY() + player.getHeight() * 0.5f, cam.viewportHeight / 2f, world.getHeight() - cam.viewportHeight / 2f);
		cam.zoom = 1f;

		cam.update();

		Vector2 mousePosition = tmpVector2.set(Gdx.input.getX(), Gdx.input.getY());
		mousePosition = aquarria.getStage().screenToStageCoordinates(mousePosition);
		if (aquarria.getStage().hit(mousePosition.x, mousePosition.y, true) == null) {
			// The camera handles the different coordinate systems too, reset to input coordinates
			mousePosition = tmpVector2.set(Gdx.input.getX(), Gdx.input.getY());
			Vector3 worldFocus = cam.unproject(tmpVector3.set(mousePosition, 0f));
			if (worldFocus.x >= 0f && worldFocus.y >= 0f &&
					worldFocus.x < world.getWidth() &&
					worldFocus.y < world.getHeight()) {
				player.setWorldFocus(worldFocus.x, worldFocus.y);
			}
		} else {
			player.setWorldFocus(null);
		}

		if (player.getWorldFocus() != null) {
			if (Gdx.input.justTouched()) {
				if (!player.getRepeatUsingItem()) {
					player.setRepeatUsingItem(true);
				}
				player.setUsedItem(ingameInterface.getFocusItem());
			}

			if (!Gdx.input.isTouched()) {
				if (player.getRepeatUsingItem()) {
					player.setRepeatUsingItem(false);
				}
			}
		} else {
			player.setRepeatUsingItem(false);
			player.setUsingItem(false);
			player.setUseTime(0f);
			player.setUsedItem(null);
		}
	}

	@Override
	public void resize(int width, int height) {
		OrthographicCamera cam = worldView.getCamera();
		cam.viewportWidth = width / World.PIXELS_PER_METER;
		cam.viewportHeight = height / World.PIXELS_PER_METER;
		cam.update();
		super.resize(width, height);
	}

	@Override
	public void show() {
		super.show();
		Stage stage = aquarria.getStage();
		stage.setKeyboardFocus(ingameInterface);
		stage.setScrollFocus(ingameInterface);
	}

	@Override
	public void dispose() {
		skin.dispose();
	}
}
