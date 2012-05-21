package org.blockout.logic.validator;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.world.IWorld;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.event.IEvent;
import org.blockout.world.state.IEventValidator;
import org.blockout.world.state.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validator for {@link AttackEvent}s.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class AttackValidator implements IEventValidator {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( AttackValidator.class );
	}

	// might depend on the weapon type in later versions
	private static final float	ATTACK_DISTANCE			= 1.5f;
	private static final float	SQUARED_ATTACK_DISTANCE	= ATTACK_DISTANCE * ATTACK_DISTANCE;
	protected IWorld			world;

	@Inject
	public AttackValidator(final IWorld world) {
		this.world = world;
	}

	@Override
	public ValidationResult validateEvent( final IEvent<?> event ) {
		if ( !(event instanceof AttackEvent) ) {
			return ValidationResult.Unknown;
		}

		AttackEvent e = (AttackEvent) event;
		if ( e.getVictim() == e.getAggressor() ) {
			logger.debug( "Attack denied since you can't attack yourself." );
			return ValidationResult.Invalid;
		}

		TileCoordinate aggressorTile = world.findTile( e.getAggressor() );
		TileCoordinate victimTile = world.findTile( e.getVictim() );
		float distance = TileCoordinate.computeSquaredEuclidianDistance( aggressorTile, victimTile );
		if ( distance > SQUARED_ATTACK_DISTANCE ) {
			logger.debug( "Attack denied since distance " + distance + " is larger than " + SQUARED_ATTACK_DISTANCE );
			return ValidationResult.Invalid;
		}

		return ValidationResult.Valid;
	}
}
