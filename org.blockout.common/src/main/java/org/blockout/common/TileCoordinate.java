package org.blockout.common;

public class TileCoordinate {
	private final int	x;
	private final int	y;

	public TileCoordinate(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static float computeEuclidianDistance( final TileCoordinate a, final TileCoordinate b ) {
		return (float) Math.sqrt( computeSquaredEuclidianDistance( a, b ) );
	}

	public static float computeSquaredEuclidianDistance( final TileCoordinate a, final TileCoordinate b ) {
		return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
	}

	public static int computeManhattanDistance( final TileCoordinate a, final TileCoordinate b ) {
		return Math.abs( a.x - b.x ) + Math.abs( a.y - b.y );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals( final Object obj ) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		TileCoordinate other = (TileCoordinate) obj;
		if ( x != other.x ) {
			return false;
		}
		if ( y != other.y ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TileCoordinate[x=" + x + ", y=" + y + "]";
	}

}
