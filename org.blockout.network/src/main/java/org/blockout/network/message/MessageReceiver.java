package org.blockout.network.message;

import java.lang.reflect.InvocationTargetException;

import org.blockout.network.dht.IHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageReceiver implements IMessageReceiver {

	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( MessageReceiver.class );
	}

	@Override
	public void receive( final Object msg, final IHash origin ) {
		Class<?> msgClass = msg.getClass();
		try {
			this.getClass().getMethod( "receive", msgClass, IHash.class ).invoke( this, msg, origin );
		} catch ( IllegalArgumentException e ) {
			logger.warn( "Can't invoke receiver method.", e );
		} catch ( SecurityException e ) {
			throw new RuntimeException( "Can't invoke receiver method due to security restriction.", e );
		} catch ( IllegalAccessException e ) {
			logger.debug( "Receiver method not visible.", e );
		} catch ( InvocationTargetException e ) {
			logger.warn( "Failed to invoke receiver method.", e );
		} catch ( NoSuchMethodException e ) {
			logger.debug( "No receiver method receive(" + msgClass.getName() + ", IHash) exists.", e );
		}
	}
}
