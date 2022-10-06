/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */

package escapeGame;

import java.util.ArrayList;

/*
 *  This class is a more enhanced block that will store an item and might require a key.  
 */
public class Chest extends Block {
	// two variables to store an item and whether the chest requires a key
	private String storage;
	private boolean requiresKey;
	
// constructor	
	public Chest(String path, boolean locked, String item) {
		// chests are never traversable
		super(path, false);
		storage = item;
		requiresKey = locked;
	}

// getters	
	// this method gets the item from storage and clears the storage
	public String getItem() {
		String temp = storage;
		storage = "";
		return temp;
	}
	
	public boolean isLocked() {
		return requiresKey;
	}
	
	// this method checks if the chest is empty
	public boolean isEmpty() {
		if (storage.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	// this method checks the player's inventory for a key to open the chest
	public boolean checkKey(ArrayList<String> inventory) {
		for (String item : inventory) {
			if (item == "Chest Key") {
				return true;
			}
		}
		return false;
	}
}