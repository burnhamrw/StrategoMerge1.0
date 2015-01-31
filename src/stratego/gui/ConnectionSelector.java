package stratego.gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import actions.CloseAction;

/**
 * This class is the GUI to select the name of the connection and initialize it properly
 * @author burnh_000
 *
 */
public class ConnectionSelector extends JFrame implements ActionListener{
	
	JPanel panel = new StrategoPanel(this);
	private JButton exit = new JButton("Exit");
	private JButton select = new JButton("Select");
	private JTextField name = new JTextField("Connection name", 10);
	
	/**
	 * Constructs the GUI and also sets up the listeners
	 */
	public ConnectionSelector()
	{
		this.setSize(400, 200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		panel.getInputMap(GameGUI.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE), "EXIT");
		panel.getActionMap().put("EXIT" ,new CloseAction());
		
		this.setUndecorated(true);
		this.add(panel);
		
		this.setVisible(true);
		
		exit.addActionListener(this);
		select.addActionListener(this);
		
		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setForeground(MainFrame.fontColor);
		GroupLayout gl_contentPane = new GroupLayout(panel);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(82)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(select)
						.addComponent(lblConnection))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
							.addComponent(exit))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(22)
							.addComponent(name, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)))
					.addGap(88))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(101, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblConnection)
						.addComponent(name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(80)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(select)
						.addComponent(exit))
					.addGap(28))
		);
		
		MainFrame.format(exit);
		MainFrame.format(select);
		
		panel.setLayout(gl_contentPane);
		name.setBackground(MainFrame.backgroundColor);
		name.setForeground(MainFrame.fontColor);
		this.setAlwaysOnTop(true);
	}
	
	/**
	 * Closes the connection screen
	 */
	public void close()
	{
		this.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		
		if(command.equals("Exit"))
		{
			MainFrame.close();
		}
		else if(command.equals("Select"))
		{
			this.dispose();
			MainFrame.initializeConnection(name.getText());


		}
		
	}
	
	public void waitDisplay()
	{
		name.setText("Waiting");
	}
	
	

}
