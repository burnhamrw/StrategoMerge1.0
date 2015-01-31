package data.pieces;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import stratego.gui.MainFrame;
import data.nodes.Location;

/**
 * This class represents the flag or end goal piece to get in the game, it cannot move and can be "killed" by any piece on the opponents team
 * 
 * @author Luke Dengler
 *
 */
public class Flag extends ImovablePiece
{

	/**
	 * Constructs a flag on the specified team (form color) and at the location passed
	 * @param newCol The color of the team it is contained on
	 * @param loc The location of the flag
	 */
    public Flag(Color newColor, Location newLoc)
    {
        super(newColor, 0, 0, newLoc);
        
        fileName = MainFrame.imageFolderRoot + "PieceLabel\\Flag.png";
    }
    
    /**
     * Gets the name of the piece
     * @return Flag, the name of the piece
     */
    public String getMyName()
    {
        return "Flag";
    }
    
    /**
     * Paints this flag on the graphics passed at the specified x and y with the width and height all in pixel format
     * @param g The graphics of the screen
     * @param x The x pixels the top left corner is
     * @param y The y pixels the top left corner is
     * @param width The width of the image
     * @param height The height of the image
     */
    public void paint(Graphics2D g, int x, int y, int width, int height)
    {
    	if(image == null)
    		setImageToSpecifiedColor();
    	
    	super.paint(g, x, y, width, height);
    }
}
