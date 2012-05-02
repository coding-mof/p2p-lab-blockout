package org.blockout.logic;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.IWorld;
import org.blockout.world.Tile;
import org.blockout.world.state.IEventValidator;
import org.blockout.world.state.ValidationResult;

/**
 * Implementation for an event validator which checks {@link PlayerMoveEvent}s
 * for collisions.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class CollisionDetectionEventValidator implements IEventValidator {

	protected IWorld	world;

	@Inject
	public CollisionDetectionEventValidator(final IWorld world) {
		this.world = world;
	}

	@Override
	public ValidationResult validateEvent( final IEvent<?> event ) {
		if ( event instanceof PlayerMoveEvent ) {
			PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent) event;
			Tile tile = world.getTile( playerMoveEvent.getNewX(), playerMoveEvent.getNewY() );
			// Player can't walk on walls
			if ( tile.getHeight() > Tile.GROUND_HEIGHT ) {
				return ValidationResult.Invalid;
			}
			// Player can't walk over other objects (Players, Monsters, Crates,
			// etc.)
			if ( tile.getObjectOnTile() != null ) {
				return ValidationResult.Invalid;
			}
			return ValidationResult.Valid;
		}
		return ValidationResult.Unknown;
	}

}
