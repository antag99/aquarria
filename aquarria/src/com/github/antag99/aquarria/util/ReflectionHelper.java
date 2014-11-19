package com.github.antag99.aquarria.util;

public class ReflectionHelper {
	
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> forName(String name) {
		try {
			return (Class<T>) Class.forName(name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> forName(String name, Class<T> superClass) {
		try {
			Class<?> clazz = Class.forName(name);
			if(!superClass.isAssignableFrom(clazz)) {
				throw new RuntimeException(name + " is not a subclass of " + superClass.getName());
			}
			return (Class<? extends T>) clazz;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T newInstance(Class<T> clazz, Object... args) {
		try {
			Class<?>[] types = new Class<?>[args.length];
			for(int i = 0; i < types.length; ++i)
				types[i] = args[i].getClass();
			return clazz.getConstructor(types).newInstance(args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
