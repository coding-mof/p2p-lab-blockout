package org.blockout.network.dht;

import java.net.InetSocketAddress;

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
		char[] hash = value.toCharArray();
		int overflow = 1;
		for ( int i = hash.length - 1; i >= 0; i-- ) {
			if ( overflow > 0 ) {
				hash[i]++;
				overflow--;
			}
			switch ( hash[i] ) {
				case ':':
					hash[i] = 'a';
					break;
				case 'g':
					overflow++;
					hash[i]--;
					break;
			}
		}
		return new Hash( String.copyValueOf( hash ) );
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
