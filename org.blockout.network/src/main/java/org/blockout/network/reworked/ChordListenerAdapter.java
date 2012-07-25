package org.blockout.network.reworked;

import org.blockout.network.dht.IHash;
import org.blockout.network.dht.WrappedRange;

/**
 * Adapter class for convenient use of the {@link ChordListener} interface.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class ChordListenerAdapter implements ChordListener {

	/**
	 * No-op implementation.
	 */
	@Override
	public void responsibilityChanged( final IChordOverlay chord, final WrappedRange<IHash> from,
			final WrappedRange<IHash> to ) {
	}

	/**
	 * No-op implementation.
	 */
	@Override
	public void predecessorChanged( final IChordOverlay chord, final IHash predecessor ) {
	}

	/**
	 * No-op implementation.
	 */
	@Override
	public void successorChanged( final IChordOverlay chord, final IHash successor ) {
	}

	/**
	 * No-op implementation.
	 */
	@Override
	public void receivedMessage( final IChordOverlay chord, final Object message, final IHash senderId ) {
	}
}
