package org.blockout.engine;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import com.google.common.base.Preconditions;

/**
 * Animation that use a series of sprites do visualize it
 * 
 * @author Florian Müller
 *
 */
public class SpriteAnimation implements IAnimation {
	Animation animation;
	
	/**
	 * Constructor to create a new sprite based animation from a spritesheet
	 * 
	 * @param spriteManager
	 * 		SpriteSheet where to get the frames
	 * @param frameTypes
	 * 		Array with sprite ids to use as frames for the animation
	 * @param duration
	 * 		Time how long a single frame should be rendered
	 * @throws NullPointerException
	 * 		If the spritesheet was null
	 * @throws IllegalArgumentException
	 * 		If the spritesheet was not loaded or there are no frames or the duration was negative
	 */
	public SpriteAnimation(ISpriteSheet spriteSheet, int[] frameIds, int duration) {
		Preconditions.checkNotNull(spriteSheet);
		Preconditions.checkArgument(spriteSheet.isSpriteSheetLoaded(), "SpriteSheet has an invalid state");
		Preconditions.checkArgument(0 < frameIds.length, "Need at least one frame");
		Preconditions.checkArgument(0 < duration, "Duration should be positive");
		
		List<Image> images = new ArrayList<Image>(frameIds.length);
		for(int id: frameIds)
			images.add(spriteSheet.getSprite(id));
		
		animation = new Animation((Image[]) images.toArray(new Image[0]), duration);
	}
	
	/**
	 * Constructor to create a new sprite based animation from a spritemanager
	 * 
	 * @param spriteManager
	 * 		SpriteManager where to get the frames
	 * @param frameTypes
	 * 		Array with sprite types to use as frames for the animation
	 * @param duration
	 * 		Time how long a single frame should be rendered
	 * @throws NullPointerException
	 * 		If the spritesheet was null
	 * @throws IllegalArgumentException
	 * 		If there are no frames or the duration was negative
	 */
	public SpriteAnimation(ISpriteManager spriteManager, SpriteType[] frameTypes, int duration){
		Preconditions.checkNotNull(spriteManager);;
		Preconditions.checkArgument(0 < frameTypes.length, "Need at least one frame");
		Preconditions.checkArgument(0 < duration, "Duration should be positive");
		
		List<Image> images = new ArrayList<Image>(frameTypes.length);
		for(SpriteType type: frameTypes)
			images.add(spriteManager.getSprite(type));
		
		animation = new Animation((Image[]) images.toArray(new Image[0]), duration);
	}
	
	@Override
	public void update(long delta) {
		animation.update(delta);
	}

	@Override
	public void render(int x, int y) {
		animation.draw(x, y);
	}

	@Override
	public boolean completed() {
		return animation.isStopped();
	}

	@Override
	public void start() {
		Preconditions.checkNotNull(animation, "No sprite animation loaded");
		
		animation.start();
	}

	@Override
	public void stop() {
		animation.stop();
	}

	@Override
	public void restart() {
		animation.restart();
	}

	@Override
	public void setLooping(boolean looping) {
		animation.setLooping(looping);
	}
}
