package data.nodes;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;

import stratego.gui.MainFrame;
import data.pieces.Flag;
import data.pieces.Piece;



/**
 * Represents a board both graphically and as a data structure. It contains locations.
 * @author Luke Dengler
 *
 */
public class Board implements Iterable<Location>
{
    private Location[][] gameBoard;
    
    
    //ROW - COL
    private boolean inBarracks;
    
	private double height;
	private double width;
	private double xPos;
	private double yPos;
    
    
    /**
     * creates the board
     * @param xsize num cols in the matrix
     * @param ysize num getRows() in the matrix
     */
    public Board(int col, int row, double xPos, double yPos, double width, double height, boolean inBarracks)
    {
        //creates the board and puts the water in the appropriate locations
    	this(xPos, yPos, width, height);
    	this.inBarracks = inBarracks;
    	
        gameBoard=new Location[col][row];
        
        this.inBarracks = inBarracks;
		
        for(int i=0; i<col*row; i++)
        {
            gameBoard[i%col][i/col]=new Location(i/col, i%col, this, inBarracks);    
        }
        
        if(col==10 && row==10)//determines if the board being made is a play board or a barracks
        {
            gameBoard[2][4]= new WaterLocation(4,2, this, 0);
            gameBoard[3][4]= new WaterLocation(4,3, this, 1);
            gameBoard[6][4]= new WaterLocation(4,6, this, 0);
            gameBoard[7][4]= new WaterLocation(4,7, this, 1);
            gameBoard[2][5]= new WaterLocation(5,2, this, 3);
            gameBoard[3][5]= new WaterLocation(5,3, this, 2);
            gameBoard[6][5]= new WaterLocation(5,6, this, 3);
            gameBoard[7][5]= new WaterLocation(5,7, this, 2);
        }
    }
    
    /**
     * Sets up the graphics ratios in the board constructor
     * @param xPos
     * @param yPos
     * @param width
     * @param height
     */
    private Board(double xPos, double yPos, double width, double height)
    {
		this.height = height;
		this.width = width;
		this.xPos = xPos;
		this.yPos = yPos;
    }
    
    
    /**
     * Getter for the amount of columns
     * @return The number of columns (x - value)
     */
    public int getCols()
    {
        return gameBoard.length;
    }

    
    /**
     * Getter for the amount of rows
     * @return The number of rows (y - value)
     */
    public int getRows()
    {
        return gameBoard[0].length;
    }
    
    
    /**
     * Gets the piece at the specified location
     * @param loc The location to get the piece from
     * @return The piece that is contained at the location sent
     */
    public Piece getPiece(Location loc)
    {
        return gameBoard[loc.getCol()][loc.getRow()].getPiece();
    }
    
    
    /**
     * Sets the piece to the location passed, does not check if there is another piece contained there
     * @param newLoc The location for the piece to be placed
     * @param newPiece The piece that will be placed
     * @return boolean representing if it was completed
     */
    public boolean set(Location newLoc, Piece newPiece)
    {
        newLoc.setPiece(newPiece);
        return true;
    }

    /**
     * Gets the location at the coordinates passed
     * @param col The column the location is contained in (x value)
     * @param row The row the location is contained in (y value)
     * @return The location at these coordinates
     */
    public Location get(int col, int row)
    {
        // TODO Auto-generated method stub
        return gameBoard[col][row];
    }
    
    /**
     * Gets the raw matrix the board holds
     * @return The Location matrix that the class holds
     */
    public Location[][] getBoard()
    {
        return gameBoard;
    }



    /**
     * Iterator over all locations the board contains
     * @return The iterator over all the locations that are contained in the board
     */
    public Iterator iterator()
    {
        ArrayList<Location> tempArray = new ArrayList<>();
        
        for(int i = 0; i < getCols(); i++)
        {
            for(int j = 0; j < getRows(); j++)
            {
                tempArray.add(gameBoard[i][j]);
            }
        }
        return tempArray.iterator();
    }
    
	/**
	 * Paints the board
	 * @param g The graphics from the mother panel
	 * @param panelWidth The current width of the panel
	 * @param panelHeight The current height of the panel
	 */
	public void paint(Graphics2D g, int panelWidth , int panelHeight)
	{	
		int squareWidth = (int) ((width / getCols()) * panelWidth);
		int squareHeight = (int) ((height / getRows()) * panelHeight);
		int xPos = (int) (this.xPos * panelWidth);
		int yPos = (int) (this.yPos * panelHeight);
		
		for(int i = 0; i < getCols(); i++)
		{
			for(int j = 0; j < getRows(); j++)
			{
				
			//	gameBoard[i][j].paint(g, xPos + squareWidth * i, yPos + squareHeight * j, squareWidth, squareHeight);
				gameBoard[i][j].paint(g, xPos, yPos, squareWidth, squareHeight);
			}
		}
		if(this == MainFrame.getCurrentGame().gameBoard)
			for(Piece p: MainFrame.getRunner().currentMoving)
				p.paint(g, xPos, yPos, squareWidth, squareHeight);
	}
	
	/**
	 * Checks if the two doubles are contained in the board
	 * @param xCoor The x ratio for where the location is
	 * @param yCoor The y ratio for where the location is
	 * @return a boolean representing if the coordinates are contained in the grid
	 */
	public boolean inBounds(double xCoor, double yCoor)
	{
		return xCoor >= xPos && xCoor <= xPos + width && yCoor >= yPos && yCoor <= yPos + height;
	}
	
	/**
	 * Retrieves the location that is contained at these coordinates of the board
	 * @param xCoor The ratio to the right of the board
	 * @param yCoor The ratio to the left of the board
	 * @return The location that is contained at that position
	 */
	public Location getElement(double xCoor, double yCoor)
	{
		if(inBounds(xCoor, yCoor))
		{
			int col = (int) ((xCoor - xPos) / (width / getCols()));
			int row = (int) ((yCoor - yPos) / (height / getRows()));
			
			return gameBoard[col][row];
		}
		else
			return null;
		
	}

	/**
	 * Returns the open locations that can have a piece spawned into(excludes water locations)
	 * @return The HashSet of locations filled with the empty locations
	 */
	public HashSet<Location> getOpenLocations() {
		HashSet<Location> empties = new HashSet<Location>();

		for(Location cur: this)
			if(cur.getPiece() == null && !(cur instanceof WaterLocation) && isInTeamSpecifiedArea(cur))
				empties.add(cur);
		
		return empties;
	}
	
	/**
	 * Returns if the location is in the team specified area, team 0 is top and team 1 is bottom
	 * @param l The location to check
	 * @return boolean if the location is in the team specified area
	 */
	public boolean isInTeamSpecifiedArea(Location l)
	{
		int rowBound = (int) (getRows() - (.5 * getRows()));
		
		if(MainFrame.getRunner().myTeamNum == 0)
			return l.getRow() < rowBound - 1;
		else
			return l.getRow() >= rowBound + 1;
	}
	
	/**
	 * Moves the piece at the old location to the new location using the pieces move method
	 * @param oldL The old location of the piece
	 * @param newL The location that piece will move to
	 */
	public void move(Location oldL, Location newL)
	{
		if(oldL.getPiece() != null)
			oldL.getPiece().move(newL);
		
		MainFrame.getRunner().repaint();
	}

	/**
	 * If the board is holding a specified amount of pieces >=
	 * @param number The number of pieces to break the threshold
	 * @return Boolean if that amount of pieces are in the board
	 */
	public boolean hasAmountOfPieces(int number) {
		int num = 0;
		
		for(Location cur: this)
		{
			if(cur.getPiece() != null)
				num++;
		}
		
		return num >= number;
	}
	
	/**
	 * 
	 * @return If the board contains a flag
	 */
	public boolean containsFlag()
	{
		for(Location cur: this)
			if(cur.getPiece() instanceof Flag)
				return true;
		return false;
	}
	
	public String toString()
	{
		if(inBarracks)
		{
			if(MainFrame.getCurrentGame().getBarracks().getBoard(Barracks.RED) == this)
				return "red";
			else
				return "blue";
		}
		else
			return "gameboard";
	}
}
