package org.blockout.network.dht;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.UUID;

import org.blockout.common.TileCoordinate;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Hash implements IHash {
	private static final long	serialVersionUID	= -3234415314303475314L;
	private final String		value;

	public Hash(final InetSocketAddress address) {
		HashFunction hf = Hashing.sha1();
		HashCode hash = hf.hashString( address.toString() );
		value = hash.toString();
	}

	public Hash(final String hash) {
		value = hash;
	}

	public Hash(final UUID id) {
		HashFunction hf = Hashing.sha1();
		HashCode hash = hf.hashString( id.toString() );
		value = hash.toString();
	}

	public Hash(final TileCoordinate coord) {
		HashFunction hf = Hashing.sha1();
		HashCode hash = hf.hashString( String.valueOf( coord.hashCode() ) );
		value = hash.toString();
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public IHash getNext() {
		BigInteger i = new BigInteger( value, 16 );
		BigInteger next = i.add( BigInteger.ONE );
		return new Hash( next.toString( 16 ) );
	}

	@Override
	public int compareTo( final IHash other ) {
		return value.compareTo( other.getValue() );
	}

	@Override
	public boolean equals( final Object other ) {
		if ( !(other instanceof Hash) ) {
			return false;
		}
		return ((Hash) other).getValue().equals( getValue() );
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public int getM() {
		return 160;
	}
}
