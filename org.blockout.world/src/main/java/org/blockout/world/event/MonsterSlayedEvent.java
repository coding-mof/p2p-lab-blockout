package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Actor;
import org.blockout.world.entity.Monster;

import com.google.common.base.Preconditions;

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
	protected TileCoordinate	deathCoord;

	public MonsterSlayedEvent(final Actor actor, final Monster monster, final TileCoordinate deathCoord) {

		Preconditions.checkNotNull( actor );
		Preconditions.checkNotNull( monster );

		this.actor = actor;
		this.monster = monster;
		this.deathCoord = deathCoord;
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
		return deathCoord;
	}

	@Override
	public String toString() {
		return "MonsterSlayedEvent[attacker=" + actor + ", victim=" + monster + ", id=" + getId() + "]";
	}
}
