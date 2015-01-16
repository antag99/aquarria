package com.github.antag99.aquarria;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * Stores data for a sprite sheet containing the frames of an animation;
 * this includes properties such as the region of the spritesheet in which
 * the frames reside and the total tile of an animation. {@link #getFrame(float)} </>
 * is used to get the current frame in an animation.
 */
public class AnimatedSpriteSheet extends SpriteSheet {
	/* beginning frame position */
	private int frameStartX;
	private int frameStartY;
	/* amount of frames */
	private int frameCount;
	/* total time of the animation */
	private float animationTime;

	public AnimatedSpriteSheet(TextureRegion texture,
			int spriteCountX, int spriteCountY,
			int spriteOffsetX, int spriteOffsetY,
			int spriteWidth, int spriteHeight,
			int spriteSpacingX, int spriteSpacingY,
			int frameStartX, int frameStartY,
			int frameCount, float animationTime) {

		super(texture, spriteCountX, spriteCountY, spriteOffsetX, spriteOffsetY,
				spriteWidth, spriteHeight, spriteSpacingX, spriteSpacingY);

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

	public TextureRegion getFrame(float time) {
		float frameTime = animationTime / frameCount;
		int frameIndex = MathUtils.floor(time / frameTime) % frameCount;
		int frameCountX = getSpriteCountX() - frameStartX;
		int frameCountY = getSpriteCountY() - frameStartY;
		int frameX = frameIndex % frameCountX;
		int frameY = frameIndex / frameCountY;
		return getSprite(frameX, frameY);
	}
}
