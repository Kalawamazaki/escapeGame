/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */

package escapeGame;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/*
 *  This class creates the JFrame for the game.  
 *  It creates a render panel that contains most of the game's logic.
 */
@SuppressWarnings("serial")
public class EscapeGame extends JFrame {
	
	public EscapeGame() {
		// instantiate a render panel and make it keyboard focusable
		RenderPanel panel = new RenderPanel(this);
		panel.setFocusable(true);
		
		// add the panel, pack it so the size just fits the panel
		// set the title, icon, close operation, make it non-resizable and show it
		this.add(panel);
		this.pack();
		this.setTitle("Escape!");
		this.setIconImage(new ImageIcon("EscapeFinalAssets/redDoor.png").getImage());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
}