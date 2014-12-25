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
package com.github.antag99.aquarria.tests;

import org.junit.Assert;
import org.junit.Test;

import com.github.antag99.aquarria.event.Event;
import com.github.antag99.aquarria.event.EventListeners;
import com.github.antag99.aquarria.event.EventManager;
import com.github.antag99.aquarria.event.EventReceiver;

public class InhertianceEventTests extends Assert {

	/**
	 * This test verifies that an event handler for an event also get invoked
	 * for the subtypes of the event.
	 */
	@Test
	public void testInheritedEvent() {
		EventManager eventManager = new EventManager();
		eventManager.registerListeners(new BaseEventListener());
		InheritedEvent inheritedEvent = new InheritedEvent();
		eventManager.fire(inheritedEvent);
		assertEquals("Event handlers for a supertype dosen't get invoked for a subtype", 1, inheritedEvent.counter);
	}

	/**
	 * This test verifies that an event handler for an event dosen't get invoked
	 * for the supertypes of the event.
	 */
	@Test
	public void testSuperEvent() {
		EventManager eventManager = new EventManager();
		eventManager.registerListeners(new InheritedEventListener());
		BaseEvent baseEvent = new BaseEvent();
		eventManager.fire(baseEvent);
		assertEquals("Event handlers for a subtype gets invoked for a supertype", 0, baseEvent.counter);
	}

	public static class BaseEvent extends Event {
		public int counter = 0;

		@Override
		public Object[] pack() {
			return new Object[0];
		}

		@Override
		public void unpack(Object[] packed) {
		}
	}

	public static class InheritedEvent extends BaseEvent {
	}

	public static class BaseEventListener implements EventListeners {
		@EventReceiver
		public void onBaseEvent(BaseEvent event) {
			event.counter++;
		}
	}

	public static class InheritedEventListener implements EventListeners {
		@EventReceiver
		public void onInheritedEvent(InheritedEvent event) {
			event.counter++;
		}
	}
}
