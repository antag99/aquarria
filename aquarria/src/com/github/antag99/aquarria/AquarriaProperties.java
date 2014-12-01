package com.github.antag99.aquarria;

import com.badlogic.gdx.files.FileHandle;

public class AquarriaProperties {
	private String terrariaDirectory;

	public AquarriaProperties() {
	}

	public FileHandle getTerrariaDirectory() {
		return terrariaDirectory == null ? null : new FileHandle(terrariaDirectory);
	}

	public void setTerrariaDirectory(FileHandle terrariaDirectory) {
		this.terrariaDirectory = terrariaDirectory == null ? "" : terrariaDirectory.path();
	}
}
