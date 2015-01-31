package stratego.gui;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import connection.Move;
import actions.CloseAction;
import data.nodes.Barracks;
import data.nodes.Board;
import data.nodes.Game;
import data.nodes.Location;
import data.pieces.Piece;



/**
 * This class displays the game to teh user and gets user input
 * @author rburnham99
 *
 */
public class GameGUI extends JPanel implements MouseListener, ActionListener, Runnable{
	
	private boolean setup = true;
	private Location lastSelected;
	private HashSet<Location> possMoves = new HashSet<Location>();
	private Game game;
	private JButton[] preparedButtons = {new JButton("Prepared"), new JButton("Prepared")};;
	private JButton pauseButton;
	private JButton removeButton;
	private Move lastMove;
	private Thread gameThread;
	private boolean myMove = true;
	
	private static final double WINDOW_BORDER = .05;
	private static final double GAME_BOARD_WIDTH = .5;
	private static final double GAME_BOARD_HEIGHT = .75;
	private static final double BARRACK_WIDTH = .35;
	private static final double BARRACK_HEIGHT = .9;
	private static final double OUTPUT_WIDTH = GAME_BOARD_WIDTH;
	private static final double OUTPUT_HEIGHT = 1.0 - GAME_BOARD_HEIGHT - 2 * WINDOW_BORDER - WINDOW_BORDER / 3;

	
	/**
	 * Creates the game board and sets the input maps
	 */
	public GameGUI()
	{
		super();
		getInputMap(GameGUI.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE), "EXIT");
		getActionMap().put("EXIT" ,new CloseAction());
		
		game = new Game(MainFrame.getRunner().myTeamNum, MainFrame.teamColors);
		
		this.addMouseListener(this);
		
		MainFrame.getRunner().moveDisplay = new MoveDisplay("Prepare for War!");
	}
	
	/**
	 * Sets the text area to a specified region based on the size of the screen
	 * @param width The width of the current frame
	 * @param height The height of the current frame
	 * @return The text area that was setup in specified location
	 */
	public JTextArea postSizeSetup(int width, int height)
	{
		this.removeAll();
		JScrollPane scrollPane = new JScrollPane();


		boolean offsetOutput = MainFrame.getRunner().myTeamNum == 1 || !setup;
		pauseButton = new JButton("Pause");
		removeButton = new JButton("Remove");

		MainFrame.format(pauseButton);
		MainFrame.format(removeButton);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap((int) (((WINDOW_BORDER + BARRACK_WIDTH / 2) * width) - (preparedButtons[0].getPreferredSize().getWidth() / 2)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(preparedButtons[1])
							.addGap((int) (((BARRACK_WIDTH /2 + WINDOW_BORDER) * width) - (preparedButtons[0].getPreferredSize().getWidth() / 2))
									+ (offsetOutput ? (int)(preparedButtons[0].getPreferredSize().getWidth()): 0))
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, (int) (OUTPUT_WIDTH *width), GroupLayout.PREFERRED_SIZE))
						.addComponent(preparedButtons[0]))
					.addContainerGap(50, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(268, Short.MAX_VALUE)
					.addComponent(removeButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pauseButton)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(pauseButton)
						.addComponent(removeButton))
					.addGap((int) ((WINDOW_BORDER + BARRACK_HEIGHT / 4) * height - pauseButton.getPreferredSize().getHeight()))
					.addComponent(preparedButtons[0])
					.addPreferredGap(ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, (int) (OUTPUT_HEIGHT * height), GroupLayout.PREFERRED_SIZE)
							.addGap((int) (WINDOW_BORDER * height)))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(preparedButtons[1])
							.addGap((int) (((BARRACK_HEIGHT / 4) + WINDOW_BORDER) * height)))))
							
		);
		
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		scrollPane.setFocusable(false);
		textArea.setFocusable(false);
		textArea.setEditable(false);
		this.setLayout(groupLayout);
		
		for(JButton cur: preparedButtons)
			MainFrame.format(cur);
		
		removeButton.setVisible(false);
		preparedButtons[(MainFrame.getRunner().myTeamNum + 1) % 2].addActionListener(this);
		pauseButton.addActionListener(this);
		removeButton.addActionListener(this);
		preparedButtons[MainFrame.getRunner().myTeamNum].setVisible(false);
		return textArea;
	}
	
	/**
	 * Paints the gui
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D graphics = (Graphics2D) g;
		

		
		game.paint(graphics, getWidth(), getHeight());
		
		
		for(Location cur: possMoves)
		{
			cur.highlight(graphics);
		}
		if(lastSelected != null)
			lastSelected.paintSelected(graphics);
		
		MainFrame.getRunner().moveDisplay.paint(graphics, (int) ((.5 + WINDOW_BORDER / 2 + GAME_BOARD_WIDTH/ 2 - .1) * getWidth()), (int) (.9 * WINDOW_BORDER * getHeight()));
		//board.paint(graphics, getWidth(), getHeight());
		//barracks.paint(graphics, getWidth(), getHeight());
	}
	
	/**
	 * The last location is going to be set to the current cooridinates in the current board if != to null
	 * @param xCoor The ratio to the right
	 * @param yCoor The ratio to the top
	 * @param boardToCheck The Board that will be checked
	 * @return A boolean if the element is valid
	 */
	private boolean setLastLocation(double xCoor, double yCoor, Board boardToCheck)
	{
		Location temp= boardToCheck.getElement(xCoor, yCoor);
		if(temp != null)
		{
			lastSelected = temp;
			return true;
		}
		return false;
		
	}
	
	/**
	 * Gets the location on the specified board and location
	 * @param xCoor The ratio to the right
	 * @param yCoor The ration to the top
	 * @param board The board to be checked
	 * @return The location at the specified location
	 */
	private Location getLocation(double xCoor, double yCoor, Board board)
	{
		return board.getElement(xCoor, yCoor);
	}
	
	/**
	 * 
	 * @return The game that is contained in this gui
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Deals with a location being clicked with mouse
	 */
	public void mouseClicked(MouseEvent e) {
		if(myMove)
		{
			double xCoor = e.getX() * 1.0 / getWidth();
			double yCoor = e.getY() * 1.0 / getHeight();
			
			boolean isFound = false;
			
			Location selected = game.getElement(xCoor, yCoor);
			
			if(selected != null)
			{
				if((selected.getPiece() != null && !selected.getPiece().isOnMyTeam()))
				{
					if(possMoves.contains(selected))
					{
						System.out.println("MOVING");
						game.move(lastSelected, selected);
						lastMove = new Move(lastSelected, selected);
						lastSelected = null;
						possMoves = new HashSet<>();
					}
					System.out.println("0");
					lastSelected = null;
					possMoves = new HashSet<>();
				}
				else if(!setup && selected.getBoard() != game.getGameBoard())
				{
					System.out.println("1");
					lastSelected = null;
					possMoves = new HashSet<>();
				}
				else if(lastSelected == null)
				{
					System.out.println(3);
					lastSelected = selected;
					possMoves = game.getPossibleMoves(selected);
					
				}
				else if(possMoves.contains(selected))
				{
					game.move(lastSelected, selected);
					lastMove = new Move(lastSelected, selected);
					lastSelected = null;
					possMoves = new HashSet<>();
	
				}
				else
				{
					System.out.println("4");
					lastSelected = selected;
					possMoves = game.getPossibleMoves(selected);
				}
			}
		
		
			setRemoveable(lastSelected != null && setup && lastSelected.getPiece() != null);
			
			MainFrame.getRunner().repaint();
		}	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String eventCommand = event.getActionCommand();
		
		if(eventCommand.equals("Prepared"))
		{
			prepared();
		}
		else if(eventCommand.equals("Remove"))
		{
			lastSelected.getPiece().returnToBarracks();
			lastSelected.setPiece(null);
			lastSelected = null;
			possMoves = new HashSet<Location>();
		}
		else
			MainFrame.pause();
		this.repaint();
	}

	/**
	 * Checks that the preparation is complete to a point and if it is ends the setup period.
	 */
	public void prepared() {
		if(game.properlySetup())
		{	
			this.setMoveable(false);
			if(MainFrame.multiplayer)
			{
				HashSet<Move> otherTeam = MainFrame.exchangeBoard();
				updateOpponentSetup(otherTeam);
			}
			else
				MainFrame.getRunner().ai.constructBoard();
			
			setup = false;
			game.prepared();
			

			preparedButtons[(MainFrame.getRunner().myTeamNum + 1) % 2]
				.setVisible(false);
			Output.setOutput(postSizeSetup(GameGUI.this.getWidth(),
				GameGUI.this.getHeight()));
			MainFrame.getRunner().repaint();

			
			
			Output.println("The war has begun...");
			lastMove = null;
			System.out.println("here");
			
			gameThread = new Thread(MainFrame.getRunner());
			gameThread.start();
			this.repaint();

		}
		
		//TODO add multiplayer setting
	}
	
	private void updateOpponentSetup(HashSet<Move> otherTeam) {
		for(Move cur: otherTeam)
		{
			Location from = cur.getFrom();
			Location to = cur.getTo();

			Location inBoard = game.getGameBoard().get(to.getCol(), to.getRow());
		
			Location inBarracks = from.getBoard().get(from.getCol(), from.getRow());

			
			game.move(inBarracks, inBoard);
		}
		this.repaint();
		
	}

	/**
	 * 
	 * @param setting If the remove button should be displayed
	 */
	private void setRemoveable(boolean setting)
	{
		removeButton.setVisible(setting);
	}
	
	/**
	 * Sets if the player can input their move
	 * @param flag If the player can input a move
	 */
	public void setMoveable(boolean flag)
	{
		myMove = flag;
	}
	
	/**
	 * Gets the move that was last inputed
	 * @return The move last inputed
	 */
	public Move getMove()
	{
		while(lastMove == null)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Move temp = lastMove;
		lastMove = null;
		return temp;
			
	}
	
	/** 
	 * Generic move made to setup the intial boards
	 * @param m
	 */
	public void move(Move m)
	{
		Location from = m.getFrom();
		Location to = m.getTo();
		Location inBoard = from.getBoard().get(to.getCol(), to.getRow());
		
		Location fromActual = to.getBoard().get(from.getCol(), from.getRow());
		game.move(fromActual, inBoard);
	}
	
	/**
	 * Repaints the board
	 */
	public void run()
	{
		this.repaint();
	}
}
