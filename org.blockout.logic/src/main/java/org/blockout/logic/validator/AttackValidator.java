package org.blockout.logic.validator;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.TileCoordinate;
import org.blockout.world.IWorld;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.event.IEvent;
import org.blockout.world.state.IEventValidator;
import org.blockout.world.state.ValidationResult;

@Named
public class AttackValidator implements IEventValidator {

	// might depend on the weapon type in later versions
	private static final float	ATTACK_DISTANCE			= 1f;
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
			return ValidationResult.Invalid;
		}

		TileCoordinate aggressorTile = world.findTile( e.getAggressor() );
		TileCoordinate victimTile = world.findTile( e.getVictim() );
		if ( TileCoordinate.computeSquaredEuclidianDistance( aggressorTile, victimTile ) <= SQUARED_ATTACK_DISTANCE ) {
			return ValidationResult.Valid;
		}

		return ValidationResult.Invalid;
	}
}
