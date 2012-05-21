package org.blockout.common;

/**
 * Represents a coordinate in a 2 dimensional cartesian coordinate system.
 * 
 * @author Marc-Christian Schulze
 * @threadSafety immutable
 * 
 */
public class TileCoordinate {
	private final int	x;
	private final int	y;

	/**
	 * Constructs a new coordinate with the given values.
	 * 
	 * @param x
	 *            The x value of the coordinate.
	 * @param y
	 *            The y value of the coordinate.
	 */
	public TileCoordinate(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x value of the coordinate.
	 * 
	 * @return The x value of the coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y value of the coordinate.
	 * 
	 * @return The y value of the coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Computes the euclidian distance between the two {@link TileCoordinate}s.
	 * 
	 * @param a
	 *            The first coordinate.
	 * @param b
	 *            The second coordinate.
	 * @return The euclidian distance between <code>a</code> and <code>b</code>.
	 */
	public static float computeEuclidianDistance( final TileCoordinate a, final TileCoordinate b ) {
		return (float) Math.sqrt( computeSquaredEuclidianDistance( a, b ) );
	}

	/**
	 * Compute the squared euclidian distance between the two
	 * {@link TileCoordinate}s. Prefer this value over the accurate value of
	 * {@link #computeEuclidianDistance(TileCoordinate, TileCoordinate)} and
	 * square the reference value instead. Usually used in code like <br />
	 * <code>if(computeSquaredEuclidianDistance(a, b) < SQUARED_DISTANCE) {</code>
	 * 
	 * 
	 * @param a
	 *            The first coordinate.
	 * @param b
	 *            The second coordinate.
	 * @return The squared euclidian distance between <code>a</code> and
	 *         <code>b</code>.
	 */
	public static float computeSquaredEuclidianDistance( final TileCoordinate a, final TileCoordinate b ) {
		return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
	}

	/**
	 * Computes the manhattan distance between two coordinates.
	 * 
	 * @param a
	 *            The first coordinate.
	 * @param b
	 *            The second coordinate.
	 * @return The manhattan distance between the two coordinates.
	 */
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
