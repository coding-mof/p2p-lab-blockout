/**
 * 
 */
package org.blockout.engine.animation;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to create a particle based animation
 * 
 * @author Florian Müller
 *
 */
public class ParticleAnimation implements IAnimation {
	private static final Logger LOG = LoggerFactory.getLogger(ParticleAnimation.class);
	
	private ParticleSystem particleSystem = null;
	private boolean isLooping = false;
	private HashMap<String, ParticleEmitter> effects = new HashMap<String, ParticleEmitter>();
	
	/**
	 * Constructor to create a default configured particle animation
	 */
	public ParticleAnimation() {
		particleSystem = new ParticleSystem("particle.tga");
		particleSystem.setRemoveCompletedEmitters(true);
	}
	
	/**
	 * Constructor to create a particle animation with a other default image
	 * 
	 * @param defaultImage
	 * 		New default particle image
	 */
	public ParticleAnimation(final Image defaultImage) {
		particleSystem = new ParticleSystem(defaultImage);
		particleSystem.setRemoveCompletedEmitters(true);
	}
	
	
	/**
	 * Constructor to load a particle animation from a 'ParticleSystem' XML file
	 * created with pedigree {@link http://slick.cokeandcode.com/demos/pedigree.jnlp} 
	 * 
	 * @param ref
	 * 		Path to XML file
	 * @throws IOException
	 * 		If the file not exists or if parsing fails
	 */
	public ParticleAnimation(final String ref) throws IOException{
		particleSystem = ParticleIO.loadConfiguredSystem(ref);
		particleSystem.setRemoveCompletedEmitters(true);
		
		// save effects
		for(int i = 0; i < particleSystem.getEmitterCount(); i++){
			ConfigurableEmitter emitter = (ConfigurableEmitter) particleSystem.getEmitter(i);
			effects.put(emitter.name, emitter);
		}
	}
	
	/**
	 * Constructor to create a particle animation with a modified max particle count
	 * 
	 * @param maxParticles
	 * 		Maximum amount of particles used with this animation
	 */
	public ParticleAnimation(final int maxParticles){
		particleSystem = new ParticleSystem("particle.tga", maxParticles);
		particleSystem.setRemoveCompletedEmitters(true);
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
	public ParticleAnimation(final Image defaultImage, final int maxParticles) {
		particleSystem = new ParticleSystem(defaultImage, maxParticles);
		particleSystem.setRemoveCompletedEmitters(false);
	}
	
	/*
	 * @see org.blockout.engine.IAnimation#update(long)
	 */
	@Override
	public void update(final long delta) {
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
	public void setLooping(final boolean looping) {
		isLooping = looping;
	}
	
    @Override
    public boolean isLooping() {
        return isLooping;
    }

	/**
	 * Add an effect to this animation
	 * 
	 * @param name
	 * 		Name of the effect
	 * @param effect
	 * 		The effect itself 
	 */
	public void addEffect(final String name, final ParticleEmitter effect){
		if(effects.containsKey(name))
			LOG.warn("override effect '" + name  + "'");
		
		effects.put(name, effect);
	}
	
	/**
	 * Load an effect from a 'ParticleEmitter' XML file
	 * created with pedigree {@link http://slick.cokeandcode.com/demos/pedigree.jnlp}  
	 * 
	 * @param ref
	 * 		Path to XML file
	 * @throws IOException
	 * 		If the file not exists or if parsing fails
	 */
	public void addEffect( final String ref) throws IOException{
		ConfigurableEmitter effect = ParticleIO.loadEmitter(ref);
		
		if(effects.containsKey(effect.name))
			LOG.warn("override effect '" + effect.name  + "'");
		
		effects.put(effect.name, effect);
	}
	
	/**
	 * Remove an effect from this animation
	 * 
	 * @param name
	 * 		Name of the effect to remove
	 */
	public void removeEffect(final String name){
		effects.remove(name);
	}
}
