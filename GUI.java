import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.simple.parser.ParseException;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

class SelectionPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JLabel title;
	JCheckBox studentBox;
	JCheckBox parentBox;
	JCheckBox assistantBox;
	JCheckBox teacherBox;
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
		getContentPane().setBackground(new Color(200, 123, 198));
		setLayout(new BorderLayout());
		
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
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == selectButton) {
				selectButton.setEnabled(false);
				
				if (assistantBox.isSelected()) {
					assistantPage = new AssistantLoginPage("Assistant Login Page");
				} else if (studentBox.isSelected()) {
					studentPage = new StudentLoginPage("Student Login Page");
				} else if (parentBox.isSelected()) {
					parentPage = new ParentLoginPage("Parent Login Page");
				} else if (teacherBox.isSelected())  {
					teacherPage = new TeacherLoginPage("Teacher Login Page");
				}
			} else if (e.getSource() == coursesButton) {
				nextPage = new GeneralInformationsPage("Courses Informations");
			}

		} else {
			selectButton.setEnabled(true);
		}
	}
}

class ParentLoginPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JLabel title;
	JLabel userLabel;
	JLabel passwordLabel;
	JTextField userText;
	JPasswordField passwordText;
	JButton loginButton;
	JButton goBackButton;
	User user;
	JPanel userPanel;
	JPanel loginPanel;
	JPanel passwordPanel;
	JPanel goBackPanel;
	JPanel centerPanel;
	JInternalFrame internalFrame;
	SelectionPage previousPage;
	ParentMainPage parentPage;
	
	public ParentLoginPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		setLayout(new BorderLayout());
		
		title = new JLabel("------------------------------------ Please enter your login details ------------------------------------");
		title.setFont(new Font("Verdana", Font.BOLD, 15));
		
		userLabel = new JLabel("Parent User Name: ");
		passwordLabel = new JLabel("Parent User password: ");
		
		userLabel.setForeground(Color.green);
		passwordLabel.setForeground(Color.green);
		
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
		
		userPanel.setBackground(Color.red);
		
		loginPanel = new JPanel();
		loginPanel.add(userPanel);
		
		
		passwordPanel = new JPanel();
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordText);
		passwordPanel.setBackground(Color.red);
		
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
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == goBackButton) {
				internalFrame = new JInternalFrame("Go back");
				internalFrame.setMinimumSize(new Dimension(500, 500));
				internalFrame.getContentPane().setBackground(Color.blue);
				internalFrame.add(new JLabel("Redirecting you..."));
				internalFrame.pack();
				internalFrame.setVisible(true);
				add(internalFrame, BorderLayout.SOUTH);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				previousPage = new SelectionPage("Selection");
			
			} else if (e.getSource() == loginButton) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					user = catalog.checkUserNamePassword("Parent", this.userText.getText(), this.passwordText.getText());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				
				if (user != null) {
					parentPage = new ParentMainPage("Parent Page");
				} else { 
					this.title.setText("---------------------------------------- Invalid email or password! ----------------------------------------");
				}
			}
		}
	}
}

class StudentLoginPage extends JFrame implements ActionListener {
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
	JInternalFrame internalFrame;
	SelectionPage previousPage;
	StudentMainPage studentPage;
	
	public StudentLoginPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(251, 255, 175));
		setLayout(new BorderLayout());
		
		title = new JLabel("------------------------------------ Please enter your login details ------------------------------------");
		title.setFont(new Font("Verdana", Font.BOLD, 15));
		
		userLabel = new JLabel("Student User Name: ");
		passwordLabel = new JLabel("Student User password: ");
		
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
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == goBackButton) {
				internalFrame = new JInternalFrame("Go back");
				internalFrame.setMinimumSize(new Dimension(500, 500));
				internalFrame.getContentPane().setBackground(Color.blue);
				internalFrame.add(new JLabel("Redirecting you..."));
				internalFrame.pack();
				internalFrame.setVisible(true);
				add(internalFrame, BorderLayout.SOUTH);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				previousPage = new SelectionPage("Selection");
			
			} else if (e.getSource() == loginButton) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					user = catalog.checkUserNamePassword("Student", this.userText.getText(), this.passwordText.getText());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				
				if (user != null) {
					studentPage = new StudentMainPage("Student Page");
				} else { 
					this.title.setText("---------------------------------------- Invalid email or password! ----------------------------------------");
				}
			}
		}
	}
}

class AssistantLoginPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JLabel title;
	JLabel userLabel;
	JLabel passwordLabel;
	JTextField userText;
	JPasswordField passwordText;
	JButton loginButton;
	JButton goBackButton;
	User user;
	JPanel userPanel;
	JPanel loginPanel;
	JPanel passwordPanel;
	JPanel goBackPanel;
	JPanel centerPanel;
	JInternalFrame internalFrame;
	SelectionPage previousPage;
	AssistantMainPage assistantPage;
	
	public AssistantLoginPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(251, 255, 175));
		setLayout(new BorderLayout());
		
		title = new JLabel("------------------------------------ Please enter your login details ------------------------------------");
		title.setFont(new Font("Verdana", Font.BOLD, 15));
		
		userLabel = new JLabel("Assistant User Name: ");
		passwordLabel = new JLabel("Assistant User password: ");
		
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
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == goBackButton) {
				internalFrame = new JInternalFrame("Go back");
				internalFrame.setMinimumSize(new Dimension(500, 500));
				internalFrame.getContentPane().setBackground(Color.blue);
				internalFrame.add(new JLabel("Redirecting you..."));
				internalFrame.pack();
				internalFrame.setVisible(true);
				add(internalFrame, BorderLayout.SOUTH);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				previousPage = new SelectionPage("Selection");
			
			} else if (e.getSource() == loginButton) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					user = catalog.checkUserNamePassword("Assistant", this.userText.getText(), this.passwordText.getText());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				
				if (user != null) {
					assistantPage = new AssistantMainPage("Assistant Page");
				} else {
					this.title.setText("---------------------------------------- Invalid email or password! ----------------------------------------");
				}
			}
		}
	}
}

class TeacherLoginPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JLabel title;
	JLabel userLabel;
	JLabel passwordLabel;
	JTextField userText;
	JPasswordField passwordText;
	JButton loginButton;
	JButton goBackButton;
	User user;
	JPanel userPanel;
	JPanel loginPanel;
	JPanel passwordPanel;
	JPanel goBackPanel;
	JPanel centerPanel;
	JInternalFrame internalFrame;
	SelectionPage previousPage;
	TeacherMainPage teacherPage;
	
	public TeacherLoginPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(251, 255, 175));
		setLayout(new BorderLayout());
		
		title = new JLabel("------------------------------------ Please enter your login details ------------------------------------");
		title.setFont(new Font("Verdana", Font.BOLD, 15));
		
		userLabel = new JLabel("Teacher User Name: ");
		passwordLabel = new JLabel("Teacher User password: ");
		
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
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == goBackButton) {
				internalFrame = new JInternalFrame("Go back");
				internalFrame.setMinimumSize(new Dimension(500, 500));
				internalFrame.getContentPane().setBackground(Color.blue);
				internalFrame.add(new JLabel("Redirecting you..."));
				internalFrame.pack();
				internalFrame.setVisible(true);
				add(internalFrame, BorderLayout.SOUTH);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				previousPage = new SelectionPage("Selection");
			
			} else if (e.getSource() == loginButton) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					user = catalog.checkUserNamePassword("Teacher", this.userText.getText(), this.passwordText.getText());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				
				if (user != null) {
					teacherPage = new TeacherMainPage("Assistant Page");
				} else {
					this.title.setText("---------------------------------------- Invalid email or password! ----------------------------------------");
				}
			}
		}
	}
}

class ParentMainPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public ParentMainPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(144, 169, 196));
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

class StudentMainPage extends JFrame implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	JList courses;
	@SuppressWarnings("rawtypes")
	JList labels;
	
	JLabel accountLabel;
	JLabel welcomeLabel;
	JLabel coursesLabel;
	JLabel photoLabel;
	JLabel myPhotoLabel;
	
	JLabel courseInformation;
	JLabel creditsLabel;
	JLabel courseTeacher;
	JLabel assistants;
	JLabel groupLabel;
	JLabel personalAssistant;
	JLabel personalGrade;
	JLabel stateLabel;
	
	DefaultListModel<Course> defList;
	
	JPanel centerPanel;
	JPanel accountPanel;
	JPanel photoPanel;
	JPanel photoPanel_aux;
	JPanel listPanel;
	JPanel leftPanel;
	JPanel rightPanel;
	
	JScrollPane scrollPane;
	JSplitPane splitPane;
	JButton logoutButton;
	Catalog catalog;
	SelectionPage page;
	JInternalFrame internalFrame;
	Student student;
	Vector<String> icons;
	
	public StudentMainPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(100, 120 ,140));
		setLayout(new BorderLayout());
		
		catalog = Catalog.getInstance();
		try {
			catalog.coursesParseJSON("./test/courses.json");
		} catch (java.text.ParseException | ParseException e) {
			e.printStackTrace();
		}
		
		try {
			catalog.loginParseJSON("Student");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		defList = new DefaultListModel<>();
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			defList.add(i, catalog.courses.get(i));
		}
		
		courses = new JList<>(defList);
		
		User user = StudentLoginPage.user;
		
		accountLabel = new JLabel("--------------------- " + user.getFirstName() + " " + user.getLastName() + " Account ---------------------");
		accountLabel.setFont(new Font("Verdana", Font.BOLD, 25));
		
		welcomeLabel = new JLabel("Welcome back " + user.getFirstName() + " " + user.getLastName());
		welcomeLabel.setForeground(Color.green);
		welcomeLabel.setFont(new Font("Times New Roman", Font.ITALIC, 10));
		
		myPhotoLabel = new JLabel("My photo");
		
		photoLabel = new JLabel();
		ImageIcon imageIcon = new ImageIcon("./imagini/" + user.getIcon());
		Image image = imageIcon.getImage();
		Image newImage = image.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newImage);
		photoLabel.setIcon(newIcon);
		
		photoPanel_aux = new JPanel(new GridLayout(2, 1));
		photoPanel_aux.add(myPhotoLabel);
		photoPanel_aux.add(photoLabel);
		
		photoPanel = new JPanel(new GridLayout(1, 2));
		photoPanel.add(welcomeLabel);
		photoPanel.add(photoPanel_aux);
		
		coursesLabel = new JLabel("The courses I'm assigned to:");
		
		logoutButton = new JButton("Log out");
		logoutButton.setBounds(100, 100, 100, 100);
		logoutButton.addActionListener(this);
		
		accountPanel = new JPanel(new GridLayout(2, 1));
		accountPanel.add(accountLabel);
		accountPanel.add(photoPanel);
		
		centerPanel = new JPanel(new GridLayout(2, 1));
		centerPanel.add(coursesLabel);
		centerPanel.add(courses);
		
		leftPanel = new JPanel(new GridLayout(3, 1));
		leftPanel.add(accountPanel, BorderLayout.NORTH);
		leftPanel.add(centerPanel, BorderLayout.CENTER);
		leftPanel.add(logoutButton, BorderLayout.SOUTH);
		
		add(leftPanel);
		
		courses.addListSelectionListener(this);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == logoutButton) {
				internalFrame = new JInternalFrame("Go back");
				internalFrame.setMinimumSize(new Dimension(500, 500));
				internalFrame.getContentPane().setBackground(Color.blue);
				internalFrame.add(new JLabel("Redirecting you..."));
				internalFrame.pack();
				internalFrame.setVisible(true);
				add(internalFrame, BorderLayout.SOUTH);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				page = new SelectionPage("Select");
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (courses.isSelectionEmpty()) {
			return;
		} else {
			Object value = courses.getSelectedValue();
			coursesLabel = new JLabel("Course informations:");
			Course course = (Course) value; 
			courseTeacher = new JLabel("- Course Teacher: " + course.getCourseTeacher().getFirstName() + " " + course.getCourseTeacher().getLastName());
			
			DefaultListModel<Assistant> assistantsList = new DefaultListModel<>();
			Iterator<Assistant> it = course.getCourseAssistants().iterator();
			
			int i = 0;
			while (it.hasNext()) {
				assistantsList.add(i, it.next());
				i++;
			}
			
			assistants = new JLabel("- Course assistants:");
			
			Vector<String> labels_aux = new Vector<>();
			
			creditsLabel = new JLabel("- Course credits: " + course.getCourseCredits());
			
			labels_aux.add(coursesLabel.getText());
			labels_aux.add(creditsLabel.getText());
			labels_aux.add(courseTeacher.getText());
			labels_aux.add(assistants.getText());
			
			for (int j = 0; j < assistantsList.size(); j++) {
				labels_aux.add(assistantsList.get(j).toString());
			}
			
			catalog = Catalog.getInstance();
			
			User user = StudentLoginPage.user;
			
			student = new Student(user.getFirstName(), user.getLastName());
			Assistant assistant = null;
			Group group = null;
			
			Map<String, Group> map = course.getGroup();
			ArrayList<Group> groups = new ArrayList<>();
			
			for (Map.Entry<String, Group> mp : map.entrySet()) {
				groups.add(mp.getValue());
				if (mp.getValue().contains(student)) {
					assistant = mp.getValue().getAssistant();
					group = mp.getValue();
					break;
				}
			}
			
			ArrayList<Student> students = new ArrayList<>();
			
			for (int j = 0; j < groups.size(); j++) {
				Iterator<Student> it_aux = groups.get(j).iterator();
				while (it_aux.hasNext()) {
					students.add(it_aux.next());
				}
			}
			
			int m = 0;
			for (int j = 0; j < students.size(); j++) {
				Student s1 = students.get(j);
				if (s1.getFirstName().equals(student.getFirstName()) && s1.getLastName().equals(student.getLastName())) {
					m = j;
				}
			}
			
			groupLabel = new JLabel("- Your group: " + group.getID());
			
			personalAssistant = new JLabel("- Your assistant: " + assistant.getFirstName() + " " + assistant.getLastName());
			
			Student s = course.getAllStudents().get(m);
			Grade grade = course.getGrade(s);
			
			personalGrade = new JLabel("- Your grade:");
			
			String state = "";
			
			if (course instanceof FullCourse) {
				if (course.getGraduatedStudents().contains(s)) {
					state += "graduated";
				} else {
					state += "failed";
				}
			} else if (course instanceof PartialCourse) {
				if (course.getGraduatedStudents().contains(s)) {
					state += "graduated";
				} else {
					state += "failed";
				}
			}
			
			stateLabel = new JLabel("- State: " + state);
			
			labels_aux.add(groupLabel.getText());
			labels_aux.add(personalAssistant.getText());
			labels_aux.add(personalGrade.getText());
			labels_aux.add(grade.printPartialScore());
			labels_aux.add(grade.printExamScore());
			labels_aux.add(grade.printTotalScore());
			labels_aux.add(stateLabel.getText());
			
			labels = new JList<>(labels_aux);
			
			rightPanel = new JPanel(new GridLayout(2, 1));
			rightPanel.add(labels);
			
			scrollPane = new JScrollPane(rightPanel);
			
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
			add(splitPane);
		}
	}
}

class AssistantMainPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public AssistantMainPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(100, 101 ,102));
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

class TeacherMainPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public TeacherMainPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(14, 140 ,240));
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

class GeneralInformationsPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JLabel titleLabel;
	JLabel noteLabel;
	
	JLabel courseName;
	JLabel courseTeacher;
	JLabel courseCredits;
	JLabel courseAssistants;
	JLabel courseGroups;
	JLabel groupId;
	JLabel groupStudents;
	JLabel groupAssistant;
	JLabel studentLabel;
	JLabel emptyLabel;
	
	JButton redirectButton;
	JButton previousPageButton;
	@SuppressWarnings("rawtypes")
	JList labels;
	
	JPanel northPanel;
	JPanel northSouthPanel;
	JPanel centerPanel;
	JPanel southPanel;
	
	JScrollPane scrollPane;
	
	ParentLoginPage parentLogin;
	SelectionPage previousPage;
	
	Vector<String> labels_texts;
	Catalog catalog;
	
	public GeneralInformationsPage(String messsage) {
		super(messsage);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(24, 195, 212));
		setLayout(new BorderLayout());
		
		catalog = Catalog.getInstance();
		
		try {
			catalog.coursesParseJSON("./test/courses.json");
		} catch (java.text.ParseException | ParseException e) {
			e.printStackTrace();
		}
		
		labels_texts = new Vector<>();
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			Course course = catalog.courses.get(i);
			
			courseName = new JLabel("Course name: " + course.getCourseName());
			labels_texts.add(courseName.getText());
			
			Teacher teacher = course.getCourseTeacher();
			courseTeacher = new JLabel("Course teacher: " + teacher.getFirstName() + " " + teacher.getLastName());
			labels_texts.add(courseTeacher.getText());
			
			courseCredits = new JLabel("Course credits: " + course.getCourseCredits());
			labels_texts.add(courseCredits.getText());
			
			courseAssistants = new JLabel("Course assistants:");
			labels_texts.add(courseAssistants.getText());
			
			Vector<Assistant> assistantsList = new Vector<>();
			Iterator<Assistant> it = course.getCourseAssistants().iterator();
			while (it.hasNext()) {
				assistantsList.add(it.next());
			}
			
			int k = 1;
			
			for (int j = 0; j < assistantsList.size(); j++) {
				labels_texts.add(k + ")" + assistantsList.get(j).toString());
				k++;
			}
			
			courseGroups = new JLabel("Course groups:");
			labels_texts.add(courseGroups.getText());
			
			Map<String, Group> map = course.getGroup();
			
			char c = 'a';
			
			for (Map.Entry<String, Group> mp : map.entrySet()) {
				Group group = mp.getValue();
				groupId = new JLabel(c + ")- Group ID: " + group.getID());
				labels_texts.add(groupId.getText());
				
				Assistant assistant = group.getAssistant();
				groupAssistant = new JLabel(c + ")- Group assistant: " + assistant.getFirstName() + " " + assistant.getLastName());
				labels_texts.add(groupAssistant.getText());
				
				groupStudents = new JLabel(c + ")- Group students:");
				labels_texts.add(groupStudents.getText());
				
				Iterator<Student> groupIterator = group.iterator();
				ArrayList<Student> studentsList = new ArrayList<>();
				
				int j = 1;
				
				while(groupIterator.hasNext()) {
					Student s = groupIterator.next();
					studentsList.add(s);
					studentLabel = new JLabel(j + ")" + s.toString());
					labels_texts.add(studentLabel.getText());
					j++;
				}
				
				c++;
			}
			
			emptyLabel = new JLabel("\n\n");
			labels_texts.add(emptyLabel.getText());
		}
		
		labels = new JList<>(labels_texts);
//		labels.setCellRenderer(new Renderer());
		
		centerPanel = new JPanel();
		centerPanel.add(labels);
		
		scrollPane = new JScrollPane(centerPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		titleLabel = new JLabel("--------------------------------------------------------Courses Informations--------------------------------------------------------");
		titleLabel.setFont(new Font("Dispence", Font.BOLD, 20));
		
		noteLabel = new JLabel("Note: Parents may view their child's grades in their personal 'Parent' accounts");
		noteLabel.setFont(new Font("Times New Roman", Font.ITALIC, 10));
		noteLabel.setForeground(Color.red);
		
		redirectButton = new JButton("Log in as a Parent");
		redirectButton.addActionListener(this);
		
		previousPageButton = new JButton("Previous");
		previousPageButton.addActionListener(this);
		
		northSouthPanel = new JPanel(new GridLayout(1, 2));
		northSouthPanel.add(noteLabel);
		northSouthPanel.add(redirectButton);
		
		northPanel = new JPanel(new GridLayout(2, 1));
		northPanel.add(titleLabel);
		northPanel.add(northSouthPanel);
		
		southPanel = new JPanel();
		southPanel.add(previousPageButton);
		
		add(northPanel, BorderLayout.NORTH);
		add(southPanel, BorderLayout.SOUTH);
		add(scrollPane, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == redirectButton) {
				parentLogin = new ParentLoginPage("Parent Login Page");
			} else if (e.getSource() == previousPageButton) {
				previousPage = new SelectionPage("Select");
			}
		}
	}
}