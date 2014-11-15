package com.github.antag99.aquarria;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class AbstractType {
	private static ObjectMap<Class<?>, ObjectMap<String, AbstractType>> typesByName = new ObjectMap<>();
	private static ObjectMap<Class<?>, Array<AbstractType>> types = new ObjectMap<>();
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractType> Array<T> getTypes(Class<T> typeClass) {
		return (Array<T>) types.get(typeClass);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractType> T forName(Class<T> typeClass, String internalName) {
		return (T) typesByName.get(typeClass).get(internalName);
	}
	
	static void queueAll(AssetManager assetManager) {
		for(Array<AbstractType> typeList : types.values()) {
			for(AbstractType type : typeList) {
				type.queueAssets(assetManager);
			}
		}
	}
	
	static void getAll(AssetManager assetManager) {
		for(Array<AbstractType> typeList : types.values()) {
			for(AbstractType type : typeList) {
				type.getAssets(assetManager);
			}
		}
	}
	
	private final String internalName;
	
	public AbstractType(String internalName) {
		if(internalName == null)
			throw new IllegalArgumentException();
		
		this.internalName = internalName;
		
		ObjectMap<String, AbstractType> typeMap = typesByName.get(getTypeClass());
		if(typeMap == null) {
			typeMap = new ObjectMap<String, AbstractType>();
			typesByName.put(getTypeClass(), typeMap);
		}
		
		typeMap.put(internalName, this);
		
		Array<AbstractType> typeList = types.get(getTypeClass());
		if(typeList == null) {
			typeList = new Array<AbstractType>(getTypeClass());
			types.put(getTypeClass(), typeList);
		}
		
		typeList.add(this);
	}
	
	public String getInternalName() {
		return internalName;
	}
	
	protected void queueAssets(AssetManager assetManager) {
	}
	
	protected void getAssets(AssetManager assetManager) {
	}
	
	protected Class<? extends AbstractType> getTypeClass() {
		return getClass();
	}
}
