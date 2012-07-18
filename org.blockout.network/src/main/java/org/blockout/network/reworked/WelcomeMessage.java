package org.blockout.network.reworked;

import java.io.Serializable;
import java.net.SocketAddress;

import org.blockout.network.dht.IHash;

import com.google.common.base.Preconditions;

/**
 * The welcome message gets send by a node already in the chord ring as response
 * to a {@link IAmMessage}. This message tells the new node what it's successor
 * and predecessor will be.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class WelcomeMessage implements Serializable {
	private static final long	serialVersionUID	= 9178269876442403205L;
	private final IHash			successorId;
	private final SocketAddress	successorAddress;
	private final IHash			lowerBound;

	public WelcomeMessage(final IHash successorId, final SocketAddress successorAddress, final IHash lowerBound) {

		Preconditions.checkNotNull( successorId );
		Preconditions.checkNotNull( successorAddress );
		Preconditions.checkNotNull( lowerBound );

		this.successorId = successorId;
		this.successorAddress = successorAddress;
		this.lowerBound = lowerBound;
	}

	public IHash getSuccessorId() {
		return successorId;
	}

	public SocketAddress getSuccessorAddress() {
		return successorAddress;
	}

	public IHash getLowerBound() {
		return lowerBound;
	}

}
