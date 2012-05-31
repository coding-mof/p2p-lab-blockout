package org.blockout.network.discovery;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;

import org.blockout.network.INodeAddress;
import org.blockout.network.NodeInfo;
import org.blockout.network.dht.IHash;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;


public class NodeDiscovery extends SimpleChannelHandler implements INodeDiscovery {
	public final int port;
	private final DatagramChannelFactory factory;
	private final CopyOnWriteArraySet<INodeAddress> nodes;
	private final CopyOnWriteArraySet<DiscoveryListener> listeners;

	private boolean serverStarted;

	public NodeDiscovery(DatagramChannelFactory factory, int port) {
		this.factory = factory;
		this.port = port;
		this.nodes = new CopyOnWriteArraySet<INodeAddress>();
		this.listeners = new CopyOnWriteArraySet<DiscoveryListener>();
		this.serverStarted = false;
	}

	public void sendDiscoveryMessage(DiscoveryMsg discoveryMessage){
		ConnectionlessBootstrap b = new ConnectionlessBootstrap(this.factory);

		// Configure the pipeline factory.
		b.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers
								.cacheDisabled(this.getClass().getClassLoader()))
						);
			}
		});

		// Enable broadcast
		b.setOption("broadcast", "true");

		DatagramChannel c = (DatagramChannel) b.bind(new InetSocketAddress(0));

		c.write(discoveryMessage, new InetSocketAddress("255.255.255.255", this.port)).awaitUninterruptibly();
		c.write(discoveryMessage, new InetSocketAddress("127.0.0.1", this.port)).awaitUninterruptibly();
	}

	public void startDiscoveryServer(){
		if (!this.serverStarted) {
			final NodeDiscovery that = this;
			ConnectionlessBootstrap b = new ConnectionlessBootstrap(
					this.factory);
			b.setOption("broadcast", "false");
			b.setOption("reuseAddress", "true");

			// Configure the pipeline factory.
			b.setPipelineFactory(new ChannelPipelineFactory() {
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels
							.pipeline(
									new ObjectEncoder(),
									new ObjectDecoder(ClassResolvers
											.cacheDisabled(this.getClass()
													.getClassLoader())), that);
				}
			});
			b.bind(new InetSocketAddress(this.port));
			this.serverStarted = true;
		}
	}


	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) {
		final NodeDiscovery that = this;
		Runnable handleMessage = new Runnable() {
			@Override
			public void run() {
				InetSocketAddress address = (InetSocketAddress) e
						.getRemoteAddress();
				DiscoveryMsg msg = (DiscoveryMsg) e.getMessage();
				that.addNode(address, msg.getPort(), msg.getNodeId());
			}
		};
		Executors.newCachedThreadPool().execute(handleMessage);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	private void addNode(InetSocketAddress address, int port, IHash nodeId) {
		NodeInfo newNode = new NodeInfo(address.getHostName(), port, nodeId);
		if (!this.nodes.contains(newNode)) {
			this.nodes.add(newNode);

			for (DiscoveryListener l : this.listeners) {
				l.nodeDiscovered(newNode);
			}
		}
	}

	@Override
	public List<INodeAddress> listNodes() {
		return new ArrayList<INodeAddress>(this.nodes);
	}

	@Override
	public void addDiscoveryListener(DiscoveryListener l) {
		this.listeners.add(l);
	}

	@Override
	public void removeDiscoveryListener(DiscoveryListener l) {
		this.listeners.remove(l);
	}

}
