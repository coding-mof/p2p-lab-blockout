package org.blockout.logic.validator;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.common.IEvent;
import org.blockout.world.IWorld;
import org.blockout.world.event.AttackEvent;
import org.blockout.world.state.IEventValidator;
import org.blockout.world.state.ValidationResult;

@Named
public class AttackValidator implements IEventValidator {

	protected IWorld	world;

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

		// TODO Auto-generated method stub
		return null;
	}

}
