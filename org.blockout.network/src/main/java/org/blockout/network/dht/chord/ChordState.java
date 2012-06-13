package org.blockout.network.dht.chord;

public enum ChordState {
	Disconnected, DiscoverySent, FirstContactMade, JoinSent, Welcomed, ContactedNeighbors, WaitingForSecondAck, Connected, Leaving
}
