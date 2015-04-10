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

/**
 * Stores data for spritesheet-based animation. {@link #getFrame(Sprite[], float)} </>
 * is used to get the current frame in an animation.
 */
public class SpriteAnimation {
	private int frameOffset;
	private int frameLength;

	private float animationTime;

	public SpriteAnimation(int frameOffset, int frameLength, float animationTime) {
		this.frameOffset = frameOffset;
		this.frameLength = frameLength;
		this.animationTime = animationTime;
	}

	public int getFrameOffset() {
		return frameOffset;
	}

	public int getFrameLength() {
		return frameLength;
	}

	public float getAnimationTime() {
		return animationTime;
	}

	public Sprite getFrame(SpriteSheet sheet, float time) {
		float frameTime = animationTime / frameLength;
		int frameIndex = frameOffset + (int) (time / frameTime) % frameLength;
		return sheet.getSprite(frameIndex % sheet.getWidth(), frameIndex / sheet.getWidth());
	}
}
