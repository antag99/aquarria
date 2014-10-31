package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.github.antag99.aquarria.Aquarria;
import com.github.antag99.aquarria.entity.Entity;
import com.github.antag99.aquarria.entity.EntityType;
import com.github.antag99.aquarria.world.World;
import com.github.antag99.aquarria.world.WorldGenerator;
import com.github.antag99.aquarria.world.WorldRenderer;
import com.github.antag99.aquarria.world.WorldView;

public class IngameScreen extends AquarriaScreen {
	public static final float PIXELS_PER_METER = 16;

	private World world;
	private WorldView worldView;
	private WorldRenderer worldRenderer;
	
	private Entity player;

	public IngameScreen(Aquarria aquarria) {
		super(aquarria);
		
		world = new World(1024, 512);
		new WorldGenerator().generate(world, 0);
		
		player = new Entity(EntityType.player);
		player.getPosition().set(world.getSpawnPoint());
		world.addEntity(player);
		
		worldView = new WorldView();
		worldView.setWorld(world);
		
		worldRenderer = new WorldRenderer();
		worldRenderer.setView(worldView);
		root.add(worldRenderer).expand().fill();
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			worldRenderer.setDrawTileGrid(!worldRenderer.getDrawTileGrid());
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
			worldRenderer.setDrawEntityBoxes(!worldRenderer.getDrawEntityBoxes());
		}
		
		world.update(delta);

		OrthographicCamera cam = worldView.getCamera();

		Vector2 playerPosition = player.getPosition();
		cam.position.x = playerPosition.x + player.getSize().x * 0.5f;
		cam.position.y = playerPosition.y + player.getSize().y * 0.5f;
		cam.zoom = 0.7f;

		cam.update();
	}
	
	@Override
	public void resize(int width, int height) {
		OrthographicCamera cam = worldView.getCamera();
		cam.viewportWidth = width / PIXELS_PER_METER;
		cam.viewportHeight = height / PIXELS_PER_METER;
		cam.update();
		super.resize(width, height);
	}
}
