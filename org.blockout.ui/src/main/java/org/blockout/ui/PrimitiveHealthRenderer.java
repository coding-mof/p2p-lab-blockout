package org.blockout.ui;

import org.blockout.world.LocalGameState;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * TODO: implement a fallback rendering strategy
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class PrimitiveHealthRenderer implements IHealthRenderer {
    private final static int MIN_FILL_DISTANCE = 145;
    private final static int MAX_FILL_DISTANCE = 245;

    private Camera           camera;
    private LocalGameState   gameState;

    private Image            glassSphere;
    private Image            healthSphere;
    private float            currentDistance;

    public PrimitiveHealthRenderer( final Camera camera,
            final LocalGameState gameState ) {
        this.camera = camera;
        this.gameState = gameState;
    }

	@Override
	public void init() {
        String imgName = "glass-sphere-3.png";
        try {
            glassSphere = new Image( imgName );
        } catch ( SlickException e ) {
            throw new RuntimeException(
                    "Couldn't load image '" + imgName + "'", e );
        }

        imgName = "healthsphere.png";
        try {
            healthSphere = new Image( imgName );
        } catch ( SlickException e ) {
            throw new RuntimeException(
                    "Couldn't load image '" + imgName + "'", e );
        }
	}

	@Override
	public void update( final long deltaMillis ) {
        float factor = gameState.getPlayer().getCurrentHealth()
                / (float) gameState.getPlayer().getMaxHealth();

        currentDistance = MIN_FILL_DISTANCE
                + ( MAX_FILL_DISTANCE - MIN_FILL_DISTANCE ) * factor;
	}

	@Override
    public void render() {
        Camera localCam = camera.getReadOnly();
        int centerX = localCam.getWidth() - 10;
        int centerY = localCam.getHeight() + 150;

        if( null != healthSphere ) {
            healthSphere
                    .draw( centerX - currentDistance,
                            centerY - currentDistance, currentDistance * 2,
                            currentDistance * 2 );
        }

        if( null != glassSphere ) {
            glassSphere.drawCentered( centerX, centerY );
        }
    }
}
