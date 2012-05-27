package org.blockout.network.demo;

import java.util.List;

import org.blockout.network.INodeAddress;
import org.blockout.network.NodeInfo;
import org.blockout.network.message.MessageBroker;
import org.blockout.network.message.MessageReceiver;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessagePassingTestbed extends MessageReceiver{
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context;
		context = new ClassPathXmlApplicationContext("testbed.xml");

		MessageBroker mp = context.getBean(MessageBroker.class);
		mp.addReceiver(new MessagePassingTestbed(mp), DemoMessage.class);
		
		System.out.println("Done");

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<NodeInfo> nodes = mp.listNodes();
		System.out.println(nodes);
		for(NodeInfo node: nodes){
			System.out.println(node);
			mp.send(new DemoMessage("Hi, I'm " + mp.nodeAddress), node);
		}
		
	}

	private final MessageBroker sut;
	private int msgCounter;
	
	public MessagePassingTestbed(MessageBroker sut){
		this.sut = sut;
		this.msgCounter = 0;
	}
	
	public void receive(DemoMessage msg, INodeAddress origin) {
		System.out.println("Received: " + msg);
		if (msgCounter < 10) {
			this.sut.send(new DemoMessage("Got Your Message: " + msg), origin);
			msgCounter++;
		}
	}

}
