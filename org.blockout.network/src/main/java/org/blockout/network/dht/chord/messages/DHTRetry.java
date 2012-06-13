package org.blockout.network.dht.chord.messages;

import java.util.concurrent.TimeUnit;

import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;

public class DHTRetry implements IMessage {

	private static final long serialVersionUID = -136161223902134682L;
	private IMessage orig;
	private INodeAddress receiver;
	private long delay;
	private TimeUnit unit;
	private int maxCount;
	private int count;

	public DHTRetry(IMessage orig, INodeAddress receiver, long delay,
			TimeUnit unit, int maxCount, int i) {
		this.orig = orig;
		this.receiver = receiver;
		this.delay = delay;
		this.unit = unit;
		this.maxCount = maxCount;
		this.count = i;
	}

	public IMessage getOrig() {
		return this.orig;
	}

	public INodeAddress getReceiver() {
		return this.receiver;
	}

	public long getDelay() {
		return this.delay;
	}

	public TimeUnit getTimeUnit() {
		return this.unit;
	}

	public int getMaxCount() {
		return this.maxCount;
	}

	public int getCount() {
		return this.count;
	}

	public DHTRetryLater getRetryLaterMessage() {
		return new DHTRetryLater(this.orig, this.receiver, this.maxCount,
				this.delay, this.unit, this.count);
	}
}
