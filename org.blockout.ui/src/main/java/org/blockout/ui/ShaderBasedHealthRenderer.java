package org.blockout.ui;

import org.blockout.engine.Shader;
import org.blockout.world.LocalGameState;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * This class is responsible for rendering the current health of the own player.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class ShaderBasedHealthRenderer implements IHealthRenderer {

	private final static int	MIN_FILL_DISTANCE	= 145 * 145;
	private final static int	MAX_FILL_DISTANCE	= 245 * 245;

	// Dependencies
	protected Camera			camera;
	protected LocalGameState	gameState;

	// Own state
	protected Image				healthSphere;
	protected Shader			shader;

	private float				currentDistance;
	private float				currentRadians;
	private float				currentGlowFactor	= 1.0f;

	public ShaderBasedHealthRenderer(final Camera camera, final LocalGameState gameState) {
		this.camera = camera;
		this.gameState = gameState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockout.ui.IHealthRenderer#init()
	 */
	@Override
	public void init() {
		if ( healthSphere == null ) {
			String imgName = "glass-sphere-3.png";
			try {
				healthSphere = new Image( imgName );
			} catch ( SlickException e ) {
				throw new RuntimeException( "Couldn't load image '" + imgName + "'", e );
			}
		}

		if ( shader == null ) {
			try {
				shader = Shader.makeShader( "plain.vs", "health.fs" );
			} catch ( SlickException e ) {
				throw new UnsupportedOperationException( "Shaders seems to be unavailable.", e );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockout.ui.IHealthRenderer#update(long)
	 */
	@Override
	public void update( final long deltaMillis ) {
		float factor = gameState.getPlayer().getCurrentHealth() / (float) gameState.getPlayer().getMaxHealth();
		currentDistance = MIN_FILL_DISTANCE + (MAX_FILL_DISTANCE - MIN_FILL_DISTANCE) * factor;

		if ( factor < 0.25f ) {
			currentRadians += Math.PI * deltaMillis / 1000f;
			if ( currentRadians > (Math.PI * 2) ) {
				currentRadians -= (Math.PI * 2);
			}
			currentGlowFactor = Math.abs( (float) Math.sin( currentRadians ) ) + 0.4f;
		} else {
			currentGlowFactor = 1.0f;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.blockout.ui.IHealthRenderer#render()
	 */
	@Override
	public void render() {
		init();
		int width = 0;
		int height = 0;
		Camera localCamera = camera.getReadOnly();

		width = localCamera.getWidth();
		height = localCamera.getHeight();

		shader.setUniform2FVariable( "SphereCenter", width - 10, -150 );
		shader.setUniformFVariable( "FillSquaredDistance", currentDistance );
		shader.setUniformFVariable( "GlowSquaredDistance",
				(float) Math.pow( Math.sqrt( currentDistance ) + (currentGlowFactor * 10), 2 ) );
		shader.setUniformFVariable( "MaxDist", MAX_FILL_DISTANCE );
		shader.setUniform3FVariable( "Color", 1, 0, 0 );

		shader.startShader();
		healthSphere.drawCentered( width - 10, height + 150 );

		Shader.forceFixedShader();
	}
}
