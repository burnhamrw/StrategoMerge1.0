package connection;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

import stratego.gui.MainFrame;
import stratego.gui.Output;
import stratego.gui.WaitScreen;

/**
 * This class creates a file that will send messages between two computers in the fcsd network.
 * 
 * @author rburnham99
 *
 */
public class ConnectionBridge{
	
	private File file;
	private static String fileRoot = MainFrame.connectionBridgeRoot;//"G:\\hs_CompSci\\AP Computer Science\\Game Projects Workspaces\\burnham_dengler\\StrategoConnectionBridge\\";
	private String fileLoc;
	private static String initiateKey = "INITIATE";
	private static String approveKey = "APPROVED";
	private static String directoryName = "directory.txt";
	private static File directory = new File(fileRoot + directoryName);
	private boolean isValid = false;
	private String name;
	private long lastSent;
	/**
	 * Creates a connection file that initializes with the message complete inside of it
	 * @param name The name of the connection file name
	 */
	public ConnectionBridge(String name)
	{
		this.fileLoc = fileRoot + name + ".txt";
		file = new File(this.fileLoc);
		FileWriter writer = null;
		String temp;
		this.name = name;
		System.out.println(fileLoc);
		if(checkDirectory(name))
		{
			MainFrame.setMyTeamNum(0);
			System.out.println("already");
			String line = attemptRead();
			System.out.println(line);
			if(line != null && line.equals(initiateKey + "\n"))
			{
				System.out.println("in");
				write(approveKey);
				isValid = true;
			}
			else
				MainFrame.close();
		}
		else
		{
			MainFrame.setMyTeamNum(1);
			System.out.println("new");
			addToDirectory(name);
			this.write(initiateKey);
			
		}
		
	}
	
	/**
	 * Checks if the connection is valid
	 * @return if the connection is valid
	 */
	public boolean isValid()
	{
		return isValid;
	}
	
	/**
	 * Checks the directory for the name of the file and if it is in use
	 * @param name The name of the file name that is being searched for in the directory
	 * @return The boolean if found, false if the directory was not found
	 */
	public boolean checkDirectory(String name)
	{
		if(getDirectoryNames().contains(name))
			return true;
		
		return false;
		
		
	}
	
	/**
	 * Gets the hashset with all names in the directory in it
	 * @return The HashSet of type string that has all names in the static directory
	 */
	public HashSet<String> getDirectoryNames()
	{
		Scanner dir = null;
		try {
			dir = new Scanner(directory);
		} catch (FileNotFoundException e) {
			System.out.println("Directory not found");
			return null;
		}
		
		HashSet<String> set = new HashSet<String>();
		
		while(dir.hasNext())
			set.add(dir.nextLine());
		
		return set;
	}
	
	/**
	 * This fills the directory with the names passed
	 * @param names HashSet of type string with the names that will be added
	 */
	private void fillDirectory(HashSet<String> names)
	{
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(directory));
		} catch (IOException e) {
			System.out.println("No write permissions were granted");
		}
		
		for(String cur: names)
		{
			try {

				writer.write(cur) ;
				writer.newLine();
			} catch (IOException e) {
				System.out.println("Error in writing names to the directory");
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Removes a name from the directory
	 * @param name The name to be removed
	 */
	public void deleteFromDirectory(String name)
	{
		HashSet<String> names = getDirectoryNames();
		
		names.remove(name);
		
		fillDirectory(names);
	}
	
	/**
	 * Adds the name to the static directory folder
	 * @param name The name of the file name to add
	 */
	public void addToDirectory(String name)
	{
		HashSet<String> oldNames = getDirectoryNames();
		
		oldNames.add(name);
		System.out.println(oldNames);
		fillDirectory(oldNames);
		
		
	}
	/**
	 * Reads the input when the file exists and then deletes.
	 * @return The string that was found in the connection
	 */
	public String attemptRead()
	{
		//waitForInput();
		String input = "";
		
		file = new File(fileLoc);
		try
		{
			Scanner reader = new Scanner(file);
			while(reader.hasNext())
			{
				input += reader.nextLine() + "\n";
			}
			
			reader.close();
			
			file.delete();
			
		}
		catch(IOException e)
		{
			return null;
		}
		lastSent = System.currentTimeMillis();
		return input;
	}

	/**
	 * Wait for the connection file to exist
	 */
	public void waitForInput()
	{
		//Math.abs(lastSent - file.lastModified()) > 100 &&
		while(true)
		{
			if( file.exists())
			{
				return;
			}
		}
		
	}
	
	/**
	 * Writes a message in the connection file
	 * 
	 * @param the message to send
	 */
	public void write(String a)
	{

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			bw.write(a);
			
			bw.close();
			
			lastSent = file.lastModified();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the connection
	 */
	public void closeConnectionBridge()
	{
		file.delete();
		this.deleteFromDirectory(name);
	}

	/**
	 * Waits for the connection to be approved
	 * 
	 */
	public void waitForApproval() {
		WaitScreen wait = new WaitScreen("for connection.");
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("begun waiting");
		while(true)
		{
			if(Math.abs(file.lastModified() - System.currentTimeMillis()) < 1000)
			{
				System.out.println("complete");
				deleteCurrentFile();
				isValid = true;
				wait.dispose();
				return;
			}
		}
		

		
	}

	/**
	 * Waits for the bridge to be read
	 */
	public void waitForRead() {
		while(true)
		{
			if(!file.exists())
			{
				return;
			}
		}
		
	}
	
	/**
	 * Checks if the input is available to be read
	 * @return If the input is available to be read
	 */
	public boolean isInputAvailable()
	{
		return file.exists();
	}

	/**
	 * Deletes the current file on the bridge
	 */
	public void deleteCurrentFile() {
		file.delete();
		
	}

}
