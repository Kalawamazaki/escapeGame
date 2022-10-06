/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */

package escapeGame;

/*
 *  This class is for the basic blocks that make up a room.  
 */
public class Block {
	// two variables, the path to the block's asset and whether the player can walk on it
	private String assetPath;
	private boolean traversable;
	
// constructor
	public Block(String path, boolean canWalk) {
		assetPath = path;
		traversable = canWalk;
	}
	
// setters
	public void setAssetPath(String path) {
		assetPath = path;
	}
	
	public void setTraversable(boolean canWalk) {
		traversable = canWalk;
	}
	
// getters
	public String getAssetPath() {
		return assetPath;
	}
	
	public boolean isTraversable() {
		return traversable;
	}
}