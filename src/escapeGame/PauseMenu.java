/*
 * Author: Peter Shippy
 * Date: May. 3, 2021
 */

package escapeGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/*
 *  This class creates a JFrame pause menu for during the game.  
 */
@SuppressWarnings("serial")
public class PauseMenu extends JFrame {
	// create a label and buttons (similar to the launcher)
	private JLabel welcome = new JLabel("Game Paused");
	private JButton resume = new JButton("Resume");
	private JButton instructions = new JButton("Read Instructions");
	private JButton quit = new JButton("Quit");
	private JFrame game;
		
	public PauseMenu(JFrame parentComponent) {
		// set game equal to the EscapeGame frame
		game = parentComponent;
		// set label alignment
		welcome.setHorizontalAlignment(JLabel.CENTER);
		// add menu listeners
		resume.addActionListener(new MenuListener());
		instructions.addActionListener(new MenuListener());
		quit.addActionListener(new MenuListener());
		// create a panel and add the components
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		panel.add(welcome);
		panel.add(resume);
		panel.add(instructions);
		panel.add(quit);
		
		// add the panel to the frame, set the title, icon, close operation, size, and make it visible
		this.add(panel);
		this.setTitle("Paused");
		this.setIconImage(new ImageIcon("EscapeFinalAssets/redDoor.png").getImage());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(300, 200);
		this.setVisible(true);
	}
	
	// inner class with action listener
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// get the frame the action came from
			JButton b = (JButton) e.getSource();
			JRootPane p = (JRootPane) b.getRootPane();
			JFrame f = (JFrame) p.getParent();
			
			// if the resume button was clicked
			if (e.getActionCommand() == "Resume") {
				// dispose this window
				f.dispose();
				
				// re-enable the game frame and bring it to the front
				game.setEnabled(true);
				game.toFront();
			
			// if the read instructions button was clicked
			} else if (e.getActionCommand() == "Read Instructions") {
				// display the instructions just like in the launcher
				String title = "Instructions";
				String message = "In this game your goal is to find the red exit door to escape the building. \n"
							   + "You do this by finding items in chests and exploring.\n"
							   + "CONTROLS:\n"
							   + "Walk up                  -> [W]/[Up Arrow]\n"
							   + "Walk down             -> [S]/[Down Arrow]\n"
							   + "Walk left                 -> [A]/[Left Arrow]\n"
							   + "Walk right              -> [D]/[Right Arrow]\n"
							   + "Interact/Use Item  -> [F]/[Space]\n"
							   + "View Inventory       -> [I]/[Ctrl]\n"
							   + "Pause Game           -> [Esc]";
				JOptionPane.showMessageDialog(f, message, title, JOptionPane.PLAIN_MESSAGE);
			
			// if the quit button was clicked
			} else if (e.getActionCommand() == "Quit") {
				// dispose the game and the menu
				game.dispose();
				f.dispose();
				
				// Instantiate the launcher and bring it to the front
				EscapeLauncher l = new EscapeLauncher("Escape!");
				l.toFront();
			}
		}
	}
}