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

/**
 * A very simple ChannelPipelineFactory that allows to add additional Handlers,
 * which aren't instantiated for each new ChannelPipeline, and are used with
 * several channels as a consequence.
 * 
 * @author Paul Dubs
 * 
 */

public class NettyChannelPipelineFactory implements ChannelPipelineFactory {

	private final HashMap<String, ChannelHandler>	additionalHandlers;

	public NettyChannelPipelineFactory() {
		additionalHandlers = new LinkedHashMap<String, ChannelHandler>();
	}

	public void addLast( final String key, final ChannelHandler handler ) {
		additionalHandlers.put( key, handler );
	}

	public HashMap<String, ChannelHandler> getHandlers() {
		return additionalHandlers;
	}

	public void removeHandler( final String key ) {
		additionalHandlers.remove( key );
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipe = Channels.pipeline( new ObjectEncoder(),
				new ObjectDecoder( ClassResolvers.cacheDisabled( this.getClass().getClassLoader() ) ) );
		for ( Entry<String, ChannelHandler> entry : additionalHandlers.entrySet() ) {
			pipe.addLast( entry.getKey(), entry.getValue() );
		}
		return pipe;
	}

}
