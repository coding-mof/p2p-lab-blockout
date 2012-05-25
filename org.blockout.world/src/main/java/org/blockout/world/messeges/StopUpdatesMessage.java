package org.blockout.world.messeges;

import org.blockout.common.TileCoordinate;
import org.blockout.network.message.IMessage;

public class StopUpdatesMessage implements IMessage {
	
	private static final long serialVersionUID = -4780430950877773335L;
	
	private TileCoordinate coordinate;

	public StopUpdatesMessage(TileCoordinate coordinate) {
		super();
		this.coordinate = coordinate;
	}

	public TileCoordinate getCoordinate() {
		return coordinate;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StopUpdatesMessage other = (StopUpdatesMessage) obj;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		return true;
	};
	
	

}
