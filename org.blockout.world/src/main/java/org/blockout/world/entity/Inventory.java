package org.blockout.world.entity;

import org.blockout.world.items.Item;

import com.google.common.base.Preconditions;

/**
 * The inventory of a player where he can store items in.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Inventory {
	protected Item[][]	items;

	/**
	 * Creates a new empty inventory.
	 */
	public Inventory() {
		items = new Item[5][];
		for ( int i = 0; i < 5; i++ ) {
			items[i] = new Item[6];
		}
	}

	/**
	 * Sets an item at the given location in the inventory.
	 * 
	 * @param x
	 *            The x position in the range of [0,5)
	 * @param y
	 *            The y position in the range of [0,6)
	 * @param item
	 *            The item to set.
	 * @throws NullPointerException
	 *             If item is null.
	 * @throws IllegalArgumentException
	 *             If the position is out of range.
	 */
	public void setItem( final int x, final int y, final Item item ) {
		Preconditions.checkNotNull( item );
		Preconditions.checkArgument( x >= 0 && x < 5, "x must be in the range [0,5)" );
		Preconditions.checkArgument( y >= 0 && y < 6, "y must be in the range [0,6)" );
		if ( items[x][y] != null ) {
			throw new IllegalArgumentException( "Can't set item at position " + x + "," + y + ". Cell blocked by item "
					+ items[x][y] );
		}

		items[x][y] = item;
	}

	/**
	 * Returns the item at a given position.
	 * 
	 * @param x
	 *            The x position in the range of [0,5)
	 * @param y
	 *            The y position in the range of [0,6)
	 * @return The item at the given position or null if there is no item.
	 * @throws IllegalArgumentException
	 *             If the position is out of range.
	 */
	public Item getItem( final int x, final int y ) {
		Preconditions.checkArgument( x >= 0 && x < 5, "x must be in the range [0,5)" );
		Preconditions.checkArgument( y >= 0 && y < 6, "y must be in the range [0,6)" );

		return items[x][y];
	}

	/**
	 * Removes and returns the item at a given position - if present.
	 * 
	 * @param x
	 *            The x position in the range of [0,5)
	 * @param y
	 *            The y position in the range of [0,6)
	 * @return Returns the removed item or null if there was no item.
	 * @throws IllegalArgumentException
	 *             If the position is out of range.
	 */
	public Item removeItem( final int x, final int y ) {
		Preconditions.checkArgument( x >= 0 && x < 5, "x must be in the range [0,5)" );
		Preconditions.checkArgument( y >= 0 && y < 6, "y must be in the range [0,6)" );

		Item item = items[x][y];
		items[x][y] = null;
		return item;
	}

	/**
	 * Swaps two items.
	 * 
	 * @param x1
	 *            The x position of the first item in the range of [0,5)
	 * @param y1
	 *            The y position of the first item in the range of [0,6)
	 * @param x2
	 *            The x position of the second item in the range of [0,5)
	 * @param y2
	 *            The y position of the second item in the range of [0,6)
	 * @throws IllegalArgumentException
	 *             If the position is out of range.
	 */
	public void swapItems( final int x1, final int y1, final int x2, final int y2 ) {
		Preconditions.checkArgument( x1 >= 0 && x1 < 5, "x1 must be in the range [0,5)" );
		Preconditions.checkArgument( y1 >= 0 && y1 < 6, "y1 must be in the range [0,6)" );
		Preconditions.checkArgument( x2 >= 0 && x2 < 5, "x2 must be in the range [0,5)" );
		Preconditions.checkArgument( y2 >= 0 && y2 < 6, "y2 must be in the range [0,6)" );

		Item tmp = items[x1][y1];
		items[x1][y1] = items[x2][y2];
		items[x2][y2] = tmp;
	}
}
