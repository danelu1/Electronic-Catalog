import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class SelectionPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JLabel title;
	static JCheckBox studentBox;
	static JCheckBox parentBox;
	static JCheckBox assistantBox;
	static JCheckBox teacherBox;
	JButton selectButton;
	JButton coursesButton;
	JPanel titlePanel;
	JPanel checkPanel;
	JPanel selectPanel;
	ButtonGroup buttonGroup;
	ParentLoginPage parentPage;
	StudentLoginPage studentPage;
	AssistantLoginPage assistantPage;
	TeacherLoginPage teacherPage;
	GeneralInformationsPage nextPage;
	
	public SelectionPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		
		title = new JLabel("Welcome to your electronic catalog. Please select what kind of account you have!");
		title.setFont(new Font("Verdana", Font.BOLD, 15));
		titlePanel = new JPanel();
		titlePanel.add(title);
		
		studentBox = new JCheckBox("Student");
		parentBox = new JCheckBox("Parent");
		assistantBox = new JCheckBox("Assistant");
		teacherBox = new JCheckBox("Teacher");
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(studentBox);
		buttonGroup.add(parentBox);
		buttonGroup.add(assistantBox);
		buttonGroup.add(teacherBox);
		
		checkPanel = new JPanel(new GridLayout(4, 1));
		checkPanel.add(studentBox);
		checkPanel.add(parentBox);
		checkPanel.add(assistantBox);
		checkPanel.add(teacherBox);
		
		selectButton = new JButton("Select");
		selectButton.setEnabled(false);
		selectButton.addActionListener(this);
		
		coursesButton = new JButton("Courses");
		coursesButton.addActionListener(this);
		
		studentBox.addActionListener(this);
		parentBox.addActionListener(this);
		assistantBox.addActionListener(this);
		teacherBox.addActionListener(this);
		
		selectPanel = new JPanel(new GridLayout(1, 2));
		selectPanel.add(selectButton);
		selectPanel.add(coursesButton);
		
		add(titlePanel, BorderLayout.NORTH);
		add(checkPanel, BorderLayout.CENTER);
		add(selectPanel, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == selectButton) {
				selectButton.setEnabled(false);
				
				if (assistantBox.isSelected()) {
					this.dispose();
					assistantPage = new AssistantLoginPage("Assistant Login Page");
				} else if (studentBox.isSelected()) {
					this.dispose();
					studentPage = new StudentLoginPage("Student Login Page");
				} else if (parentBox.isSelected()) {
					this.dispose();
					parentPage = new ParentLoginPage("Parent Login Page");
				} else if (teacherBox.isSelected()) {
					this.dispose();
					teacherPage = new TeacherLoginPage("Teacher Login Page");
				}
			} else if (e.getSource() == coursesButton) {
				this.dispose();
				nextPage = new GeneralInformationsPage("Courses Informations");
			}

		} else {
			selectButton.setEnabled(true);
		}
	}
}