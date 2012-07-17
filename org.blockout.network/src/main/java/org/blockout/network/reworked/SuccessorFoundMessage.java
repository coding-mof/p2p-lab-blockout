package org.blockout.network.reworked;

import java.io.Serializable;

import org.blockout.network.dht.IHash;

import com.google.common.base.Preconditions;

/**
 * Response message to a successor lookup.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class SuccessorFoundMessage implements Serializable {

	private static final long	serialVersionUID	= 7215804868666735216L;
	private final IHash			destination;
	private final IHash			key;
	private final IHash			successor;

	public SuccessorFoundMessage(final IHash destination, final IHash key, final IHash successor) {

		Preconditions.checkNotNull( destination );
		Preconditions.checkNotNull( key );
		Preconditions.checkNotNull( successor );

		this.destination = destination;
		this.key = key;
		this.successor = successor;
	}

	/**
	 * The id of the node that has looked for the successor.
	 * 
	 * @return
	 */
	public IHash getDestination() {
		return destination;
	}

	/**
	 * The key we searched for it's successor.
	 * 
	 * @return
	 */
	public IHash getKey() {
		return key;
	}

	/**
	 * The successor's id.
	 * 
	 * @return
	 */
	public IHash getSuccessor() {
		return successor;
	}
}
