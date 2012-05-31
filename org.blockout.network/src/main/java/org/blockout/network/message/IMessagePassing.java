package org.blockout.network.message;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.blockout.network.ConnectionManager;
import org.blockout.network.INodeAddress;
import org.blockout.network.dht.IHash;
import org.jboss.netty.channel.MessageEvent;

public interface IMessagePassing {
	public void send(IMessage msg, INodeAddress recipient);
	public void send(IMessage msg, IHash nodeId);

	public void addReceiver(IMessageReceiver receiver, Class<? extends IMessage>... filterClass);

	public void addReceiver(Set<IMessageReceiver> receiver, Class<? extends IMessage>... filterClass);

	public void removeReceiver(IMessageReceiver receiver, Class<? extends IMessage>... filterClasses);

	public INodeAddress getOwnAddress();

	public void setConnectionManager(ConnectionManager mgr);

	public void messageReceived(MessageEvent e);

	public void notify(IMessage msg, INodeAddress origin)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException;
}
