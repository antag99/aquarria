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
package com.github.antag99.aquarria.benchmarks;

import org.junit.Test;

import com.github.antag99.aquarria.event.Event;
import com.github.antag99.aquarria.event.EventListeners;
import com.github.antag99.aquarria.event.EventManager;
import com.github.antag99.aquarria.event.Receiver;

public class EventBenchmarks {
	@Test
	public void dumbBenchmark() {
		benchmark(10000, 10000);
		benchmark(10000, 10000);
		benchmark(10000, 10000);
	}

	private void benchmark(int events, int listeners) {
		System.out.println("Firing " + events + " events with " + listeners + " listeners");
		EventManager eventManager = new EventManager();
		for (int i = 0; i < listeners; ++i) {
			eventManager.registerListeners(new SampleEventListener());
		}
		long startTime = System.nanoTime();
		for (int i = 0; i < events; ++i) {
			eventManager.fire(new SampleEvent());
		}
		long totalTime = System.nanoTime() - startTime;
		System.out.println("Took " + Double.toString(totalTime / 1000000000.0) + " seconds. (" + totalTime + " ns)");
	}

	private static class SampleEvent extends Event {
		@Override
		public Object[] pack() {
			return new Object[0];
		}

		@Override
		public void unpack(Object[] packed) {
		}
	}

	private static class SampleEventListener implements EventListeners {
		@Receiver
		public void onSampleEvent(SampleEvent event) {
		}
	}
}
