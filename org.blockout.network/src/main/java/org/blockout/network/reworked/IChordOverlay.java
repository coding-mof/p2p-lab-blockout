package org.blockout.network.reworked;

import java.io.Serializable;

import org.blockout.network.dht.WrappedRange;
import org.blockout.network.dht.IHash;

/**
 * Abstraction of a chord overlay that provides key lookup functionality as well
 * as message passing.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public interface IChordOverlay {

	/**
	 * Adds a {@link ChordListener} to this overlay that gets notified when the
	 * responsibility changes or messages for this node are received.
	 * 
	 * @param l
	 */
	public void addChordListener( ChordListener l );

	/**
	 * Removes a {@link ChordListener} from this overlay.
	 * 
	 * @param l
	 */
	public void removeChordListener( ChordListener l );

	/**
	 * Returns the current responsibility of the local node. The local node id
	 * is always the upper bound of the range.
	 * 
	 * @return
	 */
	public WrappedRange<IHash> getResponsibility();

	/**
	 * Starts a lookup on the successor of the given key in the overlay. Use the
	 * returned {@link ObservableFuture} to retrieve the result.
	 * 
	 * @param key
	 * @return
	 */
	public ObservableFuture<IHash> findSuccessor( IHash key );

	/**
	 * Routes a given message to the specified nodeId.
	 * 
	 * @param message
	 * @param nodeId
	 */
	public void sendMessage( Serializable message, IHash nodeId );
}
