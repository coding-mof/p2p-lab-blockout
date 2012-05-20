package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Actor;

/**
 * AttackEvents are generated when Actors fight each other.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class AttackEvent extends AbstractEvent<AttackEvent> {

	private static final long	serialVersionUID	= -7611677686159435889L;
	protected Actor				aggressor;
	protected Actor				victim;

	public AttackEvent(final Actor aggressor, final Actor victim) {
		this.aggressor = aggressor;
		this.victim = victim;
	}

	/**
	 * Returns the aggressor who started the attack.
	 * 
	 * @return The aggressor who started the attack.
	 */
	public Actor getAggressor() {
		return aggressor;
	}

	/**
	 * Returns the victim who gets attacked.
	 * 
	 * @return The victim who gets attacked.
	 */
	public Actor getVictim() {
		return victim;
	}

	@Override
	public long getDuration() {
		return 200;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		// TODO Auto-generated method stub
		return null;
	}
}
