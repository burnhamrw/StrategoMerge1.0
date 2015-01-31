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
import javax.swing.JLabel;

public class WinDisplay extends JFrame implements ActionListener{

	private final JPanel contentPanel = new StrategoPanel(this);
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public WinDisplay() {
		MainFrame.getRunner().isEnabled(false);
		setBounds(100, 100, 185, 113);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addContainerGap(368, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addContainerGap(193, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new StrategoPanel(this);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Play Again");
				okButton.setActionCommand("OK");
				//buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Quit");
				buttonPane.add(cancelButton);
			}
		}
		
		if(MainFrame.getCurrentGame().haveWon)
			label.setText("You Won!");
		else
			label.setText("You Lost...");
		setLocationRelativeTo(null);
		
		MainFrame.format(okButton);
		MainFrame.format(cancelButton);
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		setUndecorated(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		dispose();
		MainFrame.close();
		
	}
}
