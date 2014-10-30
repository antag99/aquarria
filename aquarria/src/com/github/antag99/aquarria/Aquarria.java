package com.github.antag99.aquarria;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Aquarria implements ApplicationListener {
	public static final float PIXELS_PER_METER = 16;
	
	private Batch batch;
	private Stage stage;
	private ScreenViewport viewport;

	private World world;
	private WorldView worldView;
	private WorldRenderer worldRenderer;
	
	private Entity player;

	@Override
	public void create() {
		batch = new SpriteBatch();
		viewport = new ScreenViewport();
		stage = new Stage(viewport, batch);

		world = new World(1024, 512);
		new WorldGenerator().generate(world, 0);
		player = new Entity(EntityType.player);
		player.getPosition().set(world.getSpawnPoint());
		world.addEntity(player);
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
		float delta = Gdx.graphics.getDeltaTime();
		world.update(delta);

		OrthographicCamera cam = worldView.getCamera();

		Vector2 playerPosition = player.getPosition();
		cam.position.x = playerPosition.x;
		cam.position.y = playerPosition.y;
		cam.zoom = 0.7f;

		cam.update();
		
		Gdx.gl.glClearColor(0.6f, 0.6f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		OrthographicCamera cam = worldView.getCamera();
		cam.viewportWidth = width / PIXELS_PER_METER;
		cam.viewportHeight = height / PIXELS_PER_METER;
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
