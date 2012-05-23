package org.blockout.network.message;

import java.lang.reflect.InvocationTargetException;

import org.blockout.network.INodeAddress;

public class MessageReceiver implements IMessageReceiver {

	@Override
	public void receive(IMessage msg, INodeAddress origin) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<? extends IMessage> msgClass = msg.getClass();
		this.getClass().getMethod("receive", msgClass, INodeAddress.class).invoke(this, msg, origin);	
	}
}
