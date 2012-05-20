package org.blockout.engine;

import java.io.IOException;

import org.blockout.engine.animation.ParticleAnimation;
import org.blockout.engine.animation.effects.ExplosionEmitter;
import org.blockout.engine.sfx.AudioManagerImpl;
import org.blockout.engine.sfx.AudioType;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class EngineTestbed extends BasicGame {

	public static void main( final String[] args ) {
		try {
			AppGameContainer app = new AppGameContainer( new EngineTestbed( "EngineTestbed" ) );
			app.setDisplayMode( 640, 480, false );
			app.setAlwaysRender( true );
			app.start();
		} catch ( SlickException e ) {
			e.printStackTrace();
		}
	}

	ParticleAnimation	animation;
	AudioManagerImpl audioManager;
	
	public EngineTestbed(final String title) {
		super( title );
		
	}

	@Override
	public void render( final GameContainer arg0, final Graphics arg1 ) throws SlickException {
		animation.render( 300, 300 );
	}

	@Override
	public void init( final GameContainer arg0 ) throws SlickException {
		//animation = new ParticleAnimation();
		// animation.addEffect( "fire", new FireEmitter( 0, 0, 10 ) );
		//animation.addEffect( "ring", new ExplosionEmitter( 100 ) );
		
		try {
			animation = new ParticleAnimation("test_system.xml");
		} catch (IOException e) {
			throw new RuntimeException("Failed to load test_system.xml", e);
		}
		animation.setLooping( true );
		animation.start();
		
		audioManager = new AudioManagerImpl();
		//audioManager.getSound(AudioType.sfx_open_chest).play();
		audioManager.getMusic(AudioType.music_irish_meadow).play(1, 0.5f);
	}

	@Override
	public void update( final GameContainer container, final int delta ) throws SlickException {
		animation.update( delta );
	}
}
