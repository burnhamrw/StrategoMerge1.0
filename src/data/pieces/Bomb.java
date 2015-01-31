package data.pieces;



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import stratego.gui.MainFrame;
import data.nodes.Location;


/**
 * This class represents a bomb, which kills everything except for a miner. It also cannot be moved.
 * @author Luke Dengler
 *
 */
public class Bomb extends ImovablePiece
{
	private String fileName = MainFrame.imageFolderRoot + "PieceLabel\\Bomb.png";
	private BufferedImage image;
	
	/**
	 * Constructs a bomb on the specified team (form color) and at the location passed
	 * @param newCol The color of the team it is contained on
	 * @param loc The location of the bomb
	 */
    public Bomb(Color newCol, Location loc)
    {
    	
        super(newCol, 0, 12, loc);
        
        if(!isOnMyTeam())
        {
        	super.fileName = MainFrame.imageFolderRoot + "PieceLabel\\EnemyTeamPiece.png";
        	image = setImageToSpecifiedColor();
        }
    }
    
    /**
     * Gets the name of the piece
     * @return Bomb, the name of the piece
     */
    public String getMyName()
    {
        return "Bomb";
    }
    
    /**
     * Paints this bomb on the graphics passed at the specified x and y with the width and height all in pixel format
     * @param g The graphics of the screen
     * @param x The x pixels the top left corner is
     * @param y The y pixels the top left corner is
     * @param width The width of the image
     * @param height The height of the image
     */
    public void paint(Graphics2D g, int x, int y, int width, int height)
    {
		if(image == null)
			try {
				image = ImageIO.read(new File(fileName));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
    	g.drawImage(image,(int) (x + xPartialLocation * width),(int) (y + yPartialLocation * height), width, height, null, null);
    }
}
