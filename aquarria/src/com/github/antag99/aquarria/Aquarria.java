package com.github.antag99.aquarria;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Aquarria implements ApplicationListener {
	private Batch batch;
	private Stage stage;
	private ScreenViewport viewport;

	private World world;
	private WorldView worldView;
	private WorldRenderer worldRenderer;

	@Override
	public void create() {
		batch = new SpriteBatch();
		viewport = new ScreenViewport();
		stage = new Stage(viewport, batch);

		world = new World(1024, 512);
		new WorldGenerator().generate(world, 0);
		worldView = new WorldView();
		worldView.setWorld(world);
		worldRenderer = new WorldRenderer();
		worldRenderer.setView(worldView);
		worldRenderer.setFillParent(true);
		stage.addActor(worldRenderer);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {
		OrthographicCamera cam = worldView.getCamera();

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			cam.zoom -= 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.translate(-3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.translate(3, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.translate(0, -3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.translate(0, 3, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.rotate(-0.5f, 0, 0, 1);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.rotate(0.5f, 0, 0, 1);
		}

		float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
		float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

		cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, (world.getWidth() * 16f) / cam.viewportWidth);
		//cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, world.getWidth() - effectiveViewportWidth / 2f);
		//cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, world.getHeight() - effectiveViewportHeight / 2f);
		cam.update();
		
		Gdx.gl.glClearColor(0.6f, 0.6f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		OrthographicCamera cam = worldView.getCamera();
		cam.viewportWidth = width / 16f;
		cam.viewportHeight = height / 16f;
		cam.update();

		viewport.update(width, height, true);
	}

	@Override
	public void resume() {
	}

	@Override
	public void pause() {
	}
}
