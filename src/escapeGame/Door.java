/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */


package escapeGame;

import java.util.ArrayList;

/*
 *  This class is a more enhanced block that will link a room to another room.  
 */
public class Door extends Block {
	// two variables for whether the door needs a key and what room this door will lead to
	private boolean requiresKey;
	private Room linkedRoom;
	
// constructor	
	public Door(String path, boolean locked, Room nextRoom) {
		// if the door is not locked, it is traversable
		super(path, !locked);
		requiresKey = locked;
		linkedRoom = nextRoom;
	}

// getters
	public Room getLinkedRoom() {
		return linkedRoom;
	}
	
	public boolean isLocked() {
		return requiresKey;
	}

// setter
	// this method gets a player inventory and checks to see if a key is in that inventory
	public boolean checkKey(ArrayList<String> inventory) {
		for (String item : inventory) {
			// if key, unlock door and make it traversable
			if (item == "Door Key") {
				this.setTraversable(true);
				requiresKey = false;
				// return that the key worked
				return true;
			}
		}
		// the was not in the inventory
		return false;
	}
}