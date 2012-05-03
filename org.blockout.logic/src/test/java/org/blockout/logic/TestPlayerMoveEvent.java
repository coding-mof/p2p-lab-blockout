package org.blockout.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.blockout.world.PlayerMoveEvent;
import org.junit.Test;

public class TestPlayerMoveEvent {

	@Test
	public void testConstructor() {
		PlayerMoveEvent event = new PlayerMoveEvent( 0, 1, 2, 3 );

		assertEquals( 0, event.getOldX() );
		assertEquals( 1, event.getOldY() );
		assertEquals( 2, event.getNewX() );
		assertEquals( 3, event.getNewY() );
		assertNotNull( event.getId() );
		assertNotNull( event.getLocalTime() );
	}

	@Test
	public void testInverse() {
		PlayerMoveEvent event = new PlayerMoveEvent( 0, 1, 2, 3 );
		PlayerMoveEvent inverse = event.getInverse();

		assertNotNull( inverse );
		assertTrue( event.getId() != inverse.getId() );
		assertEquals( 2, inverse.getOldX() );
		assertEquals( 3, inverse.getOldY() );
		assertEquals( 0, inverse.getNewX() );
		assertEquals( 1, inverse.getNewY() );
		assertEquals( event.getLocalTime(), inverse.getLocalTime() );
		assertTrue( event.isInverseOf( inverse ) );
	}
}
