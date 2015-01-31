package data.nodes;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;

import data.pieces.Flag;
import data.pieces.Piece;
import stratego.gui.MainFrame;
import stratego.gui.Output;



/**
 * Is the data structure to hold all of the statistics of the game, contains a barracks and game board. It also contains the graphics settings of the game
 * @author Luke Dengler
 *
 */
public class Game
{
    Board gameBoard;
    Barracks barracks;
    private BufferedImage image;
    private boolean setup = true;
  
    private static final int XSIZE=10; //variables that control how large the board will be
    private static final int YSIZE=10;
    
	private static final double WINDOW_BORDER = .05;
	private static final double GAME_BOARD_WIDTH = .5;
	private static final double GAME_BOARD_HEIGHT = .75;
	private static final double BARRACK_WIDTH = .35;
	private static final double BARRACK_HEIGHT = .9;
	public boolean haveWon;
	private static final int MINIMUM_PIECES_TO_PLACE = 4;
	
	private int myTeam;
	private Color[] teamColors;
	
    private boolean hasStarted;
    
    /**
     * Generates the Game board, constructs the barracks and game board
     */
    public Game(int myTeam, Color[] teamColors)
    {
        gameBoard=new Board(XSIZE, YSIZE,  1 - WINDOW_BORDER - GAME_BOARD_WIDTH,
				WINDOW_BORDER,
				GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT, false);
        barracks=new Barracks(WINDOW_BORDER, WINDOW_BORDER,
				BARRACK_WIDTH,
				BARRACK_HEIGHT);
        hasStarted=false;
        
        this.teamColors = teamColors;
        this.myTeam = myTeam;
        
    }


    /**
     * 
     * @return The barracks of the game
     */
    public Barracks getBarracks()
    {
        // TODO Auto-generated method stub
        return barracks;
    }


    /**
     * Paints the game, by calling the barracks and game board's paint methods
     * @param graphics The graphics of the screen
     * @param width The width of the screen
     * @param height The height of the screen
     */
	public void paint(Graphics2D graphics, int width, int height) {
		if(image == null)
			try {
				image = ImageIO.read(new File(MainFrame.imageFolderRoot + "Backgrounds\\Background.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		graphics.drawImage(image, 0, 0, width, height, null, null);
		
		gameBoard.paint(graphics, width, height);
		barracks.paint(graphics, width, height);

		
	}


	/**
	 * Gets the element at the specified ratio of the screen
	 * @param xCoor The ratio of the x - coordinate
	 * @param yCoor The ratio of the y - coordinate
	 * @return The Location that is contained at the coordinates, null if no location is contained there
	 */
	public Location getElement(double xCoor, double yCoor) 
	{
		Location elem;
		if((elem = gameBoard.getElement(xCoor, yCoor)) != null)
			return elem;
		else
			return barracks.getElement(xCoor, yCoor);
	}


	/**
	 * Gets the possible moves of the piece located at the location sent over
	 * @param selected The location to analyze
	 * @return The possible moves of the piece at the location specified,
	 *  an empty Hash Set will be returned in the case of no piece found at that location
	 */
	public HashSet<Location> getPossibleMoves(Location selected) {
		if(selected.getPiece() != null)
		{
			if(!setup)
				return selected.getPiece().getMoveLocs();
			else
				return gameBoard.getOpenLocations();
		}
		return new HashSet<Location>();
	}


	/**
	 * Moves the piece at the last selected Location to the selected location
	 * @param lastSelected The target to be moved
	 * @param selected The location to move the target
	 */
	public void move(Location lastSelected, Location selected) {
		
		if(lastSelected.getPiece() == null)
			return;
		
		if(!setup)
		{
			gameBoard.move(lastSelected, selected);

		}
		else
		{
			if(barracks.contains(lastSelected.getPiece()))
			{
				lastSelected.getPiece().setLocation(selected);
				//gameBoard.set(selected, barracks.remove(lastSelected));
			}
			else
			{
				lastSelected.getPiece().setLocation(selected);
				//gameBoard.set(selected, lastSelected.getPiece());
				//lastSelected.setPiece(null);
			}
				

		}
		
	}
    
	/**
	 * @return the int of what team I am (0 or 1)
	 */
	public int getMyTeam() {
		return myTeam;
	}
	
	/**
	 * 
	 * @return the Color of my team
	 */
	public Color getMyTeamsColor()
	{
		return teamColors[myTeam];
	}
	
	/**
	 * 
	 * @return The color of my opponent
	 */
	public Color getOpponentsColor()
	{
		return teamColors[(myTeam + 1) % teamColors.length];
	}


	/**
	 * Sets the setup to false ending the setup period
	 */
	public void prepared() {
		setup = false;
		
	}

	/**
	 * Checks if the game is setup properly, if not it will display the reason
	 * @return
	 */
	public boolean properlySetup() {
		if(!gameBoard.containsFlag())
		{
			Output.println("Place your flag.");
			return false;
		}

		if(!gameBoard.hasAmountOfPieces(MINIMUM_PIECES_TO_PLACE))
		{
			Output.println("Place the minimum amount of pieces: " + MINIMUM_PIECES_TO_PLACE);
			return false;
		}
		Output.println("You are prepared for WAR!");
		return true;
	}


	/**
	 * 
	 * @return The gameboard
	 */
	public Board getGameBoard() {
		return gameBoard;
	}
    
    public boolean inWinSituation()
    {
    	boolean myFlag = false;
    	boolean myPieces = false;
    	boolean thierFlag = false;
    	boolean thierPieces = false;
    	
    	for(Location cur: gameBoard)
    	{
    		Piece p = cur.getPiece();
    		
    		if(p != null)
    		{
    			if(p instanceof Flag)
    			{
    				if(p.isOnMyTeam())
    					myFlag = true;
    				else
    					thierFlag = true;
    			}
    			else
    				if(p.isOnMyTeam())
    					myPieces = true;
    				else
    					thierPieces = true;
    		}
    	}
    	
    	haveWon = !(thierPieces && thierFlag);
    	
		return !(thierFlag && myFlag && thierPieces && myPieces);
    	
    }
}
