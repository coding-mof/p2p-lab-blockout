package org.blockout.network.reworked;

import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;

/**
 * Implement this interface to listen on events of a {@link IChordOverlay}.
 * 
 * @author Marc-Christian Schulze
 * @see IChordOverlay#addChordListener(ChordListener)
 */
public interface ChordListener {

	/**
	 * Gets invoked when the responsibility of the local node changed.
	 * 
	 * @param chord
	 *            A reference to the notifying overlay.
	 * @param from
	 *            The prior responsibility.
	 * @param to
	 *            The new responsibility.
	 */
	public void responsibilityChanged( IChordOverlay chord, WrappedRange<IHash> from, WrappedRange<IHash> to );

    /**
     * Gets invoked when the predecessor of the local node changed.
     * 
     * @param chord
     *            A reference to the notifying overlay.
     * @param predecessor
     *            The new predecessor
     */
    public void predecessorChanged( IChordOverlay chord, IHash predecessor );

    /**
     * Gets invoked when the successor of the local node changed.
     * 
     * @param chord
     *            A reference to the notifying overlay.
     * @param successor
     *            The new successor
     */
    public void successorChanged( IChordOverlay chord, IHash successor );

    /**
     * Gets invoked when a messages has been received that is not related to the
     * internal chord overlay management.
     * 
     * @param chord
     *            A reference to the notifying overlay.
     * @param message
     *            The received message.
     * @param senderId
     *            The node id of the sender.
     */
	public void receivedMessage( IChordOverlay chord, Object message, IHash senderId );
}
