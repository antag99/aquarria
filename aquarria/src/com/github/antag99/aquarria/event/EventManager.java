/*******************************************************************************
 * Copyright (c) 2014, Anton Gustafsson
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
package com.github.antag99.aquarria.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.ObjectMap;

public class EventManager {
	// TODO: Clean up this generic sh*t; it's probably worse than necessary.
	// TODO: Find a way to handle the cache more gracefully; right now it is
	// reset every time an event listener is removed or added. Or get rid of
	// it entirely, if it is possible to get a reasonable speed that way.
	// TODO: Event listeners with the same priority should depend on the order in which they were added.

	/**
	 * Cache for quickly firing an event, updated when an event is
	 * fired for the first time after modification. Sorted by
	 * event listener priority.
	 */
	private ObjectMap<Class<?>, EventListener<?>[]> listenerCache = new ObjectMap<>();

	/**
	 * Mapping of target event class to listeners
	 */
	private ObjectMap<Class<?>, Array<EventListener<?>>> registeredListeners = new ObjectMap<>();

	/**
	 * Mapping of {@link EventListeners} to registered handlers
	 */
	private IdentityMap<EventListeners, Array<EventListener<?>>> registeredHandlers = new IdentityMap<>();

	private Array<EventListener<?>> getRegisteredListeners(Class<?> eventClass) {
		if (!registeredListeners.containsKey(eventClass)) {
			registeredListeners.put(eventClass, new Array<EventListener<?>>());
		}
		return registeredListeners.get(eventClass);
	}

	private static final class EventHandler implements EventListener<Event> {
		private Method receiverMethod;
		private Object targetObject;
		private float priority;
		private Class<?> eventClass;

		public EventHandler(Method receiverMethod, Object targetObject, float priority, Class<?> eventClass) {
			this.receiverMethod = receiverMethod;
			this.targetObject = targetObject;
			this.priority = priority;
			this.eventClass = eventClass;
		}

		@Override
		public void notify(Event event) {
			try {
				receiverMethod.invoke(targetObject, event);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class<Event> getEventClass() {
			return (Class<Event>) eventClass;
		}

		@Override
		public float getPriority() {
			return priority;
		}
	}

	/**
	 * Creates a new event manager with no listeners
	 */
	public EventManager() {
	}

	/**
	 * Fires the given event. This causes all listeners to be notified.
	 */
	@SuppressWarnings("unchecked")
	public void fire(Event event) {
		// Lookup the event's type in the cache, or create the cache
		EventListener<?>[] cache = listenerCache.get(event.getClass());
		if (cache == null) {
			// Collect event listeners
			Array<EventListener<?>> eventListeners = new Array<>(EventListener.class);
			Class<?> eventClass = event.getClass();
			do {
				eventListeners.addAll(getRegisteredListeners(eventClass));
				eventClass = eventClass.getSuperclass();
			} while (eventClass != Event.class);

			// Sort them based on priority
			eventListeners.sort((a, b) -> {
				float aPriority = a.getPriority();
				float bPriority = b.getPriority();
				return aPriority > bPriority ? 1 : bPriority > aPriority ? -1 : 0;
			});

			// Add the cache...
			cache = eventListeners.shrink();
			listenerCache.put(event.getClass(), cache);
		}

		// Fire the event!
		for (EventListener<?> listener : cache) {
			((EventListener<Event>) listener).notify(event);
		}
	}

	/**
	 * Registers the given event listener
	 */
	public void registerListener(EventListener<?> listener) {
		listenerCache.clear();
		getRegisteredListeners(listener.getEventClass()).add(listener);
	}

	/**
	 * Unregisters the given event listener
	 */
	public void unregisterListener(EventListener<?> listener) {
		listenerCache.clear();
		getRegisteredListeners(listener.getEventClass()).removeValue(listener, true);
	}

	/**
	 * Registers the event receivers of the given listener
	 */
	public void registerListeners(EventListeners listener) {
		listenerCache.clear();

		// Find suitable event receiver methods
		Array<Method> receiverMethods = new Array<Method>();
		Class<?> level = listener.getClass();
		do {
			for (Method method : level.getDeclaredMethods()) {
				if ((method.getModifiers() & (Modifier.STATIC | Modifier.ABSTRACT)) == 0 &&
						method.isAnnotationPresent(EventReceiver.class) &&
						method.getParameterTypes().length == 1 &&
						Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
					receiverMethods.add(method);
				}
			}

			level = level.getSuperclass();
		} while (EventListeners.class.isAssignableFrom(level));

		// Create event handlers from the found methods
		Array<EventListener<?>> eventHandlers = new Array<>();
		for (Method receiverMethod : receiverMethods) {
			receiverMethod.setAccessible(true);
			EventReceiver properties = receiverMethod.getAnnotation(EventReceiver.class);
			eventHandlers.add(new EventHandler(receiverMethod, listener,
					properties.priority(), receiverMethod.getParameterTypes()[0]));
		}
		for (EventListener<?> eventHandler : eventHandlers) {
			registerListener(eventHandler);
		}
		registeredHandlers.put(listener, eventHandlers);
	}

	/**
	 * Unregisters all event receivers of the given listener
	 */
	public void unregisterListeners(EventListeners listener) {
		Array<EventListener<?>> eventHandlers = registeredHandlers.get(listener);
		for (EventListener<?> eventHandler : eventHandlers) {
			unregisterListener(eventHandler);
		}
		registeredHandlers.remove(listener);
	}
}
