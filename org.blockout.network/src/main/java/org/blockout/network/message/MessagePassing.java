package org.blockout.network.message;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Named;

import org.blockout.network.INodeAddress;
import org.blockout.network.NodeInfo;
import org.blockout.network.dht.IDistributedHashTable;
import org.blockout.network.dht.IHash;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

@Named
public class MessagePassing extends SimpleChannelUpstreamHandler implements IMessagePassing{
	public InetSocketAddress address;
	public INodeAddress nodeAddress;
	protected Multimap<Class<? extends IMessage>, IMessageReceiver> filtredReceivers;
	protected Hashtable<IHash, Channel> channelRegister;
	protected IDistributedHashTable dht;
	
	private ChannelPipelineFactory pipelinefactory;
	private ClientBootstrap clientBootstrap;
	
	@Inject
	public MessagePassing(IDistributedHashTable dht){
		this.filtredReceivers = HashMultimap.create();
		this.channelRegister = new Hashtable<IHash, Channel>();
		this.dht = dht;
		
		address = new InetSocketAddress(0);
		
		this.setUp();
	}
	
	private void setUp() {
		final MessagePassing that = this;
		// Set up the pipeline factory.
		pipelinefactory = new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(
								ClassResolvers.cacheDisabled(getClass().getClassLoader())
								),
						that
						);
			}
		};
		// Setup the Client Bootstrap
		clientBootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		clientBootstrap.setPipelineFactory(pipelinefactory);
				 
				 
		
		// Setup the Server
		ServerBootstrap bootstrap = new ServerBootstrap(
                  	new NioServerSocketChannelFactory(
                          Executors.newCachedThreadPool(),
                          Executors.newCachedThreadPool()));
		

		bootstrap.setPipelineFactory(pipelinefactory);
				
		// Bind and start to accept incoming connections.
		Channel serverChannel = bootstrap.bind(address);
		
		// Replace the abstract InetSocketAdress with the actual address
		this.address = (InetSocketAddress) serverChannel.getLocalAddress();		
		this.nodeAddress = new NodeInfo(this.address);
		
		Runnable future = new Runnable(){
			@Override
			public void run() {
				that.dht.setUp(that, that.nodeAddress);	
			}
		};
		Executors.newCachedThreadPool().execute(future);	
	}

	public List<NodeInfo> listNodes(){
		ArrayList<NodeInfo> nodes = new ArrayList<NodeInfo>();
		for(Channel chan: channelRegister.values()){
			nodes.add(new NodeInfo((InetSocketAddress)chan.getRemoteAddress()));
		}
		return nodes;
	}
	
	@Override
	public void send(final IMessage msg, final INodeAddress recipient) {
		final MessagePassing that = this;
		final IMessageEnvelope<IMessage> envelope = new MessageEnvelope<IMessage>(msg, recipient, this.nodeAddress);
		Runnable future = new Runnable() {
			@Override
			public void run() {
				Channel chan;
				IHash nodeId = recipient.getNodeId();
				if(!channelRegister.contains(nodeId)){
					if(recipient.getInetAddress() == null){
						throw new IllegalArgumentException("Channel for Peer with ID "+nodeId.getValue()+" not found.");
					}else{
						chan = that.createChannel(recipient);
					}
				}else{
					chan = channelRegister.get(nodeId);
				}
				chan.write(envelope);
				
			}
		};
		Executors.newCachedThreadPool().execute(future);
	}

	private Channel createChannel(INodeAddress recipient) {
		Preconditions.checkNotNull(recipient);
		Preconditions.checkNotNull(recipient.getInetAddress());
		// Make a new connection.
		ChannelFuture connectFuture = clientBootstrap.connect(recipient.getInetAddress());

		// Wait until the connection is made successfully.
		Channel channel = connectFuture.awaitUninterruptibly().getChannel();
		
		channelRegister.put(recipient.getNodeId(), channel);		
		return channel;
	}

	@Override
	public void send(IMessage msg, IHash nodeId) {
		// TODO
		Channel chan;
		if(channelRegister.contains(nodeId)){
			chan = channelRegister.get(nodeId);
			chan.write(msg);
		}else{
			chan = this.connectTo(nodeId);
			chan.write(msg);
		}
	}

	private Channel connectTo(IHash nodeId) {
		Channel chan = this.dht.connectTo(nodeId, channelRegister); 
		channelRegister.put(nodeId, chan);
		return chan;
	}

	private Collection<IMessageReceiver> getReceiver(Class<? extends IMessage> filterClass){
		Collection<IMessageReceiver> currentList;
		currentList = filtredReceivers.get(filterClass);
		return currentList;
	}
	
	@Override
	public void addReceiver(IMessageReceiver receiver, Class<? extends IMessage>... filterClasses) {
		for(Class<? extends IMessage> clazz: filterClasses){
			this.getReceiver(clazz).add(receiver);
		}
	}

	@Override
	public void addReceiver(Set<IMessageReceiver> receiver, Class<? extends IMessage>... filterClasses) {
		for(Class<? extends IMessage> clazz: filterClasses){
			this.getReceiver(clazz).addAll(receiver);
		}
	}
	
	@Override
	public void removeReceiver(IMessageReceiver receiver, Class<? extends IMessage>... filterClasses) {
		for(Class<? extends IMessage> clazz: filterClasses){
			this.filtredReceivers.remove(clazz, receiver);
		}		
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
		IMessageEnvelope envelope = (IMessageEnvelope) e.getMessage(); 
		this.channelRegister.put(envelope.getSender().getNodeId(), e.getChannel());
		try {
			this.notify(envelope.getMessage(), envelope.getSender());
		} catch (Exception e1) {
			e1.printStackTrace();
			e.getChannel().close();
		}
	}

	private void notify(IMessage message, INodeAddress sender) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Collection<IMessageReceiver> receiverList = this.getReceiver(message.getClass());
		for(IMessageReceiver receiver: receiverList){
			receiver.receive(message, sender);
		}
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
    		throws Exception {
    	e.getCause().printStackTrace();
    	e.getChannel().close();
    }

	@Override
	public INodeAddress getOwnAddress() {
		return this.nodeAddress;
	}
	
}
