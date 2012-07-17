package org.blockout.network.dht.chord.messages;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import org.blockout.network.message.IMessage;

public class DHTRetry implements IMessage {

	private static final long	serialVersionUID	= -136161223902134682L;
	private final IMessage		orig;
	private final SocketAddress	receiver;
	private final long			delay;
	private final TimeUnit		unit;
	private final int			maxCount;
	private final int			count;

	public DHTRetry(final IMessage orig, final SocketAddress receiver, final long delay, final TimeUnit unit,
			final int maxCount, final int i) {
		this.orig = orig;
		this.receiver = receiver;
		this.delay = delay;
		this.unit = unit;
		this.maxCount = maxCount;
		count = i;
	}

	public IMessage getOrig() {
		return orig;
	}

	public SocketAddress getReceiver() {
		return receiver;
	}

	public long getDelay() {
		return delay;
	}

	public TimeUnit getTimeUnit() {
		return unit;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public int getCount() {
		return count;
	}

	public DHTRetryLater getRetryLaterMessage() {
		return new DHTRetryLater( orig, receiver, maxCount, delay, unit, count );
	}
}
