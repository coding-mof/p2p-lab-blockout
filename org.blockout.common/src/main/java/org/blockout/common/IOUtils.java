package org.blockout.common;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class IOUtils {
	public static void close( final Closeable c ) {
		if ( c == null ) {
			return;
		}
		try {
			c.close();
		} catch ( IOException e ) {
		}
	}

	public static File createTempDir( final String prefix, final String suffix ) {
		try {
			File tempFile = File.createTempFile( prefix, suffix );
			tempFile.delete();
			tempFile.mkdirs();
			return tempFile;
		} catch ( IOException e ) {
			throw new RuntimeException( "Couldn't create temp file/dir.", e );
		}
	}
}
