package org.blockout.engine;

import java.io.IOException;

import org.blockout.engine.animation.IAnimation;
import org.blockout.engine.animation.ParticleAnimation;
import org.blockout.engine.animation.effects.SparkleEmitter;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class EngineTestbed extends BasicGame {

    public static void main( final String[] args ) {
        try {
            AppGameContainer app = new AppGameContainer( new EngineTestbed(
                    "EngineTestbed" ) );
            app.setDisplayMode( 640, 480, false );
            app.setAlwaysRender( true );
            app.start();
        } catch ( SlickException e ) {
            e.printStackTrace();
        }
    }

    SpriteManagerImpl spriteManager;
    ISpriteSheet      spriteSheet;
    IAnimation        animation;

    public EngineTestbed( final String title ) {
        super( title );

    }

    @Override
    public void render( final GameContainer arg0, final Graphics arg1 )
            throws SlickException {

        int x = 0;
        int y = 0;

        for ( SpriteType type : SpriteType.values() ) {

            Image img = spriteManager.getSprite( type );

            if( null != img )
                img.draw( x * 32, y * 32 );

            x++;
            if( x == 20 ) {
                x = 0;
                y++;
            }
        }

        animation.render( 100, 100 );
    }

    @Override
    public void init( final GameContainer arg0 ) throws SlickException {
        try {
            spriteManager = new SpriteManagerImpl( new SpriteSheetImpl(
                    "src/main/resources/nethack_spritesheet.jpg", 32, 32 ) );

            spriteSheet = new SpriteSheetImpl(
                    "src/main/resources/blood_splatter.png", 32, 32 );
        } catch ( IllegalArgumentException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        // animation = new SpriteAnimation( spriteSheet, new int[] { 0, 1, 2, 3,
        // 4, 5, 6, 7, 8 }, 1000 );
        animation = new ParticleAnimation();
        ( (ParticleAnimation) animation ).addEffect( "blood",
                new SparkleEmitter( 5000 ) );
        animation.setLooping( true );
        animation.start();

    }

    @Override
    public void update( final GameContainer container, final int delta )
            throws SlickException {

        animation.update( delta );
    }
}
