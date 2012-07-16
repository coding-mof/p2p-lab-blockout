package org.blockout.network.dht.chord.messages;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import org.blockout.network.message.IMessage;

public class DHTRetryLater implements IMessage {

	private static final long	serialVersionUID	= -7192042080060206269L;
	public final int			maxCount;
	private final IMessage		orig;
	private final SocketAddress	receiver;
	private final long			delay;
	private final TimeUnit		unit;
	private final int			count;

	public DHTRetryLater(final IMessage orig, final SocketAddress receiver, final int maxCount, final long delay,
			final TimeUnit unit) {
		this.maxCount = maxCount;
		this.orig = orig;
		this.receiver = receiver;
		this.delay = delay;
		this.unit = unit;
		count = 0;
	}

	protected DHTRetryLater(final IMessage orig, final SocketAddress receiver, final int maxCount, final long delay,
			final TimeUnit unit, final int count) {
		this.maxCount = maxCount;
		this.orig = orig;
		this.receiver = receiver;
		this.delay = delay;
		this.unit = unit;
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public IMessage getOriginalMessage() {
		return orig;
	}

	public SocketAddress getOriginalReceiver() {
		return receiver;
	}

	public long getDelay() {
		return delay;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public DHTRetry getRetryMessage() {
		return new DHTRetry( orig, receiver, delay, unit, maxCount, count + 1 );
	}

}
