/**
 * 
 */
package org.blockout.engine.animation;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

/**
 * Class to create a particle based animation
 * 
 * @author Florian Müller
 *
 */
public class ParticleAnimation implements IAnimation {
	
	private ParticleSystem particleSystem;
	private boolean isLooping;
	private HashMap<String, ParticleEmitter> effects;
	
	/**
	 * Constructor to create a default configured particle animation
	 */
	public ParticleAnimation() {
		particleSystem = new ParticleSystem("particle.tga");
		particleSystem.setRemoveCompletedEmitters(true);
		
		effects = new HashMap<String, ParticleEmitter>();
		isLooping = false;
	}
	
	/**
	 * Constructor to create a particle animation with a other default image
	 * 
	 * @param defaultImage
	 * 		New default particle image
	 */
	public ParticleAnimation(Image defaultImage) {
		particleSystem = new ParticleSystem(defaultImage);
		particleSystem.setRemoveCompletedEmitters(true);
		
		effects = new HashMap<String, ParticleEmitter>();
		isLooping = false;
	}
	
	/**
	 * Constructor to create a particle animation with a modified max particle count
	 * 
	 * @param maxParticles
	 * 		Maximum amount of particles used with this animation
	 */
	public ParticleAnimation(int maxParticles){
		particleSystem = new ParticleSystem("particle.tga", maxParticles);
		particleSystem.setRemoveCompletedEmitters(true);
		
		effects = new HashMap<String, ParticleEmitter>();
		isLooping = false;
	}
	
	/**
	 * Contructor for a particle animation with a new default particle image
	 * and a modified max particle count.
	 * 
	 * @param defaultImage
	 * 		New default particle image
	 * @param maxParticles
	 * 		Maximum amount of particles used with this animation
	 */
	public ParticleAnimation(Image defaultImage, int maxParticles) {
		particleSystem = new ParticleSystem(defaultImage, maxParticles);
		particleSystem.setRemoveCompletedEmitters(false);
		
		effects = new HashMap<String, ParticleEmitter>();
		isLooping = false;
	}
	
	/*
	 * @see org.blockout.engine.IAnimation#update(long)
	 */
	@Override
	public void update(long delta) {
		if (isLooping && completed()){
			restart();
		}
		
		particleSystem.update((int)delta);
	}

	/*
	 * @see org.blockout.engine.IAnimation#render(int, int)
	 */
	@Override
	public void render(int x, int y) {
		particleSystem.render(x, y);
	}

	/*
	 * @see org.blockout.engine.IAnimation#completed()
	 */
	@Override
	public boolean completed() {
		// Particle Animation is completed if all effects are completed
		for(int i = 0; i < particleSystem.getEmitterCount(); i++){
			ParticleEmitter emitter = particleSystem.getEmitter(i);
			if(!emitter.completed())
				return false;
		}
		
		return true;
	}

	/*
	 * @see org.blockout.engine.IAnimation#start()
	 */
	@Override
	public void start() {
		particleSystem.setVisible(true);
		particleSystem.removeAllEmitters();
		
		for (ParticleEmitter emitter : effects.values()) {
			emitter.setEnabled(true);
			emitter.resetState();
			particleSystem.addEmitter(emitter);
		}
	}

	/*
	 * @see org.blockout.engine.IAnimation#stop()
	 */
	@Override
	public void stop() {
		particleSystem.setVisible(false);
		particleSystem.removeAllEmitters();
		
		for (ParticleEmitter emitter : effects.values()) {
			emitter.setEnabled(false);
		}
	}

	/*
	 * @see org.blockout.engine.IAnimation#restart()
	 */
	@Override
	public void restart() {
		particleSystem.reset();
		stop();
		start();
	}

	/*
	 * @see org.blockout.engine.IAnimation#setLooping(boolean)
	 */
	@Override
	public void setLooping(boolean looping) {
		isLooping = looping;
	}
	
	/**
	 * Add an effect to this animation
	 * 
	 * @param name
	 * 		Name of the effect
	 * @param effect
	 * 		The effect itself 
	 */
	public void addEffect(String name, ParticleEmitter effect){
		effects.put(name, effect);
	}
	
	/**
	 * Remove an effect from this animation
	 * 
	 * @param name
	 * 		Name of the effect to remove
	 */
	public void removeEffect(String name){
		effects.remove(name);
	}
}
