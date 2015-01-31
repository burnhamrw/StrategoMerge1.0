package stratego.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import stratego.gui.MainFrame;

/**
 * This GUI selects the game mode wish to be played
 * @author burnh_000
 *
 */
public class GameModeSelect extends JFrame implements ActionListener{

	private final JPanel contentPanel = new StrategoPanel(this);
	private JComboBox comboBox;
	private static final String[] options = {"Multiplayer", "Singleplayer"};
	private JButton okButton;
	private JButton cancelButton;
	private boolean isMultiplayer;
	private boolean set = false;

	/**
	 * Create the dialog.
	 */
	public GameModeSelect() {
		setBounds(100, 100, 271, 126);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setUndecorated(true);
		setLocationRelativeTo(null);
		JLabel lblGameMode = new JLabel("Game mode:");
		lblGameMode.setForeground(MainFrame.fontColor);
		comboBox = new JComboBox(options);
		comboBox.setBackground(MainFrame.backgroundColor);
		comboBox.setForeground(MainFrame.fontColor);
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblGameMode)
					.addGap(18)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(253, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGameMode))
					.addContainerGap(187, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new StrategoPanel(this);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		MainFrame.format(okButton);
		MainFrame.format(cancelButton);
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		this.setBackground(new Color(1.0f, 1.0f, 1.0f, .5f));
		this.setVisible(true);
		this.setAlwaysOnTop(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		
		if(command.equals("OK"))
		{
			String value = (String) comboBox.getSelectedItem();
			set = true;
			isMultiplayer = value.equals("Multiplayer");
			this.dispose();
		}
		else
		{
			this.dispose();
			MainFrame.close();
		}
		
	}

	/**
	 * Returns the game mode when it is selected
	 * @return If the game mode is multiplayer
	 */
	public boolean getGameMode() {
		while(MainFrame.running)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(set)
				return isMultiplayer;
		}
		this.dispose();
		return false;
	}
}
