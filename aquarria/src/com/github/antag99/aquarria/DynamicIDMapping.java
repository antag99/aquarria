package com.github.antag99.aquarria;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;

/** Automatically expanding object-to-id/id-to-object mapping */
public class DynamicIDMapping<T> {
	private ObjectIntMap<T> idMap = new ObjectIntMap<T>();
	private IntMap<T> objectMap = new IntMap<T>();
	private int counter = 0;
	
	public DynamicIDMapping() {
	}
	
	public T getObject(int id) {
		T result = objectMap.get(id);
		if(result == null) {
			throw new RuntimeException("No object registered with ID " + id);
		}
		return result;
	}
	
	public int getID(T object) {
		int result = idMap.get(object, -1);
		if(result == -1) {
			result = counter++;
			objectMap.put(result, object);
			idMap.put(object, result);
		}
		return result;
	}
}
