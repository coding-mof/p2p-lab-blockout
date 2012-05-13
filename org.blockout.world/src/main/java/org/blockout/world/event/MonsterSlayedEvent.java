package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Actor;
import org.blockout.world.entity.Monster;

public class MonsterSlayedEvent extends AbstractEvent<MonsterSlayedEvent> {

	protected Monster	monster;
	protected Actor		actor;

	public MonsterSlayedEvent(final Actor actor, final Monster monster) {
		this.actor = actor;
		this.monster = monster;
	}

	public Monster getMonster() {
		return monster;
	}

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
