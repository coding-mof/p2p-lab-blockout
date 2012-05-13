package org.blockout.world.event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Player;

/**
 * Instances of this class represent a movement of a player. Each event
 * represents only a single step of the movement (between two nearby tiles).
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class PlayerMoveEvent implements IEvent<PlayerMoveEvent> {

	protected Player	player;
	protected int		oldX;
	protected int		oldY;
	protected int		newX;
	protected int		newY;

	protected Calendar	localTime;
	protected UUID		id;

	public PlayerMoveEvent(final Player player, final int oldX, final int oldY, final int newX, final int newY) {
		this.player = player;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;

		localTime = Calendar.getInstance();
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

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append( "[" );
		SimpleDateFormat fmt = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss.SSS" );
		buf.append( fmt.format( localTime.getTime() ) );
		buf.append( "] Player moved from (" );
		buf.append( oldX );
		buf.append( "," );
		buf.append( oldY );
		buf.append( ") to (" );
		buf.append( newX );
		buf.append( "," );
		buf.append( newY );
		buf.append( ")." );
		return buf.toString();
	}

	@Override
	public long getDuration() {
		return 500;
	}

	@Override
	public TileCoordinate getResponsibleTile() {
		return new TileCoordinate( newX, newY );
	}
}
