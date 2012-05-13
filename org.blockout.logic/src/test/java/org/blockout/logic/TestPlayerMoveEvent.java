package org.blockout.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.blockout.world.entity.Player;
import org.blockout.world.event.PlayerMoveEvent;
import org.junit.Test;

public class TestPlayerMoveEvent {

	@Test
	public void testConstructor() {
		PlayerMoveEvent event = new PlayerMoveEvent( new Player( "" ), 0, 1, 2, 3 );

		assertEquals( 0, event.getOldX() );
		assertEquals( 1, event.getOldY() );
		assertEquals( 2, event.getNewX() );
		assertEquals( 3, event.getNewY() );
		assertNotNull( event.getId() );
		assertNotNull( event.getLocalTime() );
	}
}
