import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The main class for your arcade game.
 * 
 * You can design your game any way you like, but make the game start by running
 * main here.
 * 
 * Also don't forget to write javadocs for your classes and functions!
 * 
 * @author Buffalo
 *
 */

public class Main {
	private static final Dimension SIZE = new Dimension(1080, 850);

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// System.out.println("Write your cool arcade game here!");
		JFrame frame = new GameFrame();
		frame.setSize(SIZE);
		frame.setTitle("Digger Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);

	}

}
