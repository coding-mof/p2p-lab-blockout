package org.blockout.network;

import org.blockout.network.dht.chord.Chord;

public class MessagePassingTestbed {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Chord chord = new Chord();
		MessagePassing sut = new MessagePassing(chord);

	}

}
