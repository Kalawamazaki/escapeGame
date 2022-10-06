/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */

package escapeGame;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 *  This class contains my main method.  
 *  It sets the look of the program and opens the game launcher.
 */
public class EscapeApp {

	public static void main(String[] args) {
		// change the ui to system look and feel
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException |
						 IllegalAccessException | UnsupportedLookAndFeelException e) {
					System.out.println(e);
				}
		// create a new instance of the game launcher and bring it to the front
		JFrame launcher = new EscapeLauncher("Escape!");
		launcher.toFront();
	}
}