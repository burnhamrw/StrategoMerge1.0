package stratego.gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import stratego.gui.MainFrame;


public class SinglePlayerSettingPanel extends JFrame implements ActionListener{

	private final JPanel contentPanel = new StrategoPanel(this);
	private JButton cancelButton;
	private JButton okButton;
	private JComboBox comboBox;
	private boolean set = false;
	private int difficultyValue = -1;

	/**
	 * Create the dialog.
	 */
	public SinglePlayerSettingPanel() {
		setBounds(100, 100, 258, 117);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setUndecorated(true);
		setLocationRelativeTo(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblDifficulty = new JLabel("Difficulty:");
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Easy", "Medium", "Hard"}));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDifficulty)
					.addGap(18)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(322, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDifficulty))
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
		setVisible(true);
		setAlwaysOnTop(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		
		if(command.equals("OK"))
		{
			String value = (String) comboBox.getSelectedItem();
			
			if(value.equals("Easy"))
				difficultyValue  = 0;
			else if(value.equals("Medium"))
				difficultyValue = 1;
			else
				difficultyValue = 2;
			
			set = true;
			
			this.dispose();
			//TODO do something with the values
		}
		else
		{
			this.dispose();
			MainFrame.close();
		}
		
	}

	/**
	 * Waits for the difficulty to be selected
	 * @return The difficulty 0 <= 2
	 */
	public int getValue() {
		
		while(MainFrame.running)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(set)
				return difficultyValue;
		}
		return -1;
	}

}
