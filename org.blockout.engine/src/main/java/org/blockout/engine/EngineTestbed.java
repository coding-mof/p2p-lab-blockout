package org.blockout.engine;

import java.io.IOException;

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
    }

    @Override
    public void init( final GameContainer arg0 ) throws SlickException {
        try {
            spriteManager = new SpriteManagerImpl( new SpriteSheetImpl(
                    "src/main/resources/nethack_spritesheet.jpg", 32, 32 ) );
        } catch ( IllegalArgumentException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void update( final GameContainer container, final int delta )
            throws SlickException {
    }
}
