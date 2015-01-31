package connection;

import java.util.HashSet;

import stratego.gui.MainFrame;
import data.nodes.Barracks;
import data.nodes.Board;
import data.nodes.Game;
import data.nodes.Location;
/**
 * This class sends moves across a specified connection bridge, it can also send full boards
 * @author burnh_000
 *
 */
public class MoveConnection {
	
	private ConnectionBridge connector;
	
	/**
	 * Initializes the bridge
	 * @param connector The connection bridge that will be used
	 */
	public MoveConnection(ConnectionBridge connector)
	{
		this.connector = connector;
	}
	
	/**
	 * Sends the move across the bridge
	 * @param m The move to send
	 */
	public void send(Move m)
	{
		System.out.println(m);
		connector.write(m.toString());
	}
	
	/**
	 * Receives the move that is currently on the bridge
	 * @return The move that was sent
	 */
	public Move receive()
	{
		String raw = connector.attemptRead();
		System.out.println(raw);
		String[] num = raw.split("\n");
		Location[] locs = new Location[2];
		
		for(int i = 0; i < 2; i++)
		{
			int pos = i * 3;
			
			locs[i] = toLocation(num[pos], num[pos + 1], num[pos + 2]);
		}
		
		return new Move(locs[0], locs[1]);
	}
	
	/**
	 * Gets the message that should be sent to deliver the location passed
	 * @param l The location to get the message for
	 * @return The message to send to communicate the string passed
	 */
	private String getSendMessage(Location l)
	{
		return l.toString();
	}
	
	/**
	 * Takes the strings in {col, row, board} format and creates a generic location for those properties
	 * @param str The string array in {col, row, board} format
	 * @return The generic location
	 */
	private Location toLocation(String[] str)
	{
		Board board;
		int[] numbers = new int[2];
		System.out.println(str);
		for(int i = 0; i < 2; i++)
		{
			numbers[i] = Integer.parseInt(str[i]);
		}
		
		if(str[2].equals("gameboard"))
			board = MainFrame.getCurrentGame().getGameBoard();
		else if(str[2].equals("red"))
			board = MainFrame.getCurrentGame().getBarracks().getBoard(Barracks.RED);
		else
			board = MainFrame.getCurrentGame().getBarracks().getBoard(Barracks.BLUE);
		
		return new Location(numbers[1], numbers[0], board, false);
	}
	
	/**
	 * Gets the location for the specified properties
	 * @param col The column of the location
	 * @param row The row of the location
	 * @param board The board of the location: gameboard, red, or blue. Blue is the default board
	 * @return The generic location
	 */
	private Location toLocation(String col, String row, String board)
	{
		String[] pass = {col, row, board};
		return toLocation(pass);
	}
	
	/**
	 * Sends the full game board across the bridge
	 * @param game The game that the board that contains the board that would like to be sent
	 */
	public void sendBoard(Game game)
	{
		String fullMessage = "";
		
		for(Location cur: game.getGameBoard())
		{
			if(cur.getPiece() != null)
			{
				fullMessage += cur.getPiece().getMyBarracksLoc() + "\n";
				fullMessage += cur.toString() + "\n";
				
			}
		}
		System.out.println(fullMessage);
		connector.write(fullMessage);
	}
	
	/**
	 * Gets the locations of the board that was sent over the bridge
	 * @return The HashSet of Moves that will recreate the board that was sent
	 */
	public HashSet<Move> getBoardLocations()
	{
		String raw = connector.attemptRead();
		HashSet<Move> result = new HashSet<Move>();
		String[] lines = raw.split("\n");
		
		for(int i = 0; i < lines.length / 6; i++)
		{
			int pos = i * 6;
			result.add(new Move(toLocation(lines[pos], lines[pos + 1], lines[pos + 2]),
					toLocation(lines[pos+3], lines[pos+4], lines[pos+5])));
			
		}
		
		return result;
		
		
	}

}
