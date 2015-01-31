package actions;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import stratego.gui.MainFrame;

/**
 * This action is used to initiate game play by calling the main frames playGame method
 * @author rburnham99
 *
 */
public class PlayAction extends AbstractAction implements Action {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		MainFrame.getRunner().playGame();

	}

}
