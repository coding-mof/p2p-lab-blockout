package org.blockout.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.blockout.common.IOUtils;
import org.blockout.world.Player;
import org.junit.Before;
import org.junit.Test;

public class TestPlayerManager {

	protected PlayerManager	playerMgr;

	@Before
	public void setUp() {
		File tempDir = IOUtils.createTempDir( "bo_", "_test" );
		playerMgr = new PlayerManager( tempDir );
	}

	@Test
	public void testSavePlayer() throws IOException {
		Player p = new Player( "Testplayer" );

		playerMgr.savePlayer( p );
	}

	@Test(expected = NullPointerException.class)
	public void testSaveNull() throws IOException {
		playerMgr.savePlayer( null );
	}

	@Test
	public void testLoadPlayer() throws IOException {
		Player p = new Player( "Testplayer" );
		p.setLevel( 3 );
		p.setExperience( 2 );
		p.setCurrentHealth( 1 );

		playerMgr.savePlayer( p );

		Player p2 = playerMgr.loadPlayer( "Testplayer" );

		assertEquals( p.getName(), p2.getName() );
		assertEquals( p.getLevel(), p2.getLevel() );
		assertEquals( p.getExperience(), p2.getExperience() );
		assertEquals( p.getCurrentHealth(), p2.getCurrentHealth() );
	}

	@Test(expected = NullPointerException.class)
	public void testLoadNull() throws IOException {
		playerMgr.loadPlayer( null );
	}

	@Test
	public void testDeletePlayer() throws IOException {
		Player p = new Player( "Testplayer" );
		playerMgr.savePlayer( p );
		playerMgr.deletePlayer( "Testplayer" );

		Player p2 = playerMgr.loadPlayer( "Testplayer" );

		assertNull( p2 );
	}

	@Test
	public void testDeleteAllPlayers() throws IOException {
		Player p = new Player( "Testplayer" );
		playerMgr.savePlayer( p );
		playerMgr.deleteAllPlayers();
		Set<String> playerNames = playerMgr.listPlayers();

		assertNotNull( playerNames );
		assertEquals( 0, playerNames.size() );
	}

	@Test
	public void testListNoPlayers() {
		playerMgr.deleteAllPlayers();
		Set<String> playerNames = playerMgr.listPlayers();

		assertNotNull( playerNames );
		assertEquals( 0, playerNames.size() );
	}

	@Test
	public void testListSomePlayers() throws IOException {
		Player p1 = new Player( "PlayerA" );
		Player p2 = new Player( "PlayerB" );
		Player p3 = new Player( "PlayerC" );

		playerMgr.deleteAllPlayers();
		playerMgr.savePlayer( p1 );
		playerMgr.savePlayer( p2 );
		playerMgr.savePlayer( p3 );

		Set<String> playerNames = playerMgr.listPlayers();

		assertNotNull( playerNames );
		assertEquals( 3, playerNames.size() );
	}
}
