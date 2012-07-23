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
		value = fill( hash );
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

	public Hash(final BigInteger i) {
		value = fill( i.toString( 16 ) );
	}

	private static String fill( String hash ) {
		int diff = 40 - hash.length();
		for ( int i = 0; i < diff; i++ ) {
			hash = "0" + hash;
		}
		return hash;
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
		BigInteger next = getInteger().add( BigInteger.ONE );
		if ( next.equals( new BigInteger( "2" ).pow( getM() ) ) ) {
			return new Hash( BigInteger.ZERO );
		}
		return new Hash( next );
	}

	@Override
	public int compareTo( final IHash other ) {
		return getInteger().compareTo( other.getInteger() );
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

	@Override
	public IHash getPrevious() {
		BigInteger next = getInteger().subtract( BigInteger.ONE );
		if ( next.equals( new BigInteger( "-1" ) ) ) {
			return new Hash( new BigInteger( "2" ).pow( getM() ).subtract( BigInteger.ONE ) );
		}
		return new Hash( next );
	}

	@Override
	public IHash getClosest( final IHash a, final IHash b ) {
		if ( a == null ) {
			return b;
		}
		if ( b == null ) {
			return a;
		}
		BigInteger diffA = getInteger().subtract( a.getInteger() ).abs();
		BigInteger diffB = getInteger().subtract( b.getInteger() ).abs();
		return new Hash( diffA.min( diffB ) );
	}

	@Override
	public BigInteger getInteger() {
		return new BigInteger( value, 16 );
	}
}
