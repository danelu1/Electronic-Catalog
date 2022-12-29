import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.simple.parser.ParseException;

class ParentLoginPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JLabel title;
	JLabel userLabel;
	JLabel passwordLabel;
	JTextField userText;
	JPasswordField passwordText;
	JButton loginButton;
	JButton goBackButton;
	static User user;
	JPanel userPanel;
	JPanel loginPanel;
	JPanel passwordPanel;
	JPanel goBackPanel;
	JPanel centerPanel;
	SelectionPage previousPage;
	ParentMainPage parentPage;
	
	public ParentLoginPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		setLayout(new BorderLayout());
		setResizable(false);
		
		title = new JLabel("------------------------------------ Please enter your login details ------------------------------------");
		title.setFont(new Font("Verdana", Font.BOLD, 15));
		
		userLabel = new JLabel("Parent User Name: ");
		passwordLabel = new JLabel("Parent User password: ");
		
		userText = new JTextField();
		userText.setColumns(30);
		
		passwordText = new JPasswordField();
		passwordText.setColumns(30);
		
		loginButton = new JButton("Log in");
		goBackButton = new JButton("Previous page");
		goBackButton.setBounds(100, 100, 100, 100);
		
		userPanel = new JPanel();
		userPanel.add(userLabel);
		userPanel.add(userText);
		
		loginPanel = new JPanel();
		loginPanel.add(userPanel);
		
		passwordPanel = new JPanel();
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordText);
		
		goBackPanel = new JPanel();
		goBackPanel.add(goBackButton);
		
		loginButton.addActionListener(this);
		goBackButton.addActionListener(this);
		
		centerPanel = new JPanel(new GridLayout(3, 1));
		centerPanel.add(loginPanel);
		centerPanel.add(passwordPanel);
		centerPanel.add(goBackPanel);
		
		title.setForeground(new Color(34, 90, 156));
		centerPanel.setForeground(Color.green);
		
		add(title, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(loginButton, BorderLayout.SOUTH);
		
		userText.addActionListener(this);
		
		pack();
		setVisible(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == goBackButton) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				this.dispose();
				previousPage = new SelectionPage("Selection");
			
			} else if (e.getSource() == loginButton) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					user = catalog.checkUserNamePassword("Parent", this.userText.getText(), this.passwordText.getText());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				
				if (user != null) {
					this.dispose();
					parentPage = new ParentMainPage("Parent Page");
				} else { 
					this.title.setText("---------------------------------------- Invalid email or password! ----------------------------------------");
				}
			}
		}
	}
}