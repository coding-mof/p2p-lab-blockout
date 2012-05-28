package org.blockout.network.message;

import java.lang.reflect.InvocationTargetException;

import org.blockout.network.discovery.INodeAddress;

public interface IMessageReceiver {
	public void receive(IMessage msg, INodeAddress origin) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
