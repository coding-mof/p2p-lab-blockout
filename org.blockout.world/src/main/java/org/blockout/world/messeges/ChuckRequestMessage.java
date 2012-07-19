package org.blockout.world.messeges;

import org.blockout.common.TileCoordinate;
import org.blockout.network.message.IMessage;
import org.blockout.world.Chunk;

/**
 * Message used request {@link Chunk}s through {@link MessagePassing}
 * 
 * @author key3
 * 
 */
public class ChuckRequestMessage implements IMessage {

	private static final long		serialVersionUID	= 6375258236428327902L;

	private final TileCoordinate	coordinate;

	public ChuckRequestMessage(final TileCoordinate coordinate) {
		this.coordinate = coordinate;
	}

	public TileCoordinate getCoordinate() {
		return coordinate;
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
		ChuckRequestMessage other = (ChuckRequestMessage) obj;
		if ( coordinate == null ) {
			if ( other.coordinate != null ) {
				return false;
			}
		} else if ( !coordinate.equals( other.coordinate ) ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChunkRequestMessage[coord=" + coordinate + "]";
	}
}
