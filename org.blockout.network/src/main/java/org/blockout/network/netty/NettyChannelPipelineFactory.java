package org.blockout.network.netty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

public class NettyChannelPipelineFactory implements ChannelPipelineFactory {

	private final HashMap<String, ChannelHandler> additionalHandlers;

	public NettyChannelPipelineFactory() {
		this.additionalHandlers = new LinkedHashMap<String, ChannelHandler>();
	}

	public void addLast(String key, ChannelHandler handler) {
		this.additionalHandlers.put(key, handler);
	}

	public HashMap<String, ChannelHandler> getHandlers() {
		return this.additionalHandlers;
	}

	public void removeHandler(String key) {
		this.additionalHandlers.remove(key);
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipe = Channels.pipeline(
				new ObjectEncoder(),
				new ObjectDecoder(ClassResolvers.cacheDisabled(getClass()
						.getClassLoader())));
		for (Entry<String, ChannelHandler> entry : this.additionalHandlers
				.entrySet()) {
			pipe.addLast(entry.getKey(), entry.getValue());
		}
		return pipe;
	}

}
