package data.nodes;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import stratego.gui.MainFrame;
import data.pieces.Piece;

/**
 * Water location is never a valid location to move to and also displays water when it is painted
 * @author burnh_000
 *
 */
public class WaterLocation extends Location
{

	private BufferedImage water;
	private BufferedImage shore;
	private final int rotation;
	/**
	 * Constructs a water location on the specified board at the coordinates sent
	 * @param newRow The row that it is in (y - value)
	 * @param newCol The column that it is in (x - value)
	 * @param newBoard The board it is contained in
	 */
    public WaterLocation(int newRow, int newCol, Board newBoard, int rotation)
    {

        super(newRow, newCol, newBoard, false);
    	this.rotation = rotation;
        try {
			water = ImageIO.read(new File(MainFrame.imageFolderRoot + "GameBoardImages\\Water.png"));
			shore = ImageIO.read(new File(MainFrame.imageFolderRoot + "GameBoardImages\\ShoreTopLeft.png"));
			
			int w = shore.getWidth();  
			int h = shore.getHeight();  
			BufferedImage newImage = new BufferedImage(w, h, shore.getType());
			Graphics2D g2 = newImage.createGraphics();
			g2.rotate(Math.toRadians(rotation * 90), w/2, h/2);  
		    g2.drawImage(shore,null,0,0);
			    
			shore = newImage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Nothing occurs because you cannot set a piece to a water location
     * @param the piece, but nothing is ever done with the piece
     */
    public void setPiece(Piece newPiece)
    {
        //displays message expressing that this Location will not hold a piece, invalid move
    }
    
    /**
     * @return null, because the water location will never hold a piece
     */
    public Piece getPiece()
    {
        //message expressing that this can't hold a piece
        return null;
    }
    
    /**
     * @param The enemy, but nothing is done with it
     * @return false, because the water location is always non-valid
     */
    public boolean isValid(Piece enemy)
    {
        return false;
    }
    
    /**
     * Paints this water location on the graphics passed at the specified x and y with the width and height all in pixel format
     * @param g The graphics of the screen
     * @param x The x pixels the top left corner is
     * @param y The y pixels the top left corner is
     * @param width The width of the image
     * @param height The height of the image
     */
    public void paint(Graphics2D g, int x, int y, int width, int height)
    {
    	g.drawImage(water, x + col * width, y + row * height, width, height, null, null);
    	g.drawImage(shore,x + col * width, y + row * height, width, height, null, null);
    }
}
