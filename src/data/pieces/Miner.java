package data.pieces;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import stratego.gui.MainFrame;
import stratego.gui.Output;
import data.nodes.Location;

/**
 * This represents a miner which always has an attack and defend value of 3 but it can destroy mines
 * @author Luke Dengler
 *
 */
public class Miner extends Soldier
{
    private final static int MINERVAL=3;
    
    /**
     * Constructs the miner at the specified location on the specified team
     * @param newCol The color of the team it is contained on
     * @param newLoc The location that the miner will start
     */
    public Miner(Color newCol, Location newLoc)
    {
        super(newCol, MINERVAL, MINERVAL, newLoc);
    }
    
    /**
     * Gets the name of the piece
     * @return Flag, the name of the piece
     */
    public String getMyName()
    {
        return "Miner (3)";
    }
    
    /**
     * It moves normally only if the location passed is a mine, the mine will be removed
     * @param loc The location this miner will attempt to move to
     */
    public void move(Location loc)
    {
        if(getMoveLocs().contains(loc))//this is a location which the piece can move to
        {
            if(loc.getPiece() instanceof Bomb)
            {
            	if(this.isOnMyTeam())
            		Output.println("Your miner destroyed a bomb.");
            	else
            		Output.println("Your opponent destroyed a bomb.");
            	
                loc.getPiece().returnToBarracks();
                loc.setPiece(null);
                super.move(loc);
            }
            else
            {
            	super.move(loc);
            }
        }
    }
    
    /**
     * Paints this miner on the graphics passed at the specified x and y with the width and height all in pixel format
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
				image = ImageIO.read(new File(MainFrame.imageFolderRoot + "PieceLabel\\M.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	super.paint(g, x, y, width, height);
    }
    
    

}
