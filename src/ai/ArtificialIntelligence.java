package ai;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import stratego.gui.MainFrame;
import data.nodes.Barracks;
import data.nodes.Board;
import data.nodes.Game;
import data.nodes.Location;
import data.pieces.*;

public class ArtificialIntelligence
{
    Game g1;
    Color myColor;
    public Board myBoard;
    private Board myBarrack;
    
    
    public ArtificialIntelligence(Game newGame, Color newColor)
    {
        g1=newGame;
        myColor=newColor;
        myBoard = g1.getGameBoard();
        myBarrack = g1.getBarracks().getBoard((MainFrame.getRunner().myTeamNum + 1) % 2);
        //constructBoard();
    }
    
    /**
     * a method that fills the board of the AI with all of the pieces using a version of the barracks constructor but rearranged
     */
    public void constructBoard()
    {   
        for(int i=0; i<40; i++)
        {
            Location loc= myBoard.get(i%10,i/10 );
            //go through the thing and take the case statements and mess with them from the barracks
            switch(i)
            {
              //bombs
                case  25: 
                case  18:
                case  12:
                case  11:
                case  3:
                case  0:  g1.move(getPos(new Bomb(null, null)), loc);
                break;
                
                //scouts
                case  5:
                case  19:
                case  17:
                case  13:
                case  26:
                case  39:
                case  34:
                case  32: g1.move(getPos(new Scout(null, null)), loc);
                break;
                
                //miners
                case  36:
                case  6:
                case  28:
                case  21:
                case  33: g1.move(getPos(new Miner(null, null)), loc);
                break;
                      
                //fours
                case  8:
                case  1:
                case  15:
                case  29: g1.move(getPos(new Soldier(null, 4, null)), loc);
                break;
                
                //fives
                case  24:
                case  9:
                case  38:
                case  31: g1.move(getPos(new Soldier(null, 5, null)), loc);
                break;
                
                //sixes
                case  4:
                case  16:
                case  14:
                case  30: g1.move(getPos(new Soldier(null, 6, null)), loc);
                break;
                
                //sevens
                case  10:
                case  35:
                case  37: g1.move(getPos(new Soldier(null, 7, null)), loc);
                break;
                
                      
                //eights
                case  7:
                case  22: g1.move(getPos(new Soldier(null, 8, null)), loc);
                break;
                
                //nine
                case  20: g1.move(getPos(new Soldier(null, 9, null)), loc);
                break;
                
                //ten
                case  27: g1.move(getPos(new Soldier(null, 10, null)), loc);
                break;
                
                //spy
                case  23: g1.move(getPos(new Soldier(null, 11,0, null)), loc);
                break;
                
                //flag
                case 2: g1.move(getPos(new Flag(null, null)), loc);
                break;
                
                
                default: loc.setPiece(null);
                break;
          }
                
        }
        
        
        
    }

    private <T extends Piece> Location getPos(T object)
    {
    	for(Location current: myBarrack)
    	{
    		System.out.println("null" + current.getPiece() != null);
    		if(current.getPiece() != null && current.getPiece().getClass().equals(object.getClass()) && current.getPiece().getAttackValue() == object.getAttackValue())
    		{
    			System.out.println("Got it:" + current);
    			return current;
    		}
    	}
    	return myBarrack.get(0, 0);
    	
    }
    
    public Move getBestMove()
    {
       int bestVal=-100;
       Move best=null;
       Move cur;
       for(Location loc: g1.getGameBoard()) //gets every location in the board
       {
           if(loc.getPiece()!=null && loc.getPiece().getColor().equals(myColor))//ensures that the piece of the AI's color
           {
               for(Location to: loc.getPiece().getMoveLocs())
               {    
                   cur= new Move(loc, to, g1.getGameBoard());
                   if(cur.getVal()==bestVal)
                   {
                       if(Math.random() > .5)
                       {
                           bestVal=cur.getVal();
                           best=cur;
                       }
                   }
                   else if(cur.getVal()>bestVal)
                   {
                       bestVal=cur.getVal();
                       best=cur;
                   } 
               }
           }
       }
       return best;
    }
    
    public void getMove()
    {
        Move toBeMade=getBestMove();
        g1.move(toBeMade.moveFrom, toBeMade.moveTo);
        //TODO send toBeMade to the folder
    }
    
    public HashSet<Location> getMyPieces()
    {
		return null;
        //TODO send the board somehow through the connection bridge
    }

	public void setBoard(Game currentGame) {
		// TODO Auto-generated method stub
		
	}
    
}
