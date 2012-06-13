package org.blockout.network.demo;

import org.blockout.network.INodeAddress;
import org.blockout.network.dht.Hash;
import org.blockout.network.message.MessageBroker;
import org.blockout.network.message.MessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessagePassingTestbed extends MessageReceiver {
	private static final Logger	logger;
	static {
		logger = LoggerFactory.getLogger( MessagePassingTestbed.class );
	}

	/**
	 * @param args
	 */
	public static void main( final String[] args ) {
		ClassPathXmlApplicationContext context;
		context = new ClassPathXmlApplicationContext( "testbed.xml" );

		MessageBroker mp = context.getBean( MessageBroker.class );
		mp.addReceiver( new MessagePassingTestbed( mp ), DemoMessage.class );
		//
		// System.out.println("Done");
		//
		try {
			Thread.sleep( 6500 );
		} catch ( InterruptedException e ) {
			// // TODO Auto-generated catch block
			e.printStackTrace();
		}

		mp.send( new DemoMessage( "Hallo :)" ), new Hash( "0000000000000000000000000000000000000000" ) );
		//
		// Set<INodeAddress> nodes = mp.listNodes();
		// System.out.println(nodes);
		// for (INodeAddress node : nodes) {
		// System.out.println(node);
		// mp.send(new DemoMessage("Hi, I'm " + mp.nodeAddress), node);
		// }

	}

	private final MessageBroker	sut;
	private int					msgCounter;

	public MessagePassingTestbed(final MessageBroker sut) {
		this.sut = sut;
		msgCounter = 0;
	}

	public void receive( final DemoMessage msg, final INodeAddress origin ) {
		logger.debug( "Received: " + msg );
		if ( msgCounter < 10 ) {
			sut.send( new DemoMessage( msgCounter + " " + msg ), origin );
			msgCounter++;
		}
	}

}
