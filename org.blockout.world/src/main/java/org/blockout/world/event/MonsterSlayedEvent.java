package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Actor;
import org.blockout.world.entity.Monster;

/**
 * Events of this class are generated when a monster has been slayed by an
 * {@link Actor}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class MonsterSlayedEvent extends AbstractEvent<MonsterSlayedEvent> {

	private static final long	serialVersionUID	= 5513405449741434082L;
	protected Monster			monster;
	protected Actor				actor;

	public MonsterSlayedEvent(final Actor actor, final Monster monster) {
		this.actor = actor;
		this.monster = monster;
	}

	/**
	 * Returns the monster that has been slayed.
	 * 
	 * @return The monster that has been slayed.
	 */
	public Monster getMonster() {
		return monster;
	}

	/**
	 * Returns the actor that has slayed the monster.
	 * 
	 * @return The actor that has slayed the monster.
	 */
	public Actor getActor() {
		return actor;
	}

	@Override
	public long getDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return actor + " slayed " + monster;
	}
}
