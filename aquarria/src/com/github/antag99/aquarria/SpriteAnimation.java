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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Stores data for spritesheet-based animation. {@link #getFrame(SpriteSheet, float)} </>
 * is used to get the current frame in an animation.
 */
public class SpriteAnimation {
	/* beginning frame position */
	private int frameStartX;
	private int frameStartY;
	/* amount of frames */
	private int frameCount;
	/* total time of the animation */
	private float animationTime;

	public SpriteAnimation(
			int frameStartX, int frameStartY,
			int frameCount, float animationTime) {
		this.frameStartX = frameStartX;
		this.frameStartY = frameStartY;
		this.frameCount = frameCount;
		this.animationTime = animationTime;
	}

	public int getFrameStartX() {
		return frameStartX;
	}

	public int getFrameStartY() {
		return frameStartY;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public float getAnimationTime() {
		return animationTime;
	}

	public void setAnimationTime(float animationTime) {
		this.animationTime = animationTime;
	}

	public TextureRegion getFrame(SpriteSheet sheet, float time) {
		float frameTime = animationTime / frameCount;
		int frameIndex = MathUtils.floor((time / frameTime) % frameCount);
		int frameCountX = sheet.getGrid().getSpriteCountX() - frameStartX;
		int frameCountY = sheet.getGrid().getSpriteCountY() - frameStartY;
		int frameX = frameIndex % frameCountX;
		int frameY = (frameIndex / frameCountX) % frameCountY;

		return sheet.getSprite(frameStartX + frameX, frameStartY + frameY);
	}
}
