package org.blockout.world;

/**
 * 
 * @author Konstantin Ramig
 */
public class Chunk {
	
	private int X;
	private int Y;
	private Tile[][] tiles;
	
	/**
	 * 
	 * @param X x coordinate of the Chunk in the World
	 * @param Y y coordinate of the Chunk in the World
	 * @param tiles tiles contained by the chunk should be an NxN matrix or null
	 * @throws IllegalArgumentException
	 * 				if tiles is not a NxN matrix or null
	 */
	public Chunk(int X, int Y, Tile[][] tiles) throws IllegalArgumentException {
		this.X=X;
		this.Y=Y;
		if(tiles == null) this.tiles = tiles;
		else{
			if(tiles.length == tiles[0].length) this.tiles=tiles;
			else throw new IllegalArgumentException("Tile matrix is not symetric");
		}
	}
	
	/**
	 * @param x coordinate of the Chunk in the World
	 */
	public void setX( int x){
		X=x;
	}
	
	/**
	 * @param x coordinate of the Chunk in the World
	 */
	public void setY( int y){
		Y=y;
	}
	
	/**
	 * @param x coordinate of the Chunk in the World
	 */
	public int getX(){
		return X;
	}
	
	/**
	 * @param x coordinate of the Chunk in the World
	 */
	public int getY(){
		return Y;
	}
	
	/**
	 * @param tiles tiles contained by the chunk should be an NxN matrix or null
	 */
	public void setTiles(Tile[][] tiles)throws IllegalArgumentException {
		this.tiles = tiles;
	}
	
	/**
	 * 
	 * @param x location of the Tile within the chunk
	 * @param y location of the Tile within the chunk
	 * @return the Tile t the given location
	 * @throws NullPointerException
	 * 				if tiles are still null
	 */
	public Tile getTile(int x, int y){
		return tiles[x][y];
	}

}