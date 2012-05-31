package org.blockout.network.dht;

import java.net.InetSocketAddress;

import org.blockout.common.TileCoordinate;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Hash implements IHash {
	private static final long serialVersionUID = -3234415314303475314L;
	private final String value;

	public Hash(InetSocketAddress address){
		HashFunction hf = Hashing.sha1();
		HashCode hash = hf.hashString(address.toString());
		this.value = hash.toString();
	}

	public Hash(String hash){
		this.value = hash;
	}

	public Hash(TileCoordinate coord){
		HashFunction hf = Hashing.sha1();
		HashCode hash = hf.hashString(String.valueOf(coord.hashCode()));
		this.value = hash.toString();
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String toString(){
		return this.value;
	}

	@Override
	public IHash getNext() {
		char[] hash = this.value.toCharArray();
		int overflow = 1;
		for(int i = hash.length - 1; i >= 0; i--){
			if(overflow > 0){
				hash[i]++;
				overflow--;
			}
			switch(hash[i]){
			case ':':
				hash[i] = 'a';
				break;
			case 'g':
				overflow++;
				hash[i]--;
				break;
			}
		}
		return new Hash(String.copyValueOf(hash));
	}

	@Override
	public int compareTo(IHash other) {
		return this.value.compareTo(other.getValue());
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Hash)) {
			return false;
		}
		return ((Hash) other).getValue().equals(this.getValue());
	}

	@Override
	public int hashCode() {
		return this.getValue().hashCode();
	}

}
