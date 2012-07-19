package org.blockout.world.event;

import org.blockout.common.TileCoordinate;
import org.blockout.world.entity.Player;

import com.google.common.base.Preconditions;

/**
 * Instances of this class represent a movement of a player. Each event
 * represents only a single step of the movement (between two nearby tiles).
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class PlayerMoveEvent extends AbstractEvent<PlayerMoveEvent> {

	private static final long	serialVersionUID	= -8369418703996561782L;
	protected Player			player;
	protected int				oldX;
	protected int				oldY;
	protected int				newX;
	protected int				newY;

	public PlayerMoveEvent(final Player player, final int oldX, final int oldY, final int newX, final int newY) {

		Preconditions.checkNotNull( player );

		this.player = player;
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
	}

	public Player getPlayer() {
		return player;
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
	public int hashCode() {
		return id.hashCode();
	}

	public TileCoordinate getNewPos() {
		return new TileCoordinate( getNewX(), getNewY() );
	}

	public TileCoordinate getOldPos() {
		return new TileCoordinate( getOldX(), getOldY() );
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
		buf.append( "PlayerMoveEvent[player=" + player + ", from=(" );
		buf.append( oldX );
		buf.append( "," );
		buf.append( oldY );
		buf.append( "), to=(" );
		buf.append( newX );
		buf.append( "," );
		buf.append( newY );
		buf.append( "), id=" );
		buf.append( getId() );
		buf.append( "]" );
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
