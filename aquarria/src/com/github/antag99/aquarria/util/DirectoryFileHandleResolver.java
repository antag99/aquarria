package com.github.antag99.aquarria.util;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class DirectoryFileHandleResolver implements FileHandleResolver {
	private FileHandle directory;
	
	public DirectoryFileHandleResolver(FileHandle directory) {
		this.directory = directory;
	}
	
	@Override
	public FileHandle resolve(String path) {
		return directory.child(path);
	}
}
