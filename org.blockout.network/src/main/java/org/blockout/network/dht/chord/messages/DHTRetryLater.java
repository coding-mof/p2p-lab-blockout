package org.blockout.network.dht.chord.messages;

import java.util.concurrent.TimeUnit;

import org.blockout.network.INodeAddress;
import org.blockout.network.message.IMessage;

public class DHTRetryLater implements IMessage {

	private static final long serialVersionUID = -7192042080060206269L;
	public final int maxCount;
	private IMessage orig;
	private INodeAddress receiver;
	private long delay;
	private TimeUnit unit;
	private int count;

	public DHTRetryLater(IMessage orig, INodeAddress receiver, int maxCount,
			long delay, TimeUnit unit) {
		this.maxCount = maxCount;
		this.orig = orig;
		this.receiver = receiver;
		this.delay = delay;
		this.unit = unit;
		this.count = 0;
	}

	protected DHTRetryLater(IMessage orig, INodeAddress receiver, int maxCount,
			long delay, TimeUnit unit, int count) {
		this.maxCount = maxCount;
		this.orig = orig;
		this.receiver = receiver;
		this.delay = delay;
		this.unit = unit;
		this.count = count;
	}

	public int getCount() {
		return this.count;
	}

	public IMessage getOriginalMessage() {
		return this.orig;
	}

	public INodeAddress getOriginalReceiver() {
		return this.receiver;
	}

	public long getDelay() {
		return this.delay;
	}

	public TimeUnit getUnit() {
		return this.unit;
	}

	public DHTRetry getRetryMessage() {
		return new DHTRetry(this.orig, this.receiver, this.delay, this.unit,
				this.maxCount, this.count + 1);
	}

}
