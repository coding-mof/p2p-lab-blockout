package org.blockout.world.messeges;

import org.blockout.common.TileCoordinate;
import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;
import org.blockout.world.entity.Entity;
import org.blockout.world.entity.Player;

public class EntityAddedMessage implements IMessage {

	private static final long serialVersionUID = 8717749852880026457L;
	
	private TileCoordinate coordinate;
	private Entity entity;
	private INodeAddress owner;
	
	public EntityAddedMessage(Entity entity, TileCoordinate coordinate, INodeAddress owner) {
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

	public INodeAddress getOwner() {
		return owner;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityAddedMessage other = (EntityAddedMessage) obj;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	
	
}
