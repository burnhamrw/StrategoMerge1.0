package stratego.gui;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import actions.CloseAction;
import actions.PlayAction;

/**
 * This panel displays the title of the game and wait for user input to continue
 * @author rburnham99
 *
 */
public class TitleScreen extends JPanel {
	
	private String backgroundName = MainFrame.imageFolderRoot + "Backgrounds\\StrategoTitleBackground.png";
	private String titleName = MainFrame.imageFolderRoot + "Backgrounds\\StrategoTitle.png";

	/**
	 * Temporary title of the game
	 */
	private String title = "Stratego - press space";
	

	/**
	 * Sets up the title screen and sets up the listeners
	 */
	public TitleScreen()
	{
		getInputMap(GameGUI.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE), "EXIT");
		getActionMap().put("EXIT" ,new CloseAction());
		
		getInputMap(TitleScreen.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_SPACE), "PLAY");
		getActionMap().put("PLAY", new PlayAction());
		this.repaint();
	}
	
	/**
	 * Displays the title
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D graphics = (Graphics2D) g;

		try {
			graphics.drawImage(ImageIO.read(new File(backgroundName)), 0, 0, MainFrame.getRunner().getWidth(), MainFrame.getRunner().getHeight(), null, null);
			graphics.drawImage(ImageIO.read(new File(titleName)), 0, 0, getWidth(), getHeight(), null, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
