package org.blockout.network.reworked;

import org.blockout.network.dht.IHash;

import com.google.common.base.Preconditions;

/**
 * Lookup message that gets passed in the ring when a node searches for the
 * successor of a key.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class FindSuccessorMessage extends AbstractMessage {
	private static final long	serialVersionUID	= 3333096766066522554L;
	private final IHash			origin;
	private final IHash			key;

	public FindSuccessorMessage(final IHash origin, final IHash key) {

		Preconditions.checkNotNull( origin );
		Preconditions.checkNotNull( key );

		this.origin = origin;
		this.key = key;
	}

	/**
	 * The key for which we are searching it's successor.
	 * 
	 * @return
	 */
	public IHash getKey() {
		return key;
	}

	/**
	 * The id of the node that searches the successor.
	 * 
	 * @return
	 */
	public IHash getOrigin() {
		return origin;
	}

	@Override
	public String toString() {
		return "FindSuccessorMessage[key=" + getKey() + ", origin=" + getOrigin() + "]";
	}

	@Override
	public IHash getReceiver() {
		return getKey();
	}
}
