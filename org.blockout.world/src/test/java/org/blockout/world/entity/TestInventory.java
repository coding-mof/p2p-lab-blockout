package org.blockout.world.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.blockout.world.items.Item;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the {@link Inventory} class.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class TestInventory {

	protected Inventory	inventory;

	@Before
	public void setUp() {
		inventory = new Inventory();
	}

	@Test
	public void testNewIsEmpty() {
		for ( int i = 0; i < 5; i++ ) {
			for ( int j = 0; j < 6; j++ ) {
				assertNull( inventory.getItem( i, j ) );
			}
		}
	}

	@Test
	public void testSetItem() {
		Item item = Mockito.mock( Item.class );
		inventory.setItem( 0, 0, item );

		assertEquals( item, inventory.getItem( 0, 0 ) );
	}

	@Test(expected = NullPointerException.class)
	public void testSetNull() {
		inventory.setItem( 0, 0, null );
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetItemOnBlockedPos() {
		Item item1 = Mockito.mock( Item.class );
		Item item2 = Mockito.mock( Item.class );

		inventory.setItem( 1, 0, item1 );
		inventory.setItem( 1, 0, item2 );
	}

	@Test
	public void testRemoveItem() {
		Item item = Mockito.mock( Item.class );

		inventory.setItem( 0, 0, item );
		inventory.removeItem( 0, 0 );

		assertNull( inventory.getItem( 0, 0 ) );
	}

	@Test
	public void testRemoveNonExistentItem() {
		assertNull( inventory.removeItem( 0, 0 ) );
	}

	@Test
	public void testSwapItems() {
		Item item1 = Mockito.mock( Item.class );
		Item item2 = Mockito.mock( Item.class );

		inventory.setItem( 0, 0, item1 );
		inventory.setItem( 0, 1, item2 );

		inventory.swapItems( 0, 0, 0, 1 );

		assertEquals( item1, inventory.getItem( 0, 1 ) );
		assertEquals( item2, inventory.getItem( 0, 0 ) );
	}

	@Test
	public void testSwapNull() {
		inventory.swapItems( 0, 0, 0, 1 );
	}
}
