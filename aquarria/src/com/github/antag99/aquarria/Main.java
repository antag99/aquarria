package com.github.antag99.aquarria;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;

public class Main {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Aquarria";
		cfg.width = 800;
		cfg.height = 600;
		
		if(args.length > 0 && args[0].equals("-rextract")) {
			new FileHandle("assets-terraria").deleteDirectory();
		}
		
		new LwjglApplication(new Aquarria(), cfg);
	}

}
