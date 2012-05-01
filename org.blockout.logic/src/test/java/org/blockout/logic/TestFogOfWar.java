package org.blockout.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestFogOfWar {

	@Test
	public void testConstructor() {
		FogOfWar fog = new FogOfWar();

		assertFalse( fog.isExplored( 0, 0 ) );
		assertFalse( fog.isExplored( -1, -1 ) );
		assertFalse( fog.isExplored( -1, 1 ) );
		assertFalse( fog.isExplored( 1, 1 ) );
		assertFalse( fog.isExplored( 1, -1 ) );
	}

	@Test
	public void testSetExplored() {
		FogOfWar fog = new FogOfWar();

		fog.setExplored( 0, 0, true );
		assertTrue( fog.isExplored( 0, 0 ) );

		fog.setExplored( 0, 0, false );
		assertFalse( fog.isExplored( 0, 0 ) );

		fog.setExplored( 1, 1, true );
		assertTrue( fog.isExplored( 1, 1 ) );

		fog.setExplored( 1, 1, false );
		assertFalse( fog.isExplored( 1, 1 ) );
	}

}
