package org.blockout.logic.handler;

import javax.inject.Inject;

import org.blockout.engine.AnimationManager;
import org.blockout.engine.animation.ParticleAnimation;
import org.blockout.engine.animation.effects.SparkleEmitter;
import org.blockout.world.IWorld;
import org.blockout.world.event.CrateCreatedEvent;
import org.blockout.world.event.IEvent;
import org.blockout.world.state.IStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrateCreatorHandler implements IEventHandler {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( CrateHandler.class );
	}

	protected IWorld			world;
	private AnimationManager	animationManager;

	@Inject
	public CrateCreatorHandler(final IWorld world) {
		this.world = world;
	}

	public void setAnimationManager( final AnimationManager animationManager ) {
		this.animationManager = animationManager;
	}

	@Override
	public void eventStarted( final IStateMachine stateMachine, final IEvent<?> event ) {
	}

	@Override
	public void eventFinished( final IStateMachine stateMachine, final IEvent<?> event ) {
		if ( !(event instanceof CrateCreatedEvent) ) {
			return;
		}

		CrateCreatedEvent evt = (CrateCreatedEvent) event;

		world.setEnityPosition( evt.getCrate(), evt.getResponsibleTile() );
		if ( null != animationManager ) {
			ParticleAnimation animation = new ParticleAnimation();
			animation.addEffect( "sparkles", new SparkleEmitter( 3500 ) );
			animationManager.addAnimation( animation, evt.getResponsibleTile() );
		}
	}
}