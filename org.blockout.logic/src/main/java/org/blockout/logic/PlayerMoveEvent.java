package org.blockout.logic;

import java.util.Calendar;
import java.util.UUID;

import org.blockout.common.IEvent;

/**
 * Instances of this class represent a movement of a player. Each event
 * represents only a single step of the movement (between two nearby tiles).
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class PlayerMoveEvent implements IEvent<PlayerMoveEvent> {

	// TODO: add player id or instance
	protected int		oldX;
	protected int		oldY;
	protected int		newX;
	protected int		newY;

	protected Calendar	localTime;
	protected UUID		id;

	public PlayerMoveEvent(final int oldX, final int oldY, final int newX, final int newY) {
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;

		localTime = Calendar.getInstance();
		id = UUID.randomUUID();
	}

	private PlayerMoveEvent(final int oldX, final int oldY, final int newX, final int newY, final Calendar localTime) {
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;

		this.localTime = localTime;
		id = UUID.randomUUID();
	}

	public int getOldX() {
		return oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public int getNewX() {
		return newX;
	}

	public int getNewY() {
		return newY;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public Calendar getLocalTime() {
		return localTime;
	}

	@Override
	public PlayerMoveEvent getInverse() {
		return new PlayerMoveEvent( newX, newY, oldX, oldY, localTime );
	}

	@Override
	public boolean isInverseOf( final IEvent<PlayerMoveEvent> event ) {
		// backward compatibility to Java 1.5
		if ( !(event instanceof PlayerMoveEvent) ) {
			return false;
		}

		PlayerMoveEvent pme = (PlayerMoveEvent) event;
		return (pme.getNewX() == getOldX() && pme.getNewY() == getOldY() && pme.getOldX() == getNewX()
				&& pme.getOldY() == getNewY() && pme.getLocalTime().equals( getLocalTime() ));
	}

	@Override
	public int hashCode() {
		return id.hashCode();
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
		PlayerMoveEvent other = (PlayerMoveEvent) obj;
		if ( id == null ) {
			if ( other.id != null ) {
				return false;
			}
		} else if ( !id.equals( other.id ) ) {
			return false;
		}
		return true;
	}

}
