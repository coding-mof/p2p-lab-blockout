package org.blockout.logic;

import org.blockout.common.IEvent;
import org.blockout.world.IEventValidator;
import org.blockout.world.IWorld;
import org.blockout.world.ValidationResult;

/**
 * Implementation for an event validator which checks movement events for
 * collisions.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class CollisionDetectionEventValidator implements IEventValidator {

	public CollisionDetectionEventValidator(final IWorld world) {

	}

	@Override
	public ValidationResult validateEvent( final IEvent event ) {
		// TODO Auto-generated method stub
		return null;
	}

}
