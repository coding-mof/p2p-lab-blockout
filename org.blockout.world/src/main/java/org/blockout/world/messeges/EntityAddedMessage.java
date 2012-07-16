package org.blockout.world.messeges;

import org.blockout.common.TileCoordinate;
import org.blockout.network.dht.IHash;
import org.blockout.network.message.IMessage;
import org.blockout.world.entity.Entity;

public class EntityAddedMessage implements IMessage {

	private static final long		serialVersionUID	= 8717749852880026457L;

	private final TileCoordinate	coordinate;
	private final Entity			entity;
	private final IHash				owner;

	public EntityAddedMessage(final Entity entity, final TileCoordinate coordinate, final IHash owner) {
		this.coordinate = coordinate;
		this.entity = entity;
		this.owner = owner;
	}

	public TileCoordinate getCoordinate() {
		return coordinate;
	}

	public Entity getEntity() {
		return entity;
	}

	public IHash getOwner() {
		return owner;
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
		EntityAddedMessage other = (EntityAddedMessage) obj;
		if ( coordinate == null ) {
			if ( other.coordinate != null ) {
				return false;
			}
		} else if ( !coordinate.equals( other.coordinate ) ) {
			return false;
		}
		if ( entity == null ) {
			if ( other.entity != null ) {
				return false;
			}
		} else if ( !entity.equals( other.entity ) ) {
			return false;
		}
		if ( owner == null ) {
			if ( other.owner != null ) {
				return false;
			}
		} else if ( !owner.equals( other.owner ) ) {
			return false;
		}
		return true;
	}

}
