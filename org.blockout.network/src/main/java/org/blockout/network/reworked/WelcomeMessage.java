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
	private final IHash			predecessorId;
	private final SocketAddress	predecessorAddress;

	public WelcomeMessage(final IHash successorId, final SocketAddress successorAddress, final IHash predecessorId,
			final SocketAddress predecessorAddress) {

		Preconditions.checkNotNull( successorId );
		Preconditions.checkNotNull( successorAddress );

		this.successorId = successorId;
		this.successorAddress = successorAddress;
		this.predecessorId = predecessorId;
		this.predecessorAddress = predecessorAddress;
	}

	public IHash getSuccessorId() {
		return successorId;
	}

	public SocketAddress getSuccessorAddress() {
		return successorAddress;
	}

	/**
	 * Returns the predeccesor's id - if known. Otherwise null.
	 * 
	 * @return
	 */
	public IHash getPredecessorId() {
		return predecessorId;
	}

	/**
	 * Returns the predecessor's address - if known. Otherwise null.
	 * 
	 * @return
	 */
	public SocketAddress getPredecessorAddress() {
		return predecessorAddress;
	}
}
