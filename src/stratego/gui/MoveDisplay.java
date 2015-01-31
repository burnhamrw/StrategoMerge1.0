package stratego.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.AttributedCharacterIterator;

public class MoveDisplay {
	
	private String text;
	
	public MoveDisplay(String text) {
		this.text = text;
	}
	
	public void paint(Graphics2D g, int x, int y)
	{
		g.setColor(MainFrame.fontColor);
		g.drawString(text, x, y);
	}

	public void setText(String string) {
		text = string;
		MainFrame.getRunner().repaint();
	}

}
