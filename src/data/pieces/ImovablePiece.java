package data.pieces;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import data.nodes.Location;

/**
 * Class that will hold pieces that do not move (flag and bomb)
 * @author ldengler56
 *
 */
public class ImovablePiece extends Piece
{
	/** 
	 * Constructor for the class
	 * @param col The color of the team it is contained on
	 * @param attack The attack value of the piece
	 * @param defend The defense value of the piece
	 * @param loc The location the piece will be contained at
	 */
    public ImovablePiece(Color col,  int attack, int defend, Location loc)
    {
        super(col, attack, defend, loc);
    }
    
    /**
     * Method that when clicked will display a message saying that this piece can not move
     */
    public HashSet<Location> getMoveLocs()
    {
        
//        Output.printLn("This is an immovable piece which can't be moved. Try to move another piece.");
        
        //returns an empty array, the turn should not be ended
        return new HashSet();
    }
    
    
}
