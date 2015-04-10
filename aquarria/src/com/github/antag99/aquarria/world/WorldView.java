/*******************************************************************************
 * Copyright (c) 2014-2015, Anton Gustafsson
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

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Stores view-related information of a {@link World}.
 * </p>
 * Tile frames are stored as plain integers, expected to be managed by the framing
 * code of the relevant tile type, preferably by mapping the frames
 * to {@link Enum#ordinal() enum ordinals}.
 */
public class WorldView {
	private World world;
	private OrthographicCamera camera;

	private byte[] tileFrame;
	private byte[] wallFrame;

	private final int width;
	private final int height;

	/**
	 * Creates a new world view for the given world
	 */
	public WorldView(World world) {
		this.world = world;
		this.camera = new OrthographicCamera();

		width = world.getWidth();
		height = world.getHeight();

		clear();
	}

	/**
	 * Clears the frame data of this view.
	 */
	public void clear() {
		tileFrame = new byte[width * height];
		wallFrame = new byte[width * height];
	}

	/**
	 * Gets the frame of the tile at the given position.
	 * 
	 * @param x The X position of the tile
	 * @param y The Y position of the tile
	 * @return The frame of the tile at the given position.
	 */
	public int getTileFrame(int x, int y) {
		world.checkBounds(x, y);

		return tileFrame[x + y * width] & 0xff;
	}

	/**
	 * Sets the frame of the tile at the given position.
	 * 
	 * @param x The X position of the tile
	 * @param y The Y position of the tile
	 * @param frame The frame of the tile at the given position. Must be in the range 0-255.
	 */
	public void setTileFrame(int x, int y, int frame) {
		world.checkBounds(x, y);

		if ((frame & 0xff) != frame)
			throw new IllegalArgumentException("frame out of range: " + frame);

		tileFrame[x + y * width] = (byte) frame;
	}

	/**
	 * Gets the frame of the wall at the given position.
	 * 
	 * @param x The X position of the tile
	 * @param y The Y position of the tile
	 * @return The frame of the wall at the given position.
	 */
	public int getWallFrame(int x, int y) {
		world.checkBounds(x, y);

		return wallFrame[x + y * width] & 0xff;
	}

	/**
	 * Sets the frame of the wall at the given position.
	 * 
	 * @param x The X position of the tile
	 * @param y The Y position of the tile
	 * @param frame The frame of the wall at the given position. Must be in the range 0-255.
	 */
	public void setWallFrame(int x, int y, int frame) {
		world.checkBounds(x, y);

		if ((frame & 0xff) != frame)
			throw new IllegalArgumentException("frame out of range: " + frame);

		wallFrame[x + y * width] = (byte) frame;
	}

	/**
	 * Gets the world of this view
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the camera of this view
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}
}
