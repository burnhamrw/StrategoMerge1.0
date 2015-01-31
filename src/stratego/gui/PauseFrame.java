package stratego.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class PauseFrame extends JFrame implements ActionListener{

	private final JPanel contentPanel = new StrategoPanel(this);
	private JButton okButton;
	private JButton cancelButton;


	/**
	 * Create the dialog.
	 */
	public PauseFrame() {
		setBounds(100, 100, 222, 187);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			okButton = new JButton("Resume");
			getRootPane().setDefaultButton(okButton);
		}
		{
			cancelButton = new JButton("Quit");
			cancelButton.setActionCommand("Quit");
		}
		
		JButton btnNewGame = new JButton("New Game");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addGap(62)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(cancelButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
						.addComponent(okButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
						//.addComponent(btnNewGame, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(107))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(okButton)
					.addGap(18)
					//.addComponent(btnNewGame)
					//.addGap(18)
					.addComponent(cancelButton)
					.addContainerGap(22, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		
		MainFrame.format(okButton);
		MainFrame.format(cancelButton);
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		btnNewGame.addActionListener(this);
		this.setUndecorated(true);
		setVisible(true);
		setAlwaysOnTop(true);

		MainFrame.getRunner().setEnabled(false);
	}

	/**
	 * Enables the frame and closes this one
	 */
	public void close()
	{
		this.dispose();
		MainFrame.getRunner().setEnabled(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		if(action.equals("Resume"))
		{
			this.close();
		}
		else if(action.equals("Quit"))
		{
			this.close();
			MainFrame.close();
		}
		else
		{
			this.close();
			//MainFrame.playAgain();
		}
		
	}
}
