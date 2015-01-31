package actions;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;

import stratego.gui.MainFrame;
import stratego.gui.Output;

/**
 * This class extends Abstract Action and is used to close the game
 * @author rburnham99
 *
 */
public class CloseAction extends AbstractAction {

	/**
	 * Closes the games window
	 */
	public void actionPerformed(ActionEvent e) {
		MainFrame.close();
	}

}