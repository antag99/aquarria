package com.github.antag99.aquarria;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class FileHandleResolverMultiplexer implements FileHandleResolver {
	private FileHandleResolver fallback;
	private Array<FileHandleResolver> resolvers = new Array<FileHandleResolver>();
	
	public FileHandleResolverMultiplexer(FileHandleResolver fallback) {
		this.fallback = fallback;
	}

	public void addResolver(FileHandleResolver resolver) {
		resolvers.add(resolver);
	}
	
	public void removeResolver(FileHandleResolver resolver) {
		resolvers.removeValue(resolver, true);
	}
	
	public Array<FileHandleResolver> getResolvers() {
		return resolvers;
	}
	
	@Override
	public FileHandle resolve(String path) {
		FileHandle result = null;
		for(int i = 0; i < resolvers.size; ++i) {
			result = resolvers.get(i).resolve(path);
			if(result != null && result.exists()) {
				return result;
			}
		}
		
		return fallback != null ? fallback.resolve(path) : result;
	}
}
