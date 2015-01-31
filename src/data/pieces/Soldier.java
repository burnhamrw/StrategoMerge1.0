package data.pieces;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;

import stratego.gui.MainFrame;
import data.nodes.Location;


/**
 * This is a "normal soldier" that has no special powers
 * @author Luke Dengler
 *
 */
public class Soldier extends Piece
{

	private String root = MainFrame.imageFolderRoot + "PieceLabel\\";
	protected BufferedImage image;
	
    /**
     * Constructs the scout at the specified location on the specified team with the correct value(attack and defend value)
     * @param team The color of the team it is contained on
     * @param value The value that the attack and defend value will have
     * @param loc The location that the miner will start
     */
    public Soldier( Color team, int value, Location loc)
    {
        super(team, value, value, loc);
    }
    
    /**
     * Constructs the scout at the specified location on the specified team with different attack and defend values 
     * @param team The color of the team it is contained on
     * @param attack The value it will attack with
     * @param defend The value it will defend with
     * @param loc The location that the miner will start
     */
    public Soldier(Color team, int attack, int defend,  Location loc)
    {
        super(team, attack, defend, loc);
    }
    
    /**
     * Paints this soldier on the graphics passed at the specified x and y with the width and height 
     * all in pixel format with the attack value on the chest of the figure
     * @param g The graphics of the screen
     * @param x The x pixels the top left corner is
     * @param y The y pixels the top left corner is
     * @param width The width of the image
     * @param height The height of the image
     */
    public void paint(Graphics2D g, int x, int y, int width, int height)
    {


		

    	super.paint(g, x, y, width, height);
    	
        if(!isOnMyTeam())
        	return;
        
		if(image == null)
			try {
				image = ImageIO.read(new File(root + getAttackValue() + ".png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        
		g.drawImage(image, x + (width / 3) +(int) (xPartialLocation * width), y + (height / 3) + (height / 22) +(int) (yPartialLocation * height), width / 3, height / 3, null, null);
    	

    }
}
