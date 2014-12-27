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
package com.github.antag99.aquarria.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Splits a tile image into the different frames
 */
public class FrameSplitter {
	private static ObjectMap<String, Rectangle> blockFrames = new ObjectMap<String, Rectangle>();
	private static ObjectMap<String, Rectangle> wallFrames = new ObjectMap<String, Rectangle>();
	private static ObjectMap<String, Rectangle> treeFrames = new ObjectMap<String, Rectangle>();
	private static ObjectMap<String, Rectangle> treeBranchFrames = new ObjectMap<String, Rectangle>();
	private static ObjectMap<String, Rectangle> treeTopFrames = new ObjectMap<String, Rectangle>();

	static {
		blockFrames.put("no_merge", new Rectangle(162, 54, 16, 16));
		blockFrames.put("top_left_merge", new Rectangle(18, 72, 16, 16));
		blockFrames.put("top_right_merge", new Rectangle(0, 72, 16, 16));
		blockFrames.put("bottom_left_merge", new Rectangle(18, 54, 16, 16));
		blockFrames.put("bottom_right_merge", new Rectangle(0, 54, 16, 16));
		blockFrames.put("right_merge", new Rectangle(162, 0, 16, 16));
		blockFrames.put("left_merge", new Rectangle(216, 0, 16, 16));
		blockFrames.put("top_merge", new Rectangle(108, 54, 16, 16));
		blockFrames.put("bottom_merge", new Rectangle(108, 0, 16, 16));
		blockFrames.put("left_right_merge", new Rectangle(108, 72, 16, 16));
		blockFrames.put("top_bottom_merge", new Rectangle(90, 0, 16, 16));
		blockFrames.put("all_merge", new Rectangle(18, 18, 16, 16));
		blockFrames.put("without_bottom_merge", new Rectangle(18, 36, 16, 16));
		blockFrames.put("without_right_merge", new Rectangle(72, 0, 16, 16));
		blockFrames.put("without_top_merge", new Rectangle(18, 0, 16, 16));
		blockFrames.put("without_left_merge", new Rectangle(0, 0, 16, 16));

		wallFrames.put("no_merge", new Rectangle(324, 108, 32, 32));
		wallFrames.put("top_left_merge", new Rectangle(36, 144, 32, 32));
		wallFrames.put("top_right_merge", new Rectangle(0, 144, 32, 32));
		wallFrames.put("bottom_left_merge", new Rectangle(36, 108, 32, 32));
		wallFrames.put("bottom_right_merge", new Rectangle(0, 108, 32, 32));
		wallFrames.put("right_merge", new Rectangle(324, 0, 32, 32));
		wallFrames.put("left_merge", new Rectangle(432, 0, 32, 32));
		wallFrames.put("top_merge", new Rectangle(216, 108, 32, 32));
		wallFrames.put("bottom_merge", new Rectangle(216, 0, 32, 32));
		wallFrames.put("left_right_merge", new Rectangle(216, 144, 32, 32));
		wallFrames.put("top_bottom_merge", new Rectangle(180, 0, 32, 32));
		wallFrames.put("all_merge", new Rectangle(36, 36, 32, 32));
		wallFrames.put("without_bottom_merge", new Rectangle(36, 72, 32, 32));
		wallFrames.put("without_right_merge", new Rectangle(144, 0, 32, 32));
		wallFrames.put("without_top_merge", new Rectangle(36, 0, 32, 32));
		wallFrames.put("without_left_merge", new Rectangle(0, 0, 32, 32));

		treeFrames.put("middle_trunk", new Rectangle(1 * 22, 0 * 22, 20, 20));
		treeFrames.put("middle_trunk_with_left_branch", new Rectangle(4 * 22, 0 * 22, 20, 20));
		treeFrames.put("middle_trunk_with_right_branch", new Rectangle(3 * 22, 3 * 22, 20, 20));
		treeFrames.put("middle_trunk_with_both_branches", new Rectangle(5 * 22, 3 * 22, 20, 20));
		treeFrames.put("top_trunk", new Rectangle(5 * 22, 0 * 22, 20, 20));
		treeFrames.put("top_trunk_with_left_branch", new Rectangle(6 * 22, 0 * 22, 20, 20));
		treeFrames.put("top_trunk_with_right_branch", new Rectangle(6 * 22, 3 * 22, 20, 20));
		treeFrames.put("top_trunk_with_both_branches", new Rectangle(5 * 22, 3 * 22, 20, 20));
		treeFrames.put("left_branch", new Rectangle(3 * 22, 0 * 22, 20, 20));
		treeFrames.put("right_branch", new Rectangle(4 * 22, 3 * 22, 20, 20));
		treeFrames.put("bottom_trunk", new Rectangle(0 * 22, 0 * 22, 20, 20));
		treeFrames.put("bottom_trunk_with_left_stub", new Rectangle(3 * 22, 6 * 22, 20, 20));
		treeFrames.put("bottom_trunk_with_right_stub", new Rectangle(0 * 22, 6 * 22, 20, 20));
		treeFrames.put("bottom_trunk_with_both_stubs", new Rectangle(4 * 22, 6 * 22, 20, 20));
		treeFrames.put("stub", new Rectangle(5 * 22, 0 * 22, 20, 20));
		treeFrames.put("stub_with_left_stub", new Rectangle(7 * 22, 0 * 22, 20, 20));
		treeFrames.put("stub_with_right_stub", new Rectangle(7 * 22, 3 * 22, 20, 20));
		treeFrames.put("stub_with_both_stubs", new Rectangle(7 * 22, 6 * 22, 20, 20));
		treeFrames.put("left_stub", new Rectangle(2 * 22, 6 * 22, 20, 20));
		treeFrames.put("right_stub", new Rectangle(1 * 22, 6 * 22, 20, 20));

		treeBranchFrames.put("left_branch_leaves", new Rectangle(0, 0, 40, 40));
		treeBranchFrames.put("right_branch_leaves", new Rectangle(42, 0, 40, 40));

		treeTopFrames.put("top_trunk_leaves", new Rectangle(0, 0, 80, 80));
	}

	public enum SplitType {
		BLOCK,
		WALL,
		TREE,
		TREE_BRANCH,
		TREE_TOP;
	}

	private ObjectMap<String, Rectangle> frames;

	public FrameSplitter(SplitType splitType) {
		if (splitType == null) {
			throw new NullPointerException();
		}

		switch (splitType) {
		case BLOCK:
			frames = blockFrames;
			break;
		case WALL:
			frames = wallFrames;
			break;
		case TREE:
			frames = treeFrames;
			break;
		case TREE_BRANCH:
			frames = treeBranchFrames;
			break;
		case TREE_TOP:
			frames = treeTopFrames;
			break;
		}
	}

	public void split(FileHandle sourceFile, FileHandle destinationDirectory) {
		Pixmap sourcePixmap = new Pixmap(sourceFile);
		for (String frameName : frames.keys()) {
			Rectangle frameBounds = frames.get(frameName);
			Pixmap destPixmap = new Pixmap((int) frameBounds.width, (int) frameBounds.height, Format.RGBA8888);
			destPixmap.drawPixmap(sourcePixmap, 0, 0,
					(int) frameBounds.x, (int) frameBounds.y,
					(int) frameBounds.width, (int) frameBounds.height);
			PixmapIO.writePNG(destinationDirectory.child(frameName + ".png"), destPixmap);
			destPixmap.dispose();
		}
		sourcePixmap.dispose();
	}
}
