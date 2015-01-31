package stratego.gui;
import java.awt.Color;

import javax.swing.JTextArea;

/**
 * This class deals with printing simple text messages to the user during the game into a specified text area
 * 
 * @author rburnham99
 *
 */
public class Output {


	/**
	 * The current console
	 */
	private static JTextArea console;
	
	/**
	 * Cannot create instance of this class
	 */
	private Output()
	{
		
	}
	
	/**
	 * Sets the output of the class to a new text area
	 * @param newOutput The new JTextArea
	 */
	public static void setOutput(JTextArea newOutput)
	{
		console = newOutput;
		console.setBackground(MainFrame.backgroundColor);
		console.setForeground(MainFrame.fontColor);
	}
	
	/**
	 * Prints the line out to the current output
	 * @param text The line wished to be printed
	 */
	public static void println(String text)
	{
		System.out.println("printing");
		if(console.getText().equals(""))
			console.setText(text);
		else
			console.setText(console.getText() + "\n" + text);
	}
}
