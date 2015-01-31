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
import javax.swing.JLabel;

import stratego.gui.MainFrame;


public class WaitScreen extends JFrame implements ActionListener{

	private final JPanel contentPanel = new StrategoPanel(this);
	private JButton cancelButton;

	/**
	 * Create the dialog.
	 */
	public WaitScreen(String element) {
		setBounds(100, 100, 337, 100);
		setLocationRelativeTo(null);
		this.setUndecorated(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblPleaseWait = new JLabel("Please Wait..." + element);
			contentPanel.add(lblPleaseWait);
		}
		{
			JPanel buttonPane = new StrategoPanel(this);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(this);
			}
		}
		
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		MainFrame.getRunner().setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.close();
		MainFrame.close();
		
	}

	public void close() {
		this.dispose();
		MainFrame.getRunner().setEnabled(true);
		
	}

}
