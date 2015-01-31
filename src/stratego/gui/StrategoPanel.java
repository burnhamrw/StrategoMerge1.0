package stratego.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StrategoPanel extends JPanel {
	
	private String backgroundLocation = MainFrame.imageFolderRoot + "Backgrounds\\FrameBackground.png";
	private BufferedImage image;
	private JFrame mother;
	public StrategoPanel(JFrame gameModeSelect)
	{
		this.mother = gameModeSelect;
		try {
			image = ImageIO.read(new File(backgroundLocation));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D graphics = (Graphics2D) g;
		setBackground(new Color(1.0f, 1.0f, 1.0f, .5f));
		graphics.drawImage(image, -this.getX(), -this.getY(), mother.getWidth(), mother.getHeight(), null);
	}

}
