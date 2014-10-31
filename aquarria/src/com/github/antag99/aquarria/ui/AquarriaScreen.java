package com.github.antag99.aquarria.ui;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.antag99.aquarria.Aquarria;

public class AquarriaScreen extends ScreenAdapter {
	protected Aquarria aquarria;
	protected Table root;
	
	public AquarriaScreen(Aquarria aquarria) {
		this.aquarria = aquarria;
		
		root = new Table();
		root.setFillParent(true);
	}
	
	@Override
	public void show() {
		aquarria.getStage().addActor(root);
	}
	
	@Override
	public void hide() {
		root.remove();
	}
	
	public Table getRoot() {
		return root;
	}
}
