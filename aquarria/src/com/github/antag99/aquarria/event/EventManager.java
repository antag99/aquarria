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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Manages the listeners of an event and the notification of them when the event
 * is fired.
 */
public class EventManager {

	/** Metadata for an event handler, containing the method receiver and event class */
	private static class EventHandler {
		public EventListeners listener;
		public Method recieverMethod;
		public Class<? extends Event> eventClass;
		public float priority;
	}

	/**
	 * Contains the registered event listeners and their associated event handlers.
	 */
	private IdentityMap<EventListeners, EventHandler[]> listeners = new IdentityMap<>();

	/**
	 * Cache for quickly firing an event, updated when an event is fired.
	 */
	private ObjectMap<Class<? extends Event>, EventHandler[]> handlerCache = new ObjectMap<>();

	/**
	 * Creates a new event manager with no listeners
	 */
	public EventManager() {
	}

	/**
	 * Fires the given event. This causes all listeners to be notified.
	 */
	public void fire(Event event) {
		// Lookup the event's type in the cache, or create the cache
		EventHandler[] cache = handlerCache.get(event.getClass());
		if (cache == null) {
			// Collect event handlers for the specific event
			Array<EventHandler> eventHandlers = new Array<EventHandler>(EventHandler.class);
			for (EventHandler[] eventListenerHandlers : listeners.values()) {
				for (EventHandler eventHandler : eventListenerHandlers) {
					if (eventHandler.eventClass.isAssignableFrom(event.getClass())) {
						eventHandlers.add(eventHandler);
					}
				}
			}

			// Sort them based on priority
			eventHandlers.sort((a, b) -> a.priority > b.priority ? 1 : b.priority > a.priority ? -1 : 0);

			// Add the cache...
			cache = eventHandlers.shrink();
			handlerCache.put(event.getClass(), cache);
		}

		// Fire the event!
		for (EventHandler eventHandler : cache) {
			try {
				eventHandler.recieverMethod.invoke(eventHandler.listener, event);
			} catch (InvocationTargetException e) {
				Throwable ex = e.getTargetException();
				if (ex instanceof RuntimeException) {
					throw (RuntimeException) ex;
				} else {
					throw new RuntimeException(ex);
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * Registers the event receivers of the given listener
	 */
	@SuppressWarnings("unchecked")
	public void registerListeners(EventListeners listener) {
		handlerCache.clear();

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
		EventHandler[] eventHandlers = new EventHandler[receiverMethods.size];
		for (int i = 0; i < receiverMethods.size; ++i) {
			Method receiverMethod = receiverMethods.get(i);
			receiverMethod.setAccessible(true);
			EventReceiver receiverMetadata = receiverMethod.getAnnotation(EventReceiver.class);
			eventHandlers[i] = new EventHandler();
			eventHandlers[i].eventClass = (Class<? extends Event>) receiverMethod.getParameterTypes()[0];
			eventHandlers[i].listener = listener;
			eventHandlers[i].recieverMethod = receiverMethod;
			eventHandlers[i].priority = receiverMetadata.priority();
		}

		// Register the event listener with associated event handlers
		listeners.put(listener, eventHandlers);
	}

	/**
	 * Unregisters all event receivers of the given listener
	 */
	public void unregisterListeners(EventListeners listener) {
		handlerCache.clear();

		listeners.remove(listener);
	}
}
