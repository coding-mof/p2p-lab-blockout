package org.blockout.logic;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

/**
 * This class is a data structure for storing the Fog Of War information. It
 * allows to store fog information for all 2D coordinates in the range from
 * {@link Integer#MIN_VALUE}+1 up to {@link Integer#MAX_VALUE}.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class FogOfWar {

	private static final int		SEGMENT_SIZE	= 48;

	protected Map<String, BitSet>	exploreState;

	public FogOfWar() {
		exploreState = new HashMap<String, BitSet>();
	}

	public boolean isExplored( final int x, final int y ) {
		int segmentX = (SEGMENT_SIZE + x) / SEGMENT_SIZE;
		int segmentY = (SEGMENT_SIZE + y) / SEGMENT_SIZE;
		BitSet segment = exploreState.get( "(" + segmentX + "," + segmentY + ")" );
		if ( segment == null ) {
			return false;
		}
		int ofsetX = Math.abs( x % SEGMENT_SIZE );
		int ofsetY = Math.abs( y % SEGMENT_SIZE );
		return segment.get( ofsetX * SEGMENT_SIZE + ofsetY );
	}

	public void setExplored( final int x, final int y, final boolean explored ) {
		int segmentX = (SEGMENT_SIZE + x) / SEGMENT_SIZE;
		int segmentY = (SEGMENT_SIZE + y) / SEGMENT_SIZE;
		String key = "(" + segmentX + "," + segmentY + ")";
		BitSet segment = exploreState.get( key );
		if ( segment == null ) {
			segment = new BitSet( SEGMENT_SIZE * SEGMENT_SIZE );
			exploreState.put( key, segment );
		}
		int ofsetX = Math.abs( x % SEGMENT_SIZE );
		int ofsetY = Math.abs( y % SEGMENT_SIZE );
		segment.set( ofsetX * SEGMENT_SIZE + ofsetY, explored );
	}
}
