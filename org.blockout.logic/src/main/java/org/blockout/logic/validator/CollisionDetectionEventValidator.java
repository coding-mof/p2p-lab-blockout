package org.blockout.logic.validator;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.IWorld;
import org.blockout.world.Tile;
import org.blockout.world.event.PlayerMoveEvent;
import org.blockout.world.state.IEventValidator;
import org.blockout.world.state.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for an event validator which checks {@link PlayerMoveEvent}s
 * for collisions.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class CollisionDetectionEventValidator implements IEventValidator {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( CollisionDetectionEventValidator.class );
	}

	protected IWorld			world;

	@Inject
	public CollisionDetectionEventValidator(final IWorld world) {
		this.world = world;
	}

	@Override
	public ValidationResult validateEvent( final IEvent<?> event ) {
		if ( event instanceof PlayerMoveEvent ) {
			PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent) event;
			Tile tile = world.getTile( playerMoveEvent.getNewX(), playerMoveEvent.getNewY() );
			if ( tile == null ) {
				return ValidationResult.Invalid;
			}
			// Player can't walk on walls
			if ( tile.getHeight() > Tile.GROUND_HEIGHT ) {
				if ( logger.isDebugEnabled() ) {
					logger.debug( "Tile to high: " + tile.getHeight() + ", Id: " + tile.getTileType() );
				}
				return ValidationResult.Invalid;
			}
			// Player can't walk over other objects (Players, Monsters, Crates,
			// etc.)
			if ( tile.getEntityOnTile() != null ) {
				if ( logger.isDebugEnabled() ) {
					logger.debug( "Tile blocked by object: " + tile.getEntityOnTile() );
				}
				return ValidationResult.Invalid;
			}
			return ValidationResult.Valid;
		}
		return ValidationResult.Unknown;
	}

}
