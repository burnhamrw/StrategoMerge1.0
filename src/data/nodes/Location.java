package data.nodes;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TransformAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import stratego.gui.MainFrame;
import data.pieces.Piece;

/**
 * This class is a data node containing a piece and always contained in a board.
 * @author Luke Dengler
 *
 */
public class Location
{
    protected int row;
    protected int col;
    Board myBoard;
    private Piece myPiece;
    private String fileName = MainFrame.imageFolderRoot + "GameBoardImages\\Ground.png";
    private String fileNameStone = MainFrame.imageFolderRoot + "GameBoardImages\\Stone.png";
    private String filenamePossible = MainFrame.imageFolderRoot + "GameBoardImages\\highlight.png";
    private String fileNameSelected = MainFrame.imageFolderRoot + "GameBoardImages\\selected.png";
    private BufferedImage image;
    private BufferedImage highlight;
    private boolean stone;
    private boolean possible;
    private int x, y, width, height;
	private Image selected;
    
    
    /**
     * Constructs the location
     * @param newRow The row it will be contained at (y value)
     * @param newCol The column that the location will be contained at (x value)
     * @param newBoard The board this location is contained in
     * @param stone If the piece should be displayed with a stone image
     */
    public Location(int newRow, int newCol, Board newBoard, boolean stone)
    {
    	
    	
        row=newRow;
        col=newCol;
        myBoard=newBoard;
        this.stone = stone;
    }
    
    
    /**
     * 
     * @return the column or the x- value of the location in the board
     */
    public int getCol()
    {
        return col;
    }
    
    
    
    /**
     * 
     * @return The row or y - value of the location in the board
     */
    public int getRow()
    {
        return row;
    }
    /**
     * 
     * @return the piece in this location
     */
    public Piece getPiece()
    {
        return myPiece;
    }

    /**
     * generic setter, also sets the piece's location to this location
     * @param newPiece
     */
    public void setPiece(Piece newPiece)
    {
        myPiece=newPiece;
        if(myPiece!=null)
            myPiece.setLocationRaw(this);
    }
    
    /**
     * the location is valid to moved on to if the enemy has a diffent color team than this piece and there is no piece on this location
     * @param enemy The piece that wishes to move to this location
     * @return boolean if this location is valid
     */
    public boolean isValid(Piece enemy)
    {
        return(myPiece==null || myPiece.getColor() != enemy.getColor());
    }


    /**
     * 
     * @return The board that the location is contained in
     */
    public Board getBoard()
    {
        // TODO Auto-generated method stub
        return myBoard;
    }


    /**
     * Paints the location and if it is the first time it initializes the images
     * @param g The graphics of the screen
     * @param x The x pixels the top left corner of the board
     * @param y The y pixels the top left corner of teh board
     * @param width The width of the image
     * @param height The height of the image
     */
	public void paint(Graphics2D graphics, int x, int y, int width, int height) {
		
		if(image == null)
		{
			if(stone)
				try {
					image = ImageIO.read(new File(fileNameStone));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else
				try {
					image = ImageIO.read(new File(fileName));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
			try {
				highlight = ImageIO.read(new File(filenamePossible));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				selected = ImageIO.read(new File(fileNameSelected));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Random rand = new Random();
			
			//Rotates the image:
			int rotation = rand.nextInt(4);
			int w = image.getWidth();  
			int h = image.getHeight();  
			BufferedImage newImage = new BufferedImage(w, h, image.getType());
			   	Graphics2D g2 = newImage.createGraphics();
			    g2.rotate(Math.toRadians(rotation * 90), w/2, h/2);  
			    g2.drawImage(image,null,0,0);
			    
			image = newImage;
		}
		
		
		graphics.drawImage(image, x + col * width, y + row * height,
					width, height, null, null);
		
		if(myPiece != null && !MainFrame.getRunner().currentMoving.contains(myPiece))
		{
			myPiece.paint(graphics, x, y, width, height);
		}
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		

		
	}


	/**
	 * @return All information about the location
	 */
	public String toString() {
		return col + "\n" + row + "\n" + myBoard.toString();
	}


	/**
	 * Paints that it is a valid location around this location
	 * @param g The screens graphics
	 */
	public void highlight(Graphics2D g) {

		g.drawImage(highlight,  x + col * width, y + row * height, width, height, null, null);

		
	}
	
	/**
	 * Paints that the location has been selected
	 * @param g The screens graphics
	 */
	public void paintSelected(Graphics2D g)
	{
		g.drawImage(selected, x + col * width, y + row * height, width, height, null, null);
	}
	
	/**
	 * Decides if they are equal by the row and col variable and if the boards are equal
	 * @param comparison the Location to compare to
	 * @return if they are equal
	 */
	public boolean equals(Location comparison)
	{
		return row == comparison.getRow() && col == comparison.getCol() && myBoard.equals(comparison.getBoard());
	}
}
