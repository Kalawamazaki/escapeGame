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
 * This class creates a launcher from which the game can be run, 
 * the instructions can be viewed, and the game can be closed.
 */
@SuppressWarnings("serial")
public class EscapeLauncher extends JFrame {
	// declare the components in this frame
	private JLabel welcome = new JLabel();
	private JButton launch = new JButton("Launch Game");
	private JButton instructions = new JButton("Read Instructions");
	private JButton exit = new JButton("Exit Game");

	public EscapeLauncher(String name) {
		// set a label with the game's name and center it
		welcome.setText(name);
		welcome.setHorizontalAlignment(JLabel.CENTER);
		// add menu listeners for the three button options
		launch.addActionListener(new MenuListener());
		instructions.addActionListener(new MenuListener());
		exit.addActionListener(new MenuListener());
		
		// create a panel with a grid layout to store all of the components
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		panel.add(welcome);
		panel.add(launch);
		panel.add(instructions);
		panel.add(exit);
		
		// add the panel to the frame; set the title, icon, close button, size; then make it visible
		this.add(panel);
		this.setTitle(name + " Launcher");
		this.setIconImage(new ImageIcon("assets/redDoor.png").getImage());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			
			// the launch game button was clicked
			if (e.getActionCommand() == "Launch Game") {
				// get rid of the launcher, create an instance of the game, and bring it to the front
				f.dispose();
				EscapeGame game = new EscapeGame();
				game.toFront();
			
			// the read instructions button was clicked
			} else if (e.getActionCommand() == "Read Instructions") {
				// set the title and message then show a dialog box
				String title = "Instructions";
				//(the large amount of spaces in the message are to align the columns because \t was not working)
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
			
			// the exit game button was clicked	
			} else if (e.getActionCommand() == "Exit Game") {
				System.exit(0);
			}
		}
	}
}