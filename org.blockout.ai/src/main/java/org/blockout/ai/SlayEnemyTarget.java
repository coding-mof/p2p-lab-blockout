package org.blockout.ai;

import org.blockout.world.entity.Actor;
import org.blockout.world.state.IStateMachine;

/**
 * AI task that slays a monster. This task internally acts as a listener to the
 * {@link IStateMachine} and blocks until the monster has been slayed.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SlayEnemyTarget implements ITarget {

	protected Actor	enemy;

	public SlayEnemyTarget(final Actor enemy) {
		this.enemy = enemy;
	}

	@Override
	public int hashCode() {
		return ((enemy == null) ? 0 : enemy.hashCode());
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		SlayEnemyTarget other = (SlayEnemyTarget) obj;
		if ( enemy == null ) {
			if ( other.enemy != null ) {
				return false;
			}
		} else if ( !enemy.equals( other.enemy ) ) {
			return false;
		}
		return true;
	}

	@Override
	public void approach() {
		System.out.println( "Attacking monster..." );

	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean achieved() {

		return false;
	}

}
