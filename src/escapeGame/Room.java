/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */

package escapeGame;

/*
 *  This class creates a scene of different blocks each represented by a 16x16px asset.  
 */
public class Room {
	// create a 2d array (or grid) to store 16x16 blocks
	private Block[][] sceneGrid = new Block[16][16];
	
// constructor	
	public Room(String backgroundAsset, String borderAsset) {
		// fill all 256 spots with blocks of the 'backgroundAsset' and make them traversable
		for (int i = 0; i < sceneGrid.length; i++) {
			for (int j = 0; j < sceneGrid[i].length; j++) {
				sceneGrid[i][j] = new Block(backgroundAsset, true);
			}
		}
		
		// fill the rim 60 spots with blocks of the 'borderAsset' and make them untraversable
		for (int i = 0; i < sceneGrid.length; i++) {
			sceneGrid[i][0] = new Block(borderAsset, false);
			sceneGrid[i][sceneGrid.length-1] = new Block(borderAsset, false);
			sceneGrid[0][i] = new Block(borderAsset, false);
			sceneGrid[sceneGrid.length-1][i] = new Block(borderAsset, false);
		}	
	}
	
// setters
	// change the asset and traversability of a block
	public void setBlock(int row, int col, String assetPath, Boolean canWalk) {
		// find the block
		Block block = getBlock(row, col);
		// change data
		block.setAssetPath(assetPath);
		block.setTraversable(canWalk);
	}
	
	// replace a block with a new block
	public void setBlock(int row, int col, Block replacement) {
		sceneGrid[row][col] = replacement;
	}
	
// getter	
	// get the block at row and col
	public Block getBlock(int row, int col) {
		return sceneGrid[row][col];
	}
}