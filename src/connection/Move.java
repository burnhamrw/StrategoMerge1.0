package connection;

import data.nodes.Location;

/**
 * This class contains the information for a move containing a location that the location will move from to the to
 * 
 * @author rburnham99
 *
 */
public class Move {

	/**
	 * @return the from
	 */
	public Location getFrom() {
		return from;
	}
	
	/**
	 * @return the to
	 */
	public Location getTo() {
		return to;
	}
	
	public Move(Location from, Location to) {
		super();
		this.from = from;
		this.to = to;
	}
	
	public String toString()
	{
		return "" + from.toString() + "\n" + to.toString();
	}
	
	private final Location from;
	private final Location to;
}
