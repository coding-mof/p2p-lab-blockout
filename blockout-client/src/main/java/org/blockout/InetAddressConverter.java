package org.blockout;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 * Converter for JCommander to convert and resolve IP-Addresses and hostnames
 * from the command line.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class InetAddressConverter implements IStringConverter<InetAddress> {

	@Override
	public InetAddress convert( final String paramString ) {
		try {
			return InetAddress.getByName( paramString );
		} catch ( UnknownHostException e ) {
			throw new ParameterException( "Given hostname can't get resolved." );
		}
	}

}
