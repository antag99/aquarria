package com.github.antag99.aquarria;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Aquarria";
		cfg.width = 800;
		cfg.height = 600;
		
		new LwjglApplication(new Aquarria(), cfg);
	}

}
