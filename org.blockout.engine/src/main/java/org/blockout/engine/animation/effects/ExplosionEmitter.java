package org.blockout.engine.animation.effects;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;


/**
 * Explosion like effect for a particle based animation
 * 
 * @author Florian Müller
 *
 */
public class ExplosionEmitter implements ParticleEmitter {

	boolean startExpanding;
	float radius;
	
	float x = 0;
	float y = 0;
	float size = 30;
	int time = 0;
	
	public ExplosionEmitter(float radius) {
		this.radius = radius;
		this.startExpanding = false;
	}
	
	@Override
	public void update(ParticleSystem system, int delta) {
		time += delta;
		
		if (!startExpanding) {
			startExpanding = true;
			
			int num = (int) ((2 * Math.PI * radius) / size);
			float step = 360.0f / num;
			
			for(int i = 0; i < num; i++){
				Particle p = system.getNewParticle(this, 1000);
				p.setPosition(x, y);
				p.setSize(size);
				p.setColor(1, 1, 1, 0.5f);
				
				float vx = (float) (Math.cos(i * step));
				float vy = (float) (Math.sin(i * step));
				p.setVelocity(vx, vy, 0.1f);
			}
		}
	}
	
	@Override
	public void updateParticle(Particle particle, int delta) {
		float c = 0.002f * delta;
		particle.adjustColor(0,-c/2,-c*2,-c/4);
	}

	@Override
	public boolean completed() {
		if (1100 < time) {
			return true;
		}
		
		return false;
	}

	@Override
	public void wrapUp() {}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setEnabled(boolean enabled) {}

	@Override
	public boolean useAdditive() {
		return false;
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public boolean isOriented() {
		return false;
	}

	@Override
	public boolean usePoints(ParticleSystem system) {
		return false;
	}

	@Override
	public void resetState() {
		startExpanding = false;
		time = 0;
	}
}
