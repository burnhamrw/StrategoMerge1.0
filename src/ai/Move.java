package ai;
import java.util.Random;

import data.nodes.Board;
import data.nodes.Location;
import data.pieces.Bomb;
import data.pieces.Flag;
import data.pieces.Miner;
import data.pieces.Piece;
import data.pieces.Scout;


public class Move
{

    
    Location moveFrom;
    Location moveTo;
    Board myBoard;
    
    public Move(Location from, Location to, Board newBoard)
    {
        myBoard=newBoard;
        moveFrom=from;
        moveTo=to;
    }
    
    public int getVal()
    {
        //pre given a location that has a piece in it and a second location that can be moved to by the piece in the first location
       int val; //the pieces need to be on the right teams... and have pieces at all
       if(moveTo.getPiece() != null)
       {       
           Piece attacker=moveFrom.getPiece();
           Piece defender=moveTo.getPiece();  
           int def= defender.getDefendValue();
           int att= attacker.getAttackValue();
           
           
           if(defender instanceof Flag) //AI will win the game
           {
               val = 100;
           }
           else if(defender.getAttackValue()==11) //AI will capture a spy
           {
               val=99;
           }
           else if(defender instanceof Bomb)
           {
               val=0;
               if(attacker instanceof Miner)
                   val = 98;
           }
           else if(def>att)
           {
               val = 0;
           }
           else if(def==att)
           {
               val = def-1;
           }
           else if(def<att)
           {
               val =  def;
           }
           else
           {
               val=-100;
               //TODO print something about this, it should not occur... ever
           }
       }
       else//location moveTo does not contain a hostile piece
       {
           if(moveFrom.getPiece() instanceof Scout)
               val=20;//scouts move often into empty space for the purpose of scouting
           else if(moveFrom.getPiece().getAttackValue()==11)
               val=-1;//spies should not be moved into empty vulnerable spaces
           if(moveFrom.getPiece() instanceof Miner)
               val=-1;
           else
               val=10-moveFrom.getPiece().getAttackValue();//don't want to move high value pieces into empty vulnerable spaces    
       }

       return val;
    }
}
