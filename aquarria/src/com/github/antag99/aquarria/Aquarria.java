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
package com.github.antag99.aquarria;

import java.util.Scanner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.antag99.aquarria.ui.IngameScreen;
import com.github.antag99.aquarria.xnb.Steam;

public class Aquarria extends Game {
	private Batch batch;
	private Stage stage;
	private ScreenViewport viewport;
	private Preferences preferences;

	private FileHandle terrariaAssets;
	private FileHandle terrariaDirectory;

	private IngameScreen ingameScreen;

	public Aquarria() {
	}

	private void processAssets() {
		terrariaAssets = Gdx.files.local("assets-terraria");

		System.out.println("Terraria directory: " + terrariaDirectory.path());
		System.out.println("Processing vanilla assets...");
		try {
			long startTime = System.currentTimeMillis();

			ContentExtractor extractor = new ContentExtractor(terrariaDirectory.child("Content"), terrariaAssets);
			extractor.convert();

			long time = System.currentTimeMillis() - startTime;
			System.out.println("Done. Took " + time / 1000f + " seconds");
		} catch (Throwable ex) {
			System.err.println("Error extracting assets. Exiting.");
			ex.printStackTrace();
			Gdx.app.exit();
			return;
		}
	}

	@Override
	public void create() {
		/* load preferences */
		preferences = Gdx.app.getPreferences("aquarria");

		/* locate terraria directory */
		if (!preferences.contains("terrariaDirectory")) {
			terrariaDirectory = Steam.findTerrariaDirectory();
		} else {
			terrariaDirectory = new FileHandle(preferences.getString("terrariaDirectory"));
		}

		while (terrariaDirectory == null || !terrariaDirectory.exists()) {
			System.out.println("Terraria installation directory not found.");
			System.out.print("Enter terraria installation directory: ");
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			terrariaDirectory = new FileHandle(scanner.nextLine());
			savePreferences();
		}
		savePreferences();

		/* process vanilla terraria assets */
		processAssets();

		batch = new SpriteBatch();
		viewport = new ScreenViewport();
		stage = new Stage(viewport, batch);

		Gdx.input.setInputProcessor(stage);

		Assets.initialize();
		GameRegistry.initialize();
		ingameScreen = new IngameScreen(this);

		setScreen(ingameScreen);
	}

	private void savePreferences() {
		preferences.putString("terrariaDirectory", terrariaDirectory.path());
		preferences.flush();
	}

	@Override
	public void dispose() {
		super.dispose();
		ingameScreen.dispose();
		Assets.dispose();
		batch.dispose();
		savePreferences();
	}

	public Stage getStage() {
		return stage;
	}

	public Batch getBatch() {
		return batch;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.6f, 0.6f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		super.resize(width, height);
	}
}
