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

import java.util.Random;

import com.github.antag99.aquarria.Direction;
import com.github.antag99.aquarria.GameRegistry;
import com.github.antag99.aquarria.TileType;

public class TreeGeneratorTask implements WorldGeneratorTask {
	private TileType treeType;

	public TreeGeneratorTask(TileType treeType) {
		this.treeType = treeType;
	}

	@Override
	public void generate(WorldGenerator generator, long seed) {
		World world = generator.getWorld();
		Random random = new Random(seed);

		for (int i = 5; i < world.getWidth() - 5; i += 10) {
			generateTree(generator, i, world.getSurfaceLevel(i), random);
		}
	}

	private void generateTree(WorldGenerator generator, int x, int y, Random random) {
		World world = generator.getWorld();

		// Height of the tree
		int height = random.nextInt(8) + 5;
		// Direction for the next branch, 0 for random
		int nextBranchDir = 0;
		// When the next branch will be placed
		int nextBranchLevel = 2 + random.nextInt(3);

		for (int i = 0; i < height; ++i) {
			world.setTileType(x, y + i, treeType);
			world.setTileAttached(x, y + i, Direction.SOUTH, true);

			if (i == nextBranchLevel) {
				int branchDir = nextBranchDir != 0 ? nextBranchDir :
						random.nextBoolean() ? 1 : -1;
				world.setTileType(x + branchDir, y + i, treeType);
				world.setTileAttached(x + branchDir, y + i, Direction.get(-branchDir, 0), true);

				nextBranchLevel += 1 + random.nextInt(3);
				nextBranchDir = i + 1 == nextBranchLevel ? -branchDir : 0;
			}
		}

		// Block tile beneath the tree
		world.setTileBlocked(x, y - 1, true);

		// Create left foot?
		if (world.inBounds(x - 1, y) &&
				world.getTileType(x - 1, y) == GameRegistry.airTile &&
				world.getTileType(x - 1, y - 1).isSolid() &&
				random.nextBoolean()) {
			world.setTileType(x - 1, y, treeType);
			world.setTileAttached(x - 1, y, Direction.EAST, true);
			world.setTileAttached(x - 1, y, Direction.SOUTH, true);
			world.setTileBlocked(x - 1, y - 1, true);
		}

		// Create right foot?
		if (world.inBounds(x + 1, y) &&
				world.getTileType(x + 1, y) == GameRegistry.airTile &&
				world.getTileType(x + 1, y - 1).isSolid() &&
				random.nextBoolean()) {
			world.setTileType(x + 1, y, treeType);
			world.setTileAttached(x + 1, y, Direction.WEST, true);
			world.setTileAttached(x + 1, y, Direction.SOUTH, true);
			world.setTileBlocked(x + 1, y - 1, true);
		}
	}
}
