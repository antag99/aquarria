package com.github.antag99.aquarria.world;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class WorldView {
	private World world;
	private OrthographicCamera camera;
	
	public WorldView() {
		camera = new OrthographicCamera();
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}
}
