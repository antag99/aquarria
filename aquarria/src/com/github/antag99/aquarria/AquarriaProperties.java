package com.github.antag99.aquarria;

import com.badlogic.gdx.files.FileHandle;

public class AquarriaProperties {
	private String terrariaDirectory;
	private boolean forceExtractAssets = false;
	
	public AquarriaProperties() {
	}

	public FileHandle getTerrariaDirectory() {
		return terrariaDirectory == null ? null : new FileHandle(terrariaDirectory);
	}
	
	public void setTerrariaDirectory(FileHandle terrariaDirectory) {
		this.terrariaDirectory = terrariaDirectory == null ? "" : terrariaDirectory.path();
	}
	
	public void setForceExtractAssets(boolean forceExtractAssets) {
		this.forceExtractAssets = forceExtractAssets;
	}

	public boolean getForceExtractAssets() {
		return forceExtractAssets;
	}
}
