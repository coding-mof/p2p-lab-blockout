package org.blockout.engine;

import org.blockout.engine.animation.ParticleAnimation;
import org.blockout.engine.animation.effects.ExplosionEmitter;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

public class EngineTestbed extends BasicGame {

	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new EngineTestbed("EngineTestbed"));
			app.setDisplayMode(640, 480, false);
			app.setAlwaysRender(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	ParticleAnimation animation;
	ParticleSystem particleSystem;
	
	public EngineTestbed(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		animation.render(300, 300);
		particleSystem.render(200, 100);
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		animation = new ParticleAnimation();
		//animation.addEffect("fire", new FireEmitter(0, 0, 10));
		animation.addEffect("ring", new ExplosionEmitter(100));
		animation.setLooping(true);
		animation.start();
		
		particleSystem = new ParticleSystem("particle.tga");
		particleSystem.addEmitter(new FireEmitter());
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		animation.update(delta);
		particleSystem.update(delta);
	}
}
