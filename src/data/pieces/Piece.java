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
import stratego.gui.Output;
import data.nodes.Location;

/**
 * This class is a data structure and graphical representation. It moves around in a specified manner
 * @author burnh_000
 *
 */
public class Piece implements Runnable
{
    private int attackVal;
    private int defendVal;
    private Location myBarracksLoc;
    private Location currentLoc;
    private boolean isAlive = true;
    private Color myColor;
    private static final int WAIT_TIME = 20;
    private static final int FRAMES_TO_GET = 10;
    private Location moveTo;
    
	protected BufferedImage image;
	protected double xPartialLocation;
	protected double yPartialLocation;
    
    protected String fileName = MainFrame.imageFolderRoot + "PieceLabel\\Generic Soldier.png";
//    private int myID;
//    private static int lastID;
    
    
    /**
     * Constructor to create a piece with an attack and defend value on the specified team and located at the location sent
     * @param newCol The color of the team that the piece is contained on
     * @param newAttack The attack value
     * @param newDefend The defend value
     * @param newBarrack The Barracks location so that the piece can return to the same place after it dies
     */
    public Piece(Color newCol, int newAttack, int newDefend, Location newBarrack)
    {
        isAlive=true;//signifies this piece being in the barracks
        myBarracksLoc=newBarrack;
        currentLoc=newBarrack;
        myColor=newCol;
        attackVal=newAttack;
        defendVal=newDefend;
        setPartials(newBarrack);
        

    }
    
    /**
     * an overloaded constructor that will call the standard constructor using newVal for the attack and defend value
     * @param newCol
     * @param newVal
     * @param newBarrack
     */
    public Piece(Color newCol, int newVal, Location newBarrack)
    {
        this(newCol, newVal, newVal, newBarrack);
    }
    
    /**
     * 
     * @return the location of this piece
     */
    public Location getLocation()
    {
        return currentLoc;
    }
    
    /**
     * 
     * @return the attack value of this piece
     */
    public int getAttackValue()
    {
        return attackVal;
    }
    
    
    /**
     * 
     * @return the defend value of this piece
     */
    public int getDefendValue()
    {
        return defendVal;
    }
    
    /**
     * Gets the possible moves from the pieces current location
     * @return A hash set with the locations this piece can move to contained inside of it
     */
    public HashSet<Location> getMoveLocs()
    {
    	return getMoveLocs(currentLoc);
    }
    
    
    /**
     * Helper method to get the possible locations adjacent to the location passed
     * @param the location to find the possible move locations from
     * @return the four adjacent locations that can be moved to, checks that they can be moved to
     */
    protected HashSet<Location> getMoveLocs(Location loc)
    {
        HashSet<Location> locs=new HashSet<Location>();
        
        //if statements for the four surrounding squares
        int myColumn=loc.getCol();
        int myRow=loc.getRow();
        Location temp;
        
        
        //checks the right square
        if(myColumn+1 < loc.getBoard().getCols()) //checks that the location is on the board
        {
        	
            temp=loc.getBoard().get(myColumn +1, myRow); //sets a temp Location variable to the location being checked
            if(temp.isValid(this)) 
            	locs.add(temp); 
  
        }
        
        //checks the left square
        if(myColumn-1 >= 0)
        {
            temp=(loc.getBoard().get(myColumn - 1, myRow));
            if(temp.isValid(this))
                locs.add(temp);
        }
        
        //checks the bottom square
        if(myRow + 1 < loc.getBoard().getRows())
        {
            temp=(loc.getBoard().get(myColumn, myRow + 1));
            if(temp.isValid(this))
                locs.add(temp);
        }
        
        //checks the top square
        if(myRow-1 >= 0 )
        {
            temp=(loc.getBoard().get(myColumn, myRow - 1));
            if(temp.isValid(this))
                locs.add(temp);
        }
        
        return locs;
    }
    
    /**
     * 
     * @return this pieces color
     */
    public Color getColor()
    {
        return  myColor;
    }

    
    /**
     * takes a location and moves the piece to that location, or loses the combat and gets removed
     * @param loc
     */
    public void move(Location loc)
    {
        if(getMoveLocs().contains(loc))//this is a location which the piece can move to
        {
            Location movedFrom=currentLoc;
            moveToThreader(loc);
            if(loc.getPiece()==null)//the location is empty and is simply occupied by this piece
            {
                loc.setPiece(this);
            }
            else
            {
                
                Piece opposition=loc.getPiece();//gets the piece in the space that is being moved upon

                if(opposition.isOnMyTeam())
                {
                	Output.println("Your piece is a " + opposition.getMyName());
                	Output.println("Opponent's piece is a " + this.getMyName());
                }
                else
                {
                	Output.println("Your piece is a " + this.getMyName());
                	Output.println("Opponent's piece is a " + opposition.getMyName());
                }
                
                // ex: displayPieces(this, opposition);
                if(opposition instanceof Flag)
                {
                    //TODO Display a victory message and reset the game------------------------------------------------------------------
                }
                
                if(opposition.getDefendValue()>attackVal)
                {
                    //nothing occurs the moving piece will vanish and the other piece will be unaffected
                    this.returnToBarracks();
                }
                else if(opposition.getDefendValue()==attackVal)//a tie occurred
                {
                    loc.setPiece(null); //the piece being moved upon is removed, the moving piece will be removed later
                    this.returnToBarracks();
                    opposition.returnToBarracks();
                }
                else if(opposition.getDefendValue()<attackVal)//the moving piece wins
                {
                    loc.setPiece(this);
                    opposition.returnToBarracks();
                }
                        
            }
            
            movedFrom.setPiece(null);
        }
        
        
        
    }
    
    
    private void moveToThreader(Location loc) {
		
    	moveTo = loc;
    	
    	Thread m = new Thread(this);
    	m.start();
		
	}
    

	/**
     * @return what this piece would be called in common language
     */
    public String getMyName()
    {
        String myName = "";
        
        if(attackVal==11)
        {
            myName="Spy";
        }
        else
        {
            myName+=attackVal;
        }
        return myName;
    }

    /**
     * returns a piece to its barracks location
     */
    public void returnToBarracks()
    {
    	isAlive = false;
        myBarracksLoc.setPiece(this);
        if(moveTo == null)
        	setPartials(myBarracksLoc);
    }
    
    public String toString()
    {
        
        return "Attack: " + attackVal + "\tDefend:" + defendVal + "\tColor: " + myColor;
    }

    /**
     * sets this pieces location
     * @param location
     */
    public void setLocation(Location location)
    {
        currentLoc.setPiece(null);
        location.setPiece(this);
        
        setPartials(location);
    }
    
    public void setLocationRaw(Location location)
    {
    	currentLoc = location;
    }
    
    /**
     * moves the piece from the baracks to any given location on the board
     * @param loc
     */
    public void leaveBarracks(Location loc)
    {
    	isAlive = true;
        if(currentLoc.equals(myBarracksLoc))
        {
            currentLoc=loc;
            loc.setPiece(this);
            setPartials(loc);;
        }
    }
    
    /**
     * The partial locations are set to the locations
     * @param loc The loc to set them to
     */
    public void setPartials(Location loc)
    {
    	if(loc != null)
    	{
    		xPartialLocation = loc.getCol();
    		yPartialLocation = loc.getRow();
    	}
    }

    /**
     * Paints this piece on the graphics passed at the specified x and y with the width and height all in pixel format
     * @param g The graphics of the screen
     * @param x The x pixels the top left corner of the board
     * @param y The y pixels the top left corner of the board
     * @param width The width of the image
     * @param height The height of the image
     */
	public void paint(Graphics2D graphics, int x, int y, int width, int height) {
		if(this.image == null)
		{
	        if(!isOnMyTeam())
	        	fileName = MainFrame.imageFolderRoot + "PieceLabel\\EnemyTeamPiece.png";
			this.image = setImageToSpecifiedColor();
		}
		
		graphics.drawImage(image,(int) (x + xPartialLocation * width),(int) (y + yPartialLocation * height), width, height, null, null);
		
	}
	
	/**
	 * Takes the current image and removes all black and places the team color into the specified image
	 * @return The buffered image that is created
	 */
	protected BufferedImage setImageToSpecifiedColor()
	{
		BufferedImage image = null;

		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < image.getWidth(); i++)
		{
			for(int j = 0; j < image.getHeight(); j++)
			{
				Color avg = new Color(image.getRGB(i, j));
				
				if(image.getRGB(i,j) == Color.BLACK.getRGB())
					image.setRGB(i, j, getColor().getRGB());
			}
		}
		
		return image;
		
	}
	
	/**
	 * 
	 * @return if this piece is on my team
	 */
	public boolean isOnMyTeam()
	{
		return MainFrame.isMyTeam(myColor);
	}

	/**
	 * @return the myBarracksLoc
	 */
	public Location getMyBarracksLoc() {
		return myBarracksLoc;
	}


	public void run() 
	{
		double xChange = (moveTo.getCol() - xPartialLocation) * 1.0 / FRAMES_TO_GET;
		double yChange = (moveTo.getRow() - yPartialLocation) * 1.0 / FRAMES_TO_GET;
		MainFrame.getRunner().currentMoving.add(this);
		
		for(int i = 0; i < FRAMES_TO_GET; i++)
		{
			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			xPartialLocation += xChange;
			yPartialLocation += yChange;
			
			MainFrame.getRunner().repaint();
		}
		
		if(isAlive)
			setPartials(moveTo);
		else
			setPartials(myBarracksLoc);

		
		moveTo = null;
		
		MainFrame.getRunner().currentMoving.remove(this);
	}

}
