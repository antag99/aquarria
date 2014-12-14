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

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;

public abstract class GraphicsDelegate implements Graphics {
	private Graphics target;

	public GraphicsDelegate(Graphics target) {
		this.target = target;
	}

	public Graphics getTarget() {
		return target;
	}

	public BufferFormat getBufferFormat() {
		return target.getBufferFormat();
	}

	public float getDeltaTime() {
		return target.getDeltaTime();
	}

	public float getDensity() {
		return target.getDensity();
	}

	public DisplayMode getDesktopDisplayMode() {
		return target.getDesktopDisplayMode();
	}

	public DisplayMode[] getDisplayModes() {
		return target.getDisplayModes();
	}

	public long getFrameId() {
		return target.getFrameId();
	}

	public int getFramesPerSecond() {
		return target.getFramesPerSecond();
	}

	public GL20 getGL20() {
		return target.getGL20();
	}

	public GL30 getGL30() {
		return target.getGL30();
	}

	public int getHeight() {
		return target.getHeight();
	}

	public float getPpcX() {
		return target.getPpcX();
	}

	public float getPpcY() {
		return target.getPpcY();
	}

	public float getPpiX() {
		return target.getPpiX();
	}

	public float getPpiY() {
		return target.getPpiY();
	}

	public float getRawDeltaTime() {
		return target.getRawDeltaTime();
	}

	public GraphicsType getType() {
		return target.getType();
	}

	public int getWidth() {
		return target.getWidth();
	}

	public boolean isContinuousRendering() {
		return target.isContinuousRendering();
	}

	public boolean isFullscreen() {
		return target.isFullscreen();
	}

	public boolean isGL30Available() {
		return target.isGL30Available();
	}

	public void requestRendering() {
		target.requestRendering();
	}

	public void setContinuousRendering(boolean isContinuous) {
		target.setContinuousRendering(isContinuous);
	}

	public boolean setDisplayMode(DisplayMode displayMode) {
		return target.setDisplayMode(displayMode);
	}

	public boolean setDisplayMode(int width, int height, boolean fullscreen) {
		return target.setDisplayMode(width, height, fullscreen);
	}

	public void setTitle(String title) {
		target.setTitle(title);
	}

	public void setVSync(boolean vsync) {
		target.setVSync(vsync);
	}

	public boolean supportsDisplayModeChange() {
		return target.supportsDisplayModeChange();
	}

	public boolean supportsExtension(String extension) {
		return target.supportsExtension(extension);
	}
}
