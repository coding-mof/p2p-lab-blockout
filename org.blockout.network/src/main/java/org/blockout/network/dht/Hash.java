package org.blockout.network.dht;

import java.net.InetSocketAddress;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Hash implements IHash {
	private static final long serialVersionUID = -3234415314303475314L;
	private String value;

	public Hash(InetSocketAddress address){
		HashFunction hf = Hashing.sha1();
		HashCode hash = hf.hashString(address.toString());
		value = hash.toString();
	}
	
	public Hash(String hash){
		value = hash;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return value;
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

}
