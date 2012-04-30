package org.blockout.world;

public class Tile {
	protected int	tileType;

	public Tile(final int type) {
		tileType = type;
	}

	public int getTileType() {
		return tileType;
	}

	public void setTileType( final int tileType ) {
		this.tileType = tileType;
	}

}
