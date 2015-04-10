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
package com.github.antag99.aquarria;

import com.github.antag99.aquarria.world.World;
import com.github.antag99.aquarria.world.WorldView;

public interface TileType extends Type {

	/**
	 * Gets whether this tile is solid. Solid tiles can collide with solid entities.
	 * Examples of non-solid tiles are trees and workbenches. Solid tiles include
	 * dirt, stone and grass blocks.
	 */
	public boolean isSolid();

	/**
	 * Gets the sprite for the tile at the given position. This additional information
	 * is required to implement things such as sprites depending on adjacent tiles.
	 * 
	 * @param worldView The world view for the world the tile resides in
	 * @param x The X position of the tile
	 * @param y The Y position of the tile
	 */
	public Sprite getTexture(WorldView worldView, int x, int y);

	/**
	 * Called when this tile is placed in a world. This is not invoked as a result
	 * of world generation; it's called when a player or event causes this tile
	 * to be placed.
	 * 
	 * @param world The world this tile was placed in
	 * @param x The X position of the tile
	 * @param y The Y position of the tile
	 */
	public void placed(World world, int x, int y);

	/**
	 * Called when this tile is destroyed in a world. This is not invoked as a result
	 * of world generation; it's called when a player or event causes this tile
	 * to be destroyed.
	 * 
	 * @param world The world this tile was destroyed in
	 * @param x The X position of the tile
	 * @param y The Y position of the tile
	 */
	public void destroyed(World world, int x, int y);
}
