package stratego.gui;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;

import ai.ArtificialIntelligence;
import connection.ConnectionBridge;
import connection.Move;
import connection.MoveConnection;
import data.nodes.Board;
import data.nodes.Game;
import data.nodes.Location;
import data.pieces.Piece;

/**
 *  The JFrame that the game is placed in, the class is a singleton class
 * @author rburnham99
 *
 */
public class MainFrame extends JFrame implements Runnable{
	
	/**
	 * Singleton runner
	 */
	private static MainFrame single;
	private static TitleScreen title;
	private static ConnectionSelector connection;
	public static String imageFolderRoot;
	private final static String bootFile = "StrategoBootOptions.txt";
	public static String connectionBridgeRoot;
	
	/**
	 * Contents of the page
	 */
	private GameGUI gui;
	private ConnectionBridge connectionBridge;
	private MoveConnection moveConnection;
	private int move = 0;
	private MusicPlayer musicPlayer;
	public static final Color fontColor = new Color(192, 102, 13);
	public static final Color backgroundColor = new Color(102, 51, 0);
	public static boolean multiplayer;
	public HashSet<Piece> currentMoving;
	
	public final static Color[] teamColors =  {new Color(245, 75, 75), new Color(79, 157, 246)};
	public int myTeamNum;
	private GameModeSelect selector;
	private int aiDifficulty;
	public ArtificialIntelligence ai;
	public MoveDisplay moveDisplay;
	public static boolean running =true;
	
	/**
	 * Creates the new runner sets it full screen and adds the game gui
	 */
	private MainFrame()
	{
		super();
		System.out.println(imageFolderRoot);
		readBoot();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		currentMoving = new HashSet<Piece>();
		setUndecorated(true);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());

		title = new TitleScreen();
		add(title);
		setVisible(true);
		
	}
	
	/**
	 * Reads the boot file to get the photo and bridge locations on the hard drive and starts the music
	 */
	private void readBoot() {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(bootFile));
		} catch (FileNotFoundException e) {
			new ErrorPanel("Cannot locate boot file: " + bootFile);
			return;
		} catch (IOException e)
		{
			new ErrorPanel("Cannot locate boot file: " + bootFile);
			return;
		}
		
		imageFolderRoot = sc.nextLine();
		connectionBridgeRoot = sc.nextLine();

		sc.close();
		
		Thread music = new Thread(musicPlayer = new MusicPlayer());
		music.start();
	}

	/**
	 * The single class that is available of the runner
	 * @return Runner the frame that the game is played
	 */
	public static MainFrame getRunner()
	{
		if(single == null)
			return single = new MainFrame();
		else
			return single;
		
	}
	
	/**
	 * Runs the game
	 * @param args NO INPUT NEEDED
	 */
	public static void main(String[] args) {
		MainFrame.getRunner();
		MainFrame.getRunner().isEnabled(true);
		running = true;
		MainFrame.getRunner().selectGameMode();
	}
	
	/**
	 * Closes the current game, but does not end any running code.
	 */
	public static void close()
	{
		MainFrame.getRunner().running = false;
		if(connection != null)
			connection.close();
		connection = null;
		

		
		ConnectionBridge connectionBridge = MainFrame.getRunner().connectionBridge;
		
		if(connectionBridge != null)
			connectionBridge.closeConnectionBridge();
		
		MainFrame.getRunner().musicPlayer.close();
		
		MainFrame.getRunner().dispose();
		single = null;
	}
	
	/**
	 * This is called to setup the game and begin game play
	 */
	public void playGame()
	{
		getContentPane().removeAll();
		getContentPane().repaint();
		
		gui = new GameGUI();
		add(gui);

		Output.setOutput(gui.postSizeSetup(single.getWidth(), single.getHeight()));
		single.setVisible(true);
		
		System.out.println("Creating ai");
		if(!multiplayer)
			ai = new ArtificialIntelligence(getCurrentGame(), teamColors[0]);
		System.out.println("Done creating ai");
		Output.println("Welcome to Stratego");
		
		MainFrame.getRunner().setEnabled(true);

	}
	
	/**
	 * @return the current game the gui is displaying and playing
	 */
	public static Game getCurrentGame()
	{
		return MainFrame.getRunner().gui.getGame();
	}
	
	/**
	 * checks if the passed color is the color of my team
	 * @param passed The color to check
	 * @return If the color passed is equal to my teams color
	 */
	public static boolean isMyTeam(Color passed)
	{
		if(passed == null)
			return true;
		return passed.equals(teamColors[MainFrame.getRunner().myTeamNum]);

	}

	/**
	 * Pauses the game and opens the pause screen
	 */
	public static void pause() {
		//TODO Display pause menu
		
		new PauseFrame();
		
	}

	/**
	 * Initializes the connection of the connection bridge, has to wait for an acceptance
	 * @param text The text of the "server" that is wanted
	 */
	public static void initializeConnection(String text) 
	{
		MainFrame.getRunner().connectionBridge = new ConnectionBridge(text);
		ConnectionBridge connectionBridge = MainFrame.getRunner().connectionBridge;

		
		if(!connectionBridge.isValid())
		{
			connectionBridge.waitForApproval();
		}

		MainFrame.getRunner().moveConnection = new MoveConnection(connectionBridge);
		MainFrame.getRunner().setEnabled(true);
		MainFrame.getRunner().playGame();
		Output.println("Connected to: "  + text + connectionBridge.isValid());

	}

	/**
	 * @param myTeamNum the myTeamNum to set
	 */
	public static void setMyTeamNum(int myTeamNum) {
		MainFrame.getRunner().myTeamNum = myTeamNum;
	}

	/**
	 * Exchanges the board passed to the other side of the bridge
	 * @return The hashset of the Moves needed to mirror the opponents board
	 */
	public static HashSet<Move> exchangeBoard(){
		ConnectionBridge connectionBridge = MainFrame.getRunner().connectionBridge;
		MoveConnection moveConnection = MainFrame.getRunner().moveConnection;
		
		
		if(connectionBridge.isInputAvailable())
		{
			HashSet<Move> temp = moveConnection.getBoardLocations();
			moveConnection.sendBoard(getCurrentGame());
			return temp;
		}
		else
		{
			moveConnection.sendBoard(getCurrentGame());
			connectionBridge.waitForRead();
			System.out.println("waiting for input");
			connectionBridge.waitForInput();
			System.out.println("Getting locs");
			return moveConnection.getBoardLocations();
			
		}
	}
	
	/**
	 * Selects the game mode to play
	 */
	public void selectGameMode()
	{
		setEnabled(false);
		multiplayer = (new GameModeSelect()).getGameMode();
		if(!running)
			return;
		
		if(multiplayer)
		{
			connection = new ConnectionSelector();
		}
		else
		{
			myTeamNum = 1;
			aiDifficulty = (new SinglePlayerSettingPanel()).getValue();
			
			if(aiDifficulty == -1)
				return;
			
			System.out.println("Wanting to play game");
			MainFrame.getRunner().playGame();
		}
		
		setEnabled(true);
	}

	/**
	 * Plays the game
	 */
	public void run()
	{
		int turn = 0;
		System.out.println("Start");
		while(running)
		{
			getRunner().moveDisplay.setText("Your turn");
			if(turn == myTeamNum)
			{
				gui.setMoveable(true);
				if(multiplayer)
					moveConnection.send(gui.getMove());
				else
					gui.getMove();
			}
			else
			{
				getRunner().moveDisplay.setText("Opponent's turn.");
				gui.setMoveable(false);
				if(multiplayer)
				{
					connectionBridge.waitForInput();
					System.out.println("read");
					gui.move(moveConnection.receive());
				}
				else
					ai.getMove();
			}
			
			Thread t = new Thread(gui);
			t.start();
			
			if(getCurrentGame().inWinSituation())
				new WinDisplay();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			turn = (turn + 1) % 2;
		}
	}
	
	/**
	 * Sets if the window is enabled or not
	 * @param enabled if the window is enabled or not
	 */
	public void isEnabled(boolean enabled)
	{
		this.setFocusable(enabled);
	}
	
	
	public static void format(JButton button)
	{
		button.setBackground(backgroundColor);
		button.setForeground(fontColor);
	}
}
