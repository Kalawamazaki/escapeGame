/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */

package escapeGame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * The main logic for the game happens in this panel.  
 * It will create the scene shown to the user, handle inputs,
 * and store most of the running data.
 * The panel will be a 16x16 grid of images defined by Block objects in
 * Room objects. Each image is 16x16 pixels large but location will be based on
 * the grid level and not the pixel level. Rows and columns are used to describe
 * the grid for simplicity.
 */
@SuppressWarnings("serial")
public class RenderPanel extends JPanel{
	
	// define all of the scenes that will be rendered in the game
	private Room spawn;
	private Room chestRoom;
	private Room mainWarp;
	private Room failRoom;
	private Room hall;
	private Room exit;
	private Room hallWarp;
	private Room viewRoom;
	private Room escape;
	
	// define a pointer to the current room and a pointer to the parent frame
	private Room currentRoom;
	private JFrame parentFrame;
	// instantiate a new player object (protagonist)
	private Player protag = new Player("assets/player.png");
	// flags whether the exit door can be seen
	private boolean exitVisible = false;

// constructor
	public RenderPanel(JFrame gameWindow) {
		// set the parentFrame, build out all of the rooms, set the first room, and set the player's position
		parentFrame = gameWindow;
		buildMap();
		currentRoom = spawn;
		protag.setPosition(7, 7);
		
		// create a keyListener for the panel
		this.addKeyListener(new KeyAdapter() {
			/*
			 * For each move, we will change the character's position
			 * and then check if that position is legal or if it has special
			 * properties such as being a door.
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
					// move left one spot
					case KeyEvent.VK_A:
					case KeyEvent.VK_LEFT:
						protag.setColPosition(protag.getColPosition() - 1);
						checkLocation();
						break;
					// move right one spot
					case KeyEvent.VK_D:
					case KeyEvent.VK_RIGHT:
						protag.setColPosition(protag.getColPosition() + 1);
						checkLocation();
						break;
					// move up one spot
					case KeyEvent.VK_W:
					case KeyEvent.VK_UP:
						protag.setRowPosition(protag.getRowPosition() - 1);
						checkLocation();
						break;
					// move down one spot
					case KeyEvent.VK_S:
					case KeyEvent.VK_DOWN:
						protag.setRowPosition(protag.getRowPosition() + 1);
						checkLocation();
						break;
					// interact with the room or an item in the room
					case KeyEvent.VK_F:
					case KeyEvent.VK_SPACE:
						interact();
						break;
					// check the player's inventory	
					case KeyEvent.VK_I:
					case KeyEvent.VK_CONTROL:
						showInventory();
						break;
					// pause the game
					case KeyEvent.VK_ESCAPE:
						pause();
						break;
				}
				// after a move or action, re-render the panel
				repaint();
			}
		});
	}
	
	// set the default size of the panel to 256px by 256px so 256 16x16px assets will fit
	public Dimension getPreferredSize() {
		return new Dimension(256,256);
	}
	
	// this method will build all of the rooms and create door connections
	private void buildMap() {
		// declare asset paths
		String floorAsset = "assets/wood.png";
		String wallAsset = "assets/brick.png";
		String doorAsset = "assets/door.png";
		String chestAsset = "assets/chest.png";
		
		// initialize rooms with the default floor and boarder assets
		spawn = new Room(floorAsset, wallAsset);
		chestRoom = new Room(floorAsset, wallAsset);
		mainWarp = new Room(floorAsset, wallAsset);
		failRoom = new Room(floorAsset, wallAsset);
		hall = new Room(floorAsset, wallAsset);
		exit = new Room(floorAsset, wallAsset);
		hallWarp = new Room(floorAsset, wallAsset);
		viewRoom = new Room(floorAsset, wallAsset);
		escape = new Room(wallAsset, wallAsset);
		
		/*
		 * Initialize doors and chests in rooms 
		 * (which requires all rooms to be initialized).
		 * Each door in a room will be connected to another room 
		 * and may or may not be locked.
		 * Each chest will have an item in it 
		 * and may or may not be locked.
		 */
		spawn.setBlock(7, 0, new Door(doorAsset, false, chestRoom));
		spawn.setBlock(0, 7, new Door(doorAsset, true, failRoom));
		spawn.setBlock(7, 15, new Door(doorAsset, false, mainWarp));
		spawn.setBlock(15, 7, new Door(doorAsset, false, hall));
		
		chestRoom.setBlock(7, 15, new Door(doorAsset, false, spawn));
		chestRoom.setBlock(1, 7, new Chest(chestAsset, false, "Door Key"));
		chestRoom.setBlock(14, 7, new Chest(chestAsset, false, "Chest Key"));
		
		mainWarp.setBlock(7, 0, new Door(doorAsset, false, spawn));
		mainWarp.setBlock(7, 15, new Door(doorAsset, false, hallWarp));
		
		hall.setBlock(7, 0, new Door(doorAsset, false, exit));
		hall.setBlock(0, 7, new Door(doorAsset, false, spawn));
		hall.setBlock(15, 7, new Door(doorAsset, false, hallWarp));
		
		exit.setBlock(7, 15, new Door(doorAsset, false, hall));
		
		hallWarp.setBlock(7, 0, new Door(doorAsset, false, mainWarp));
		hallWarp.setBlock(0, 7, new Door(doorAsset, false, hall));
		hallWarp.setBlock(7, 15, new Door(doorAsset, false, viewRoom));
		
		viewRoom.setBlock(7, 0, new Door(doorAsset, false, hallWarp));
		viewRoom.setBlock(7, 14, new Chest(chestAsset, true, "Spectacles"));
	}
	
	/*
	 * This method looks at the player's location to see if it is valid 
	 * or if the player needs to move to another space/room.
	 * It also checks for the win and loss conditions.
	 */
	private void checkLocation() {
		// check to make sure the player is not off screen
		checkScreenBoundaries();
		// check to make sure the player is not standing on a border or chest
		checkInvalidBlock();
		// check to see if the player needs to enter walk through a door
		openDoor();
		
		// if the currentRoom is the escape room, the player has won
		if (currentRoom == escape) {
			// show win message
			String message = "As you enter the red door, you catch a whiff of fresh air.\n"
						   + "Congragulations! You have escaped.";
			String title = "Success";
			JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.PLAIN_MESSAGE);
			
			// dispose the game frame and return to the launcher
			parentFrame.dispose();
			EscapeLauncher l = new EscapeLauncher("Escape!");
			l.toFront();
		// if the currentRoom is the fail room, the player has lost	
		} else if (currentRoom == failRoom) {
			// repaint the screen then show fail message
			repaint();
			String message = "Your mind descends into madness as you get lost in the maze.\n"
						   + "GAME OVER";
			String title = "GAME OVER";
			JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.ERROR_MESSAGE);
			
			// dispose the game frame and return to the launcher
			parentFrame.dispose();
			EscapeLauncher l = new EscapeLauncher("Escape!");
			l.toFront();
		} else {
			return;
		}
	}
	
	// this method checks to make sure the player is not off the screen
	private void checkScreenBoundaries() {
		int[] position = protag.getPosition();
		// check north edge
		if (position[0] < 0) {
			protag.setRowPosition(position[0] + 1);
		// check south edge
		} else if (position[0] > 15) {
			protag.setRowPosition(position[0] - 1);
		// check east edge
		} else if (position[1] < 0) {
			protag.setColPosition(position[1] + 1);
		// check west edge
		} else if (position[1] > 15) {
			protag.setColPosition(position[1] - 1);
		}
	}
	
	// this method checks to make sure the player is not on any untraversable blocks on the rim
	private void checkInvalidBlock() {
		int[] position = protag.getPosition();
		// check if the block is traversable
		Block currentBlock = currentRoom.getBlock(position[0], position[1]);
		if (currentBlock.isTraversable() == false) {
			// the check happens from three blocks in from the edge because no chests or doors should be any further in
			if (position[0] < 2) {
				protag.setRowPosition(position[0] + 1);
			} else if (position[0] > 13) {
				protag.setRowPosition(position[0] - 1);
			} else if (position[1] < 2) {
				protag.setColPosition(position[1] + 1);
			} else if (position[1] > 13) {
				protag.setColPosition(position[1] - 1);
			}
		}
	}
	
	// this method checks if the player should travel through a door
	private void openDoor() {
		int[] position = protag.getPosition();
		try {
			// check if the player is on a door
			Door currentDoor = (Door) currentRoom.getBlock(position[0], position[1]);
			// make sure the door is not locked
			if (!currentDoor.isLocked()) {
				currentRoom = currentDoor.getLinkedRoom();
				protag.setPosition(7, 7);
			}
		} catch (ClassCastException e) {
			return;
		}
	}
	
	// this method handles a number of player interactions based on position
	private void interact() {
		// get if a door and a chest are in range of the player (one block north, south, east, or west of the player)
		Door currentDoor = checkDoorRange();
		Chest currentChest = checkChestRange();
		// if the player is interacting next to a door (doors take precedence)
		if (currentDoor != null) {
			// if the player has a door key (unlock it)
			if (currentDoor.checkKey(protag.getInventory())) {
				// display success message
				String message = "You unlocked the door!";
				String title = "Announcement";
				JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
			// no door key	
			} else {
				// display fail message
				String message = "You don't have a Door Key!";
				String title = "Announcement";
				JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
			}
			
		// if the player is interacting next to a chest	
		} else if (currentChest != null) {
			// check if the chest is locked and full
			if (currentChest.isLocked() && !currentChest.isEmpty()) {
				// if player has key
				if (currentChest.checkKey(protag.getInventory())) {
					// get item
					String item = currentChest.getItem();
					protag.addItem(item);
					// display success message	
					String message = "You recieved: '" + item + "'!";
					String title = "Announcement";
					JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
				// player does not have key
				} else {
					// display fail message
					String message = "This chest needs a Chest Key!";
					String title = "Announcement";
					JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
				}
			// if the chest is not locked and full
			} else if (!currentChest.isEmpty()) {
				// get item
				String item = currentChest.getItem();
				protag.addItem(item);
				// display success message
				String message = "You recieved: '" + item + "'!";
				String title = "Announcement";
				JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
			// if the chest is empty
			} else {
				// display fail message
				String message = "This chest is empty!";
				String title = "Announcement";
				JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
			}
		
		// if there are no chests or doors, but the player has spectacles and is in the exit room and the exit door is not visible
		} else if (protag.getInventory().contains("Spectacles") && currentRoom == exit && !exitVisible) {
			// create 'unhide' (create) a door in the exit room that links to escape
			String exitDoorAsset = "assets/redDoor.png";
			exit.setBlock(7, 0, new Door(exitDoorAsset, true, escape));
			exitVisible = true;
			// display success message
			String message = "A door appears!";
			String title = "Announcement";
			JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
		
		// if player has the spectacles but is not in the exit room	
		} else if (protag.getInventory().contains("Spectacles")) {
			// display fail message
			String message = "Nothing happened";
			String title = "Announcement";
			JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
			
		// if the play is not near an intractable item and does not have spectacles	
		} else {
			// display fail message
			String message = "There is nothing to interact with here";
			String title = "Announcement";
			JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	// this method checks the blocks around the player for a door to return
	private Door checkDoorRange() {
		Door currentDoor;
		int[] position = protag.getPosition();
		try {
			// check north first
			currentDoor = (Door) currentRoom.getBlock(position[0]-1, position[1]);
		} catch (ClassCastException e) {
			try {
				// then east
				currentDoor = (Door) currentRoom.getBlock(position[0], position[1]+1);
			} catch (ClassCastException e1) {
				try {
					// then south
					currentDoor = (Door) currentRoom.getBlock(position[0]+1, position[1]);
				} catch (ClassCastException e2) {
					try {
						// finally west
						currentDoor = (Door) currentRoom.getBlock(position[0], position[1]-1);
					} catch (ClassCastException e3) {
						// no door
						currentDoor = null;
					}
				}
			}
		}
		return currentDoor;
	}
	
	// this method checks the blocks around the player for a chest to return
	private Chest checkChestRange() {
		Chest currentChest;
		int[] position = protag.getPosition();
		try {
			// check north first
			currentChest = (Chest) currentRoom.getBlock(position[0]-1, position[1]);
		} catch (ClassCastException e) {
			try {
				// then east
				currentChest = (Chest) currentRoom.getBlock(position[0], position[1]+1);
			} catch (ClassCastException e1) {
				try {
					// then south
					currentChest = (Chest) currentRoom.getBlock(position[0]+1, position[1]);
				} catch (ClassCastException e2) {
					try {
						// finally west
						currentChest = (Chest) currentRoom.getBlock(position[0], position[1]-1);
					} catch (ClassCastException e3) {
						// no chest
						currentChest = null;
					}
				}
			}
		}
		return currentChest;
	}
	
	// this method 'pauses' the game (locks the screen) and creates a pause menu
	private void pause() {
		// disable parent frame
		parentFrame.setEnabled(false);
		
		// create a pause menu and pass the parent 'EscapeGame' frame (for re-enabling it)
		PauseMenu menu = new PauseMenu(parentFrame);
		// make the menu request focus and stay on top
		menu.setAlwaysOnTop(true);
		menu.setAutoRequestFocus(true);
	}
	
	// this method displays the player's inventory
	private void showInventory() {
		// set title and message header
		String title = "Inventory";
		String message = "You look in your bag and see: \n";
		// if there are no items in the inventory
		if (protag.getInventory().isEmpty()) {
			message += "[Nothing]" + "\n";
		// else get the items in the inventory and add them to the message	
		} else {
			for (String item : protag.getInventory()) {
				message += item + "\n";
			}
		}
		// display the message
		JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.PLAIN_MESSAGE);
	}
	
	// this method displays the rooms and player and is called using repaint() in the class
	@Override
	protected void paintComponent(Graphics g) {
		// first call the super method
		super.paintComponent(g);
		// for all of the blocks in the current room
		for (int row = 0; row < 16; row++) {
			for (int col = 0; col < 16; col++) {
				// get the block's image
				Image image = new ImageIcon(currentRoom.getBlock(row, col).getAssetPath()).getImage();
				// draw it on the panel, scaling the row and column numbers up to pixels for each block.
				g.drawImage(image, col*16, row*16, this);
			}
		}
		// get the player's asset
		Image sprite = new ImageIcon(protag.getAssetPath()).getImage();
		// draw the sprite on the panel over the room and scaled up.
		g.drawImage(sprite, protag.getColPosition()*16, protag.getRowPosition()*16, this);
	}
}