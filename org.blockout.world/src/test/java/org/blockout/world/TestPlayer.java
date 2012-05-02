package org.blockout.world;

import static org.junit.Assert.assertEquals;

import org.blockout.world.Player;
import org.junit.Test;

public class TestPlayer {

	@Test
	public void testValidConstructor() {
		Player p = new Player( "TestPlayer" );

		assertEquals( p.getMaxHealth(), p.getCurrentHealth() );
		assertEquals( 0, p.getExperience() );
		assertEquals( 1, p.getLevel() );
		assertEquals( "TestPlayer", p.getName() );
	}

	@Test(expected = NullPointerException.class)
	public void testNPEConstructor() {
		new Player( null );
	}

	@Test
	public void testValidLevel() {
		Player p = new Player( "TestPlayer" );
		p.setLevel( 15 );

		assertEquals( 15, p.getLevel() );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroInvalidLevel() {
		Player p = new Player( "TestPlayer" );
		p.setLevel( 0 );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeInvalidLevel() {
		Player p = new Player( "TestPlayer" );
		p.setLevel( -1 );
	}

	@Test
	public void setValidExperience() {
		Player p = new Player( "TestPlayer" );
		p.setExperience( 100 );

		assertEquals( 100, p.getExperience() );
	}

	@Test
	public void setZeroValidExperience() {
		Player p = new Player( "TestPlayer" );
		p.setExperience( 0 );

		assertEquals( 0, p.getExperience() );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeInvalidExperience() {
		Player p = new Player( "TestPlayer" );
		p.setExperience( -1 );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeInvalidHealth() {
		Player p = new Player( "TestPlayer" );
		p.setCurrentHealth( -1 );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testZeroInvalidHealth() {
		Player p = new Player( "TestPlayer" );
		p.setCurrentHealth( 0 );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxInvalidHealth() {
		Player p = new Player( "TestPlayer" );
		p.setCurrentHealth( p.getMaxHealth() + 1 );
	}

	@Test
	public void setValidHealth() {
		Player p = new Player( "TestPlayer" );
		p.setCurrentHealth( 100 );

		assertEquals( 100, p.getCurrentHealth() );
	}

	@Test
	public void setValidName() {
		Player p = new Player( "TestPlayer" );
		p.setName( "Player" );

		assertEquals( "Player", p.getName() );
	}

	@Test(expected = NullPointerException.class)
	public void setInvalidNPEName() {
		Player p = new Player( "TestPlayer" );
		p.setName( null );
	}
}
