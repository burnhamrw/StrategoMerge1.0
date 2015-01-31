package data.pieces;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;

import stratego.gui.MainFrame;
import stratego.gui.Output;
import data.nodes.Location;

/**
 * A Scout can take two moves during one move session.
 * @author Luke Dengler
 *
 */
public class Scout extends Soldier
{
    private final static int SCOUTVAL=2;
    
    /**
     * Constructs the scout at the specified location on the specified team
     * @param newCol The color of the team it is contained on
     * @param newLoc The location that the miner will start
     */
    public Scout(Color newCol, Location newLoc)
    {
        super(newCol, SCOUTVAL, SCOUTVAL, newLoc);
    }
    
    /**
     * the scout version of the get move locations that will check all 12 possible locations that can be moved to
     * @return The possible move locations
     */
    public HashSet<Location> getMoveLocs()
    {
    	HashSet<Location> locs = new HashSet<Location>();
        HashSet<Location> result = new HashSet<Location>();
        locs=super.getMoveLocs();
        Location temp;
        HashSet<Location> temporary;
        
        for(Location loc: locs)
        {
        	result.add(loc);
        	temporary = getMoveLocs(loc);
        	for(Location purgatoryLoc: temporary)
        	{
        		if(!result.contains(purgatoryLoc) && purgatoryLoc.getPiece() != null)
        			result.add(purgatoryLoc);
        	}

        }
        
        

        return result;
    } 
    
    /**
     * Gets the name of the piece
     * @return Scout, the name of the piece
     */
    public String getMyName()
    {
        return "Scout(2)";
    }
    
    /**
     * Paints this scout on the graphics passed at the specified x and y with the width and height all in pixel format
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
				image = ImageIO.read(new File(MainFrame.imageFolderRoot + "PieceLabel\\S.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	super.paint(g, x, y, width, height);
    }
}
