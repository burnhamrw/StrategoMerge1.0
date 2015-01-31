package data.nodes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import stratego.gui.MainFrame;
import data.pieces.Bomb;
import data.pieces.Flag;
import data.pieces.Miner;
import data.pieces.Piece;
import data.pieces.Scout;
import data.pieces.Soldier;


/**
 * This class represents the barracks in the game both as a data structure and as a graphics component
 * @author Luke Dengler
 *
 */
public class Barracks implements Iterable<Location>
{
    private static final int SPY_DEFEND = 0;
    private static final int SPY_ATTACK = 11;
    private final int numPieces=40;
    private Board blueBarracks;
    private Board redBarracks;
    private final int  teamHeight=6;
    private final int  teamWidth=7;
    private Board[] barracks = new Board[TEAM_SIZE];

    private static final int TEAM_SIZE = 2;
    public static final int RED = 0;
    public static final int BLUE = 1;
    private static final Color[] teamColors = MainFrame.teamColors;
    
    /**
     * constructs the barracks and all of the pieces within it
     * 
     */
    public Barracks(double xPos, double yPos, double width, double height)
    {
    	
        barracks[0] = new Board(teamWidth, teamHeight, xPos, yPos, width, height / 2, true);
        barracks[1] = new Board(teamWidth, teamHeight, xPos, yPos + height / 2, width, height / 2, true);
        
        /*  6 bombs
         *  8 scouts
         *  5 miners
         *  4 fours
         *  4 fives
         *  4 sixes
         *  3 sevens
         *  2 eights
         *  1 nine
         *  1 ten
         *  1 spy
         *  1 flag
         */
        
        Color curTeam;
        Location loc;
        
        for(int barrackNum = 0; barrackNum < TEAM_SIZE; barrackNum++)
        {          
            curTeam = teamColors[barrackNum];
            
            for(int i=0; i<40; i++)
            {
                loc= barracks[barrackNum].get(i % (barracks[barrackNum].getCols()), i/barracks[barrackNum].getCols()); 
                switch(i)
                {
                  //bombs
                  case  0: 
                  case  1:
                  case  2:
                  case  3:
                  case  4:
                  case  5:  loc.setPiece(new Bomb(curTeam, loc));
                  break;
                  
                  //scouts
                  case  6:
                  case  7:
                  case  8:
                  case  9:
                  case  10:
                  case  11:
                  case  12:
                  case  13: loc.setPiece(new Scout(curTeam, loc));
                  break;
                  
                  //miners
                  case  14:
                  case  15:
                  case  16:
                  case  17:
                  case  18: loc.setPiece(new Miner(curTeam, loc));
                  break;
                        
                  //fours
                  case  19:
                  case  20:
                  case  21:
                  case  22: loc.setPiece(new Soldier(curTeam, 4, loc));
                  break;
                  
                  //fives
                  case  23:
                  case  24:
                  case  25:
                  case  26: loc.setPiece(new Soldier(curTeam, 5, loc));
                  break;
                  
                  //sixes
                  case  27:
                  case  28:
                  case  29:
                  case  30: loc.setPiece(new Soldier(curTeam, 6, loc));
                  break;
                  
                  //sevens
                  case  31:
                  case  32:
                  case  33: loc.setPiece(new Soldier(curTeam, 7, loc));
                  break;
                  
                        
                  //eights
                  case  34:
                  case  35: loc.setPiece(new Soldier(curTeam, 8, loc));
                  break;
                  
                  //nine
                  case  36: loc.setPiece(new Soldier(curTeam, 9, loc));
                  break;
                  
                  //ten
                  case  37: loc.setPiece(new Soldier(curTeam, 10, loc));
                  break;
                  
                  //spy
                  case  38: loc.setPiece(new Soldier(curTeam, SPY_ATTACK, SPY_DEFEND, loc));
                  break;
                  
                  //flag
                  case 39: loc.setPiece(new Flag(curTeam, loc));
                  break;
                  
                  
                  default: loc.setPiece(null);
                  break;
                }
       
           }
                         
        }
       
    }
    
    /**
     * Prints all pieces in the Barracks
     * @return the String of all pieces toStrings
     */
    public String toString()
    {
        String out = "";
        for(Location cur: this)
        {
            if(cur==null || cur.getPiece()==null)
            {
                
            }
            else
            {
                out+=cur.getPiece().toString() + "\n";
            }
        }
        
        return out;
    }

    /**
     * Iterator with all locations in the two boards
     * @return an iterator with all the locations
     */
    public Iterator iterator()
    {
        ArrayList<Location> tempArray = new ArrayList<Location>();
        
        for(Board curBoard: barracks)
            for(Location curLocation: curBoard)
                tempArray.add(curLocation);

        
        return tempArray.iterator();
    }

    /**
     * Paints the barracks
     * @param graphics The graphics of the screen it is contained in
     * @param width The width of the screen
     * @param height The Height of the screen
     */
	public void paint(Graphics2D graphics, int width, int height) {
		for(Board cur: barracks)
			cur.paint(graphics, width, height);
		
	}

	/**
	 * Gets the element at the ratio specified if it is contained in the barracks
	 * @param xCoor Ratio x on the screen
	 * @param yCoor Ratio y on the screen
	 * @return The location if there is one at that position on the screen, null if otherwise
	 */
	public Location getElement(double xCoor, double yCoor) {
		
		Location elem;
		
		if((elem = barracks[0].getElement(xCoor, yCoor)) != null)
			return elem;
		else
			return barracks[1].getElement(xCoor, yCoor);
	}
	
	/**
	 * Removes the piece at the location that is specified
	 * @param l The location to remove the piece
	 * @return The piece that was removed, null if no piece was contained at that location
	 */
	public Piece remove(Location l)
	{
		Board boardContainer = l.getBoard();
		
		for(Board curCheck: barracks)
		{
			if(curCheck.equals(boardContainer))
			{
				Piece find = l.getPiece();
				curCheck.get(l.getCol(), l.getRow()).setPiece(null);
				return find;
			}
		}
		
		return null;
			
	}

	/**
	 * Returns if the piece is contained in the barracks
	 * @param piece the piece being search for
	 * @return true if the piece was found, false otherwise
	 */
	public boolean contains(Piece piece) {
		for(Location curLoc: this)
		{
			if(curLoc.getPiece() == piece)
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the proper board
	 * @param the number of the constant that specifies red or blue
	 * @param the Board that goes with the current color
	 */
    public Board getBoard(int num)
    {
    	return barracks[num];
    }
}
