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
package com.github.antag99.aquarria.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IntArray;

public class LiquidManager {
	private World world;
	private byte[] liquidMatrix;
	private float tickCounter;
	private IntArray activeLiquids;

	// Liquid simulation uses a fixed time step,
	// as it is quite hard to interpolate liquid movement
	// based on the time since the last frame.
	private static final float TICK = (1f / 255);
	private static final int MAX_LIQUID = 255;

	public LiquidManager(World world) {
		this.world = world;
		liquidMatrix = new byte[world.getWidth() * world.getHeight()];
		activeLiquids = new IntArray();
	}

	public int getLiquid(int x, int y) {
		world.checkBounds(x, y);

		return liquidMatrix[x + y * world.getWidth()] & 0xff;
	}

	public void setLiquid(int x, int y, int liquid) {
		world.checkBounds(x, y);

		int position = x + y * world.getWidth();
		liquidMatrix[position] = (byte) liquid;

		boolean liquidActive = activeLiquids.contains(position);
		if (liquid != 0 && !liquidActive)
			activeLiquids.add(position);
		if (liquid == 0 && liquidActive)
			activeLiquids.removeValue(position);
	}

	public void update(float delta) {
		tickCounter += delta;

		while (tickCounter > TICK) {
			tick();

			tickCounter -= TICK;
		}
	}

	private void tick() {
		for (int i = 0; i < activeLiquids.size; ++i) {
			int position = activeLiquids.items[i];
			int x = position % world.getWidth();
			int y = position / world.getWidth();

			flow(x, y);
		}
	}

	private void flow(int x, int y) {
		if (world.getTileType(x, y).isSolid())
			return;

		if (!flow(x, y, x, y - 1, 16, false) || getLiquid(x, y - 1) == MAX_LIQUID) {
			// Randomizing the order prevents issues related to
			// water not flowing in some fixed direction when just one unit is left.
			int dir = MathUtils.random(0, 1) * 2 - 1;
			flow(x, y, x + dir, y, 16, true);
			flow(x, y, x - dir, y, 16, true);
		}
	}

	/**
	 * @param smooth Whether the liquids between the two tiles should be smoothed to the same amount.
	 * @return Whether any liquid was moved.
	 */
	private boolean flow(int srcX, int srcY, int dstX, int dstY, int speed, boolean smooth) {
		if (world.inBounds(dstX, dstY) && !world.getTileType(dstX, dstY).isSolid()) {
			int srcLiquid = getLiquid(srcX, srcY);
			int dstLiquid = getLiquid(dstX, dstY);

			int amount = speed;

			if (smooth) {
				int targetLiquid = (srcLiquid + dstLiquid) / 2;
				int remainder = (srcLiquid + dstLiquid) % 2;
				amount = Math.min(targetLiquid - dstLiquid, amount) + remainder;
			} else {
				amount = Math.min(MAX_LIQUID - dstLiquid, amount);
			}

			amount = Math.min(srcLiquid, amount);

			setLiquid(srcX, srcY, srcLiquid - amount);
			setLiquid(dstX, dstY, dstLiquid + amount);

			return amount != 0;
		}

		return false;
	}
}
