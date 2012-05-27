package org.blockout.network.demo;

import org.blockout.network.message.IMessage;

public class DemoMessage implements IMessage {
	private static final long serialVersionUID = 1L;
	private final String msg;

	public DemoMessage(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return this.msg;
	}

}
