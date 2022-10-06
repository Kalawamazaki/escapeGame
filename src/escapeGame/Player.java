/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */

package escapeGame;

import java.util.ArrayList;

/*
 * This class will store data for the player during the game.  
 */
public class Player {
	// store row and column position in a room grid
	private int rowPosition;
	private int colPosition;
	// store file path for the character asset
	private String assetPath;
	// store the character's inventory as an arrayList
	private ArrayList<String> inventory;
	
// constructor
	public Player(String path) {
		assetPath = path;
		rowPosition = 1;
		colPosition = 1;
		inventory = new ArrayList<String>(3);
	}
	
// setters
	// this method sets both the row and column in one call
	public void setPosition(int row, int col) {
		rowPosition = row;
		colPosition = col;
	}
	
	public void setRowPosition(int row) {
		rowPosition = row;
	}
	
	public void setColPosition(int col) {
		colPosition = col;
	}
	
	// this method adds an item to the inventory arraylist
	public void addItem(String item) {
		inventory.add(item);
	}
	
// getters
	// get the position as a two element array
	public int[] getPosition() {
		return new int[]{rowPosition, colPosition};
	}
	
	public int getRowPosition() {
		return rowPosition;
	}
	
	public int getColPosition() {
		return colPosition;
	}
	
	// get the file path of the player's asset
	public String getAssetPath() {
		return assetPath;
	}
	
	// get the inventory as an ArrayList
	public ArrayList<String> getInventory() {
		return inventory;
	}
}