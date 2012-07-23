package org.blockout.engine;

import java.io.IOException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import com.google.common.base.Preconditions;

/**
 * Implementation of a SpriteSheet to access single sprites inside an image by
 * its id
 * 
 * @author Florian Müller
 * 
 */
public class SpriteSheetImpl implements ISpriteSheet {
    private SpriteSheet spriteSheet;
    private int         numSprites;

    /**
     * Constructor to create an empty SpriteSheet
     */
    public SpriteSheetImpl() {
        spriteSheet = null;
        numSprites = -1;
    }

    /**
     * Constructor to create a new SpriteSheet from an image source with a
     * specific tile size
     * 
     * @param ref
     *            The location of the spritesheet file to load
     * @param tw
     *            Tile width
     * @param th
     *            Tile height
     * @throws IOException
     *             If there is a problem while loading the spritesheet
     * @throws IllegalArgumentException
     *             If <i>tw</i> or <i>th</i> are negative or greater as the
     *             image size.
     */
    public SpriteSheetImpl( final String ref, final int tw, final int th )
            throws IOException, IllegalArgumentException {
        loadSpriteSheet( ref, tw, th );
    }

    /**
     * @see ISpriteSheet#loadSpriteSheet(String, int, int)
     * 
     * @throws IllegalArgumentException
     *             If <i>tw</i> or <i>th</i> are negative or greater as the
     *             image size.
     * 
     * @throws IllegalStateException
     *             If there is no spritesheet loaded
     */
    @Override
    public void loadSpriteSheet( final String ref, final int tw, final int th )
            throws IOException {
        Preconditions.checkNotNull( ref );
        Preconditions.checkArgument( tw > 0, "Tile width has a negative value" );
        Preconditions
                .checkArgument( th > 0, "Tile height has a negative value" );

        Image img;
        try {
            img = new Image( ref );
        } catch ( Throwable t ) {
            throw new IOException( "Failed to load spritesheet: " + ref, t );
        }

        Preconditions.checkArgument( img.getWidth() > tw,
                "Tile width is to big" );
        Preconditions.checkArgument( img.getHeight() > th,
                "Tile height is to big" );

        spriteSheet = new SpriteSheet( img, tw, th );
        numSprites = spriteSheet.getVerticalCount()
                * spriteSheet.getHorizontalCount();
    }

    /**
     * @see ISpriteSheet#isSpriteSheetLoaded()
     */
    @Override
    public boolean isSpriteSheetLoaded() {
        return null != spriteSheet;
    }

    /**
     * @see ISpriteSheet#getSprite(int)
     */
    @Override
    public Image getSprite( final int spriteId ) throws IllegalStateException {
        Preconditions.checkState( isSpriteSheetLoaded(),
                "No spritesheet loaded" );

        if( 0 <= spriteId && numSprites > spriteId ) {
            int sx = spriteId % spriteSheet.getHorizontalCount();
            int sy = spriteId / spriteSheet.getHorizontalCount();
            return spriteSheet.getSprite( sx, sy );
        }

        return null;
    }

    /**
     * @see ISpriteSheet#startUse()
     * 
     * @throws IllegalStateException
     *             If there is no spritesheet loaded
     */
    @Override
    public void startUse() {
        Preconditions.checkState( isSpriteSheetLoaded(),
                "No spritesheet loaded" );

        spriteSheet.startUse();
    }

    /**
     * @see ISpriteSheet#endUse()
     * 
     * @throws IllegalStateException
     *             If there is no spritesheet loaded
     */
    @Override
    public void endUse() {
        Preconditions.checkState( isSpriteSheetLoaded(),
                "No spritesheet loaded" );

        spriteSheet.endUse();
    }

    /**
     * @see ISpriteSheet#renderInUse(int, int, int)
     * 
     * @throws IllegalStateException
     *             If there is no spritesheet loaded
     */
    @Override
    public void renderInUse( int spriteId, int x, int y ) {
        Preconditions.checkState( isSpriteSheetLoaded(),
                "No spritesheet loaded" );

        if( 0 < spriteId && numSprites > spriteId ) {
            int sx = spriteId % spriteSheet.getHorizontalCount();
            int sy = spriteId / spriteSheet.getHorizontalCount();
            spriteSheet.renderInUse( x, y, sx, sy );
        }
    }

    @Override
    public int getSpriteCount() {
        return numSprites;
    }
}
