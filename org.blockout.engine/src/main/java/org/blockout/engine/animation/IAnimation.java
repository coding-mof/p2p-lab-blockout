package org.blockout.engine.animation;

/**
 * Implementaion of this interface handle the lifetime of an graphical animation.
 * 
 * @author Florian Müller
 *
 */
public interface IAnimation {
	/**
	 * Update the animation.
	 * 
	 * @param delta 
	 * 		Time in milliseconds since last update
	 */
	public void update(long delta);
	
	/**
	 * Render the animation on the given position.
	 * 
	 * @param x 
	 * 		x coordinate of the position where to render the animation.
	 * @param y
	 * 		y coordinate of the position where to render the animation.
	 */
	public void render(int x, int y);
	
	/**
	 * Check if the animation has completed.
	 * 
	 * @return 
	 * 		Return true if the animation has completed, false otherwise.
	 */
	public boolean completed();
	
	/**
	 * Start the animation
	 */
	public void start();
	
	/**
	 * Stop the animation
	 */
	public void stop();
	
	/**
	 * Restart the animation from the beginning
	 */
	public void restart();
	
	/**
	 * Indicate if this animation should loop forever or stop if it's done.
	 * 
	 * @param looping
	 * 		True if the animation should loop, false otherwise.
	 */
	public void setLooping(boolean looping);

    /**
     * Returns if the animation is looping
     * 
     * @return True if the animation is looping, false otherwise.
     */
    public boolean isLooping();
}
