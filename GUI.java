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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
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
		setResizable(false);
		
		title = new JLabel("-------------------------------------Please enter your login details-------------------------------------");
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
				this.dispose();
				previousPage = new SelectionPage("Selection");
			
			} else if (e.getSource() == loginButton) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					user = catalog.checkUserNamePassword("Student", this.userText.getText(), this.passwordText.getText());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				
				if (user != null) {
					this.dispose();
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
	static User user;
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
		setResizable(false);
		
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
				this.dispose();
				previousPage = new SelectionPage("Selection");
			
			} else if (e.getSource() == loginButton) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					user = catalog.checkUserNamePassword("Assistant", this.userText.getText(), this.passwordText.getText());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				
				if (user != null) {
					this.dispose();
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
		setResizable(false);
		
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
				this.dispose();
				previousPage = new SelectionPage("Selection");
			
			} else if (e.getSource() == loginButton) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					user = catalog.checkUserNamePassword("Teacher", this.userText.getText(), this.passwordText.getText());
				} catch (IOException | ParseException e1) {
					e1.printStackTrace();
				}
				
				if (user != null) {
					this.dispose();
					teacherPage = new TeacherMainPage("Teacher Page");
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
	
	JTextArea informationsArea;
	
	JLabel accountLabel;
	JLabel welcomeLabel;
	JLabel coursesLabel;
	JLabel photoLabel;
	JLabel myPhotoLabel;
	
	Vector<Course> defList;
	
	JPanel centerPanel;
	JPanel accountPanel;
	JPanel photoPanel;
	JPanel photoPanel_aux;
	JPanel panel;
	JPanel myPhotoPanel;
	JPanel myPhotoPanel_aux;
	JPanel logoutPanel;
	
	JScrollPane leftScrollPane;
	JScrollPane rightScrollPane;
	JButton logoutButton;
	Catalog catalog;
	SelectionPage page;
	JInternalFrame internalFrame;
	Student student;
	Vector<String> icons;
	UserFactory factory = new UserFactory();
	
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
		
		defList = new Vector<>();
		User user = StudentLoginPage.user;
		Student s = (Student) factory.getUser("Student", user.getFirstName(), user.getLastName());
		
		ArrayList<Student> students = new ArrayList<>();
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			students = catalog.courses.get(i).getAllStudents();
			for (int j = 0; j < students.size(); j++) {
				if (students.get(j).equals(s)) {
					defList.add(catalog.courses.get(i));
				}
			}
		}
		
		courses = new JList<>(defList);
		
		accountLabel = new JLabel("--------------------- " + user.getFirstName() + " " + user.getLastName() + " Account ---------------------");
		accountLabel.setFont(new Font("Verdana", Font.BOLD, 25));
		
		welcomeLabel = new JLabel("Welcome back " + user.getFirstName() + " " + user.getLastName());
		welcomeLabel.setForeground(Color.green);
		welcomeLabel.setFont(new Font("Times New Roman", Font.ITALIC, 10));
		
		myPhotoLabel = new JLabel("My photo");
		
		photoLabel = new JLabel();
		ImageIcon imageIcon = new ImageIcon("./imagini/" + user.getIcon());
		Image image = imageIcon.getImage();
		Image newImage = image.getScaledInstance(100, 60, Image.SCALE_FAST);
		ImageIcon newIcon = new ImageIcon(newImage);
		photoLabel.setIcon(newIcon);
		
		photoPanel_aux = new JPanel(new GridLayout(2, 1));
		
		myPhotoPanel = new JPanel();
		myPhotoPanel_aux = new JPanel();
		
		myPhotoPanel.add(myPhotoLabel);
		myPhotoPanel_aux.add(photoLabel);
		
		photoPanel_aux.add(myPhotoPanel);
		photoPanel_aux.add(myPhotoPanel_aux);
		
		photoPanel = new JPanel(new GridLayout(1, 2));
		photoPanel.add(welcomeLabel);
		photoPanel.add(photoPanel_aux);
		
		coursesLabel = new JLabel("The courses I'm assigned to:");
		
		leftScrollPane = new JScrollPane(courses);
		
		panel = new JPanel(new GridLayout(2, 1));
		panel.add(coursesLabel);
		panel.add(leftScrollPane);
		
		informationsArea = new JTextArea();
		
		rightScrollPane = new JScrollPane(informationsArea);
		
		logoutButton = new JButton("Log out");
		logoutButton.setBounds(100, 100, 100, 100);
		logoutButton.addActionListener(this);
		logoutPanel = new JPanel();
		logoutPanel.add(logoutButton);
		
		accountPanel = new JPanel(new GridLayout(2, 1));
		accountPanel.add(accountLabel);
		accountPanel.add(photoPanel);
		
		centerPanel = new JPanel(new GridLayout(1, 2));
		centerPanel.add(panel);
		centerPanel.add(rightScrollPane);
		
		add(accountPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(logoutPanel, BorderLayout.SOUTH);
		
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
				this.dispose();
				page = new SelectionPage("Select");
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (courses.isSelectionEmpty()) {
			return;
		} else {
			Object value = courses.getSelectedValue();
			String courseInformations = "Course informations:\n";
			Course course = (Course) value; 
			String courseCredits = "\t- Course credits: " + course.getCourseCredits() + "\n";
			String courseTeacher = "\t- Course teacher: " + course.getCourseTeacher().toString() + "\n";
			
			Iterator<Assistant> it = course.getCourseAssistants().iterator();
			String assistants = "\t- Course assistants:\n";
			
			while (it.hasNext()) {
				assistants += "\t\t" + it.next() + "\n";
			}
			
			String myAssistant = "\t- My assistant: ";
			String myGroup = "\t- My group: ";
			String myGrade = "\t- My grade:\n\t\t";
			
			User user = StudentLoginPage.user;
			Student s = (Student) factory.getUser("Student", user.getFirstName(), user.getLastName());
			
			Map<String, Group> map = course.getGroup();
			
			for (Map.Entry<String, Group> mp : map.entrySet()) {
				Group group = mp.getValue();
					
				if (group.contains(s)) {
					myGroup += group.getID();
					myAssistant += group.getAssistant().getFirstName() + " " + group.getAssistant().getLastName();
					break;
				}
			}
			
			myGroup += "\n";
			myAssistant += "\n";
			
			HashMap<Student, Grade> gradesMap = course.getAllStudentGrades();
			
			if (gradesMap.containsKey(user)) {
				Grade grade = course.getGrade(s);
				myGrade += grade.printPartialScore() + "\n\t\t" + grade.printExamScore() + "\n\t\t" + grade.printTotalScore() + "\n";
			}
			
			String str = "\t- ";
			
			if (course.getBestStudent().equals(s)) {
				str += "Congratulations! You are the courses's best student!!!";
			}
			
			String info = courseInformations + courseCredits + courseTeacher + assistants + myGroup + myAssistant + myGrade + str;
			
			informationsArea.setText(info);
			informationsArea.setEditable(false);		

			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}

class AssistantMainPage extends JFrame implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("rawtypes")
	JList myCourses;
	
	JLabel accountLabel;
	JLabel welcomeLabel;
	JLabel myPhotoLabel;
	JLabel photoLabel;
	JLabel myCoursesLabel;
	
	JLabel courseInformations;
	JLabel courseTeacher;
	JLabel courseCredits;
	JLabel courseAssistants;
	JLabel myGroup;
	JLabel myStudents;
	
	JPanel myPhotoPanel;
	JPanel welcomePanel;
	JPanel northPanel;
	JPanel centerPanel;
	JPanel southPanel;
	JPanel panel;
	
	JButton logoutButton;
	JButton validateButton;
	
	JTextArea informationsArea;
	
	JScrollPane leftScroll;
	JScrollPane rightScroll;
	
	Catalog catalog;
	Vector<Course> courses; 
	Vector<String> informations;
	SelectionPage page;
	UserFactory factory = new UserFactory();

	public AssistantMainPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(100, 101 ,102));
		
		catalog = Catalog.getInstance();
		
		try {
			catalog.coursesParseJSON("./test/courses.json");
		} catch (java.text.ParseException | ParseException e) {
			e.printStackTrace();
		}
		
		User user = AssistantLoginPage.user;
		Assistant assistant = (Assistant) factory.getUser("Assistant", user.getFirstName(), user.getLastName());
		
		courses = new Vector<>();
		
		for (Course course : catalog.courses) {
			Map<String, Group> map = course.getGroup();
			
			for (Map.Entry<String, Group> mp : map.entrySet()) {
				Group group = mp.getValue();
				
				if (group.getAssistant().equals(assistant)) {
					courses.add(course);
				}
			}
		}
		
		myCourses = new JList<>(courses);
		
		accountLabel = new JLabel("------------------------------ " + assistant.getFirstName() + " " + assistant.getLastName() + " Account ------------------------------");
		accountLabel.setFont(new Font("Times New Roman", Font.ITALIC, 30));
		
		welcomeLabel = new JLabel("Welcome back " + assistant.getFirstName() + " " + assistant.getLastName());
		welcomeLabel.setForeground(Color.blue);
		
		myPhotoLabel = new JLabel("My photo");
		
		photoLabel = new JLabel();
		ImageIcon imageIcon = new ImageIcon("./imagini/" + user.getIcon());
		Image image = imageIcon.getImage();
		Image newImage = image.getScaledInstance(100, 60, Image.SCALE_FAST);
		ImageIcon newIcon = new ImageIcon(newImage);
		photoLabel.setIcon(newIcon);
		
		myPhotoPanel = new JPanel(new GridLayout(2, 1));
		myPhotoPanel.add(myPhotoLabel);
		myPhotoPanel.add(photoLabel);
		
		welcomePanel = new JPanel(new GridLayout(1, 2));
		welcomePanel.add(welcomeLabel);
		welcomePanel.add(myPhotoPanel);
		
		myCoursesLabel = new JLabel("My courses:");
		
		northPanel = new JPanel(new GridLayout(2, 1));
		northPanel.add(accountLabel);
		northPanel.add(welcomePanel);
		
		leftScroll = new JScrollPane(myCourses);
		leftScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		panel = new JPanel(new GridLayout(2, 1));
		panel.add(myCoursesLabel);
		panel.add(leftScroll);
		
		informationsArea = new JTextArea();
		
		rightScroll = new JScrollPane(informationsArea);
		
		centerPanel = new JPanel(new GridLayout(1, 2));
		centerPanel.add(panel);
		centerPanel.add(rightScroll);
		
		logoutButton = new JButton("Previous");
		validateButton = new JButton("Validate");
		southPanel = new JPanel(new GridLayout(1, 2));
		southPanel.add(logoutButton);
		southPanel.add(validateButton);
		
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		myCourses.addListSelectionListener(this);
		logoutButton.addActionListener(this);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == logoutButton) {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				this.dispose();
				page = new SelectionPage("Select");
			} else if (e.getSource() == validateButton) {
//				return;
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (myCourses.isSelectionEmpty()) {
			return;
		} else {
			Object value = myCourses.getSelectedValue();
			Course course = (Course) value;
			
			courseInformations = new JLabel("Course informations:\n");
			courseCredits = new JLabel("\t- Course credits: " + course.getCourseCredits() + "\n");
			courseTeacher = new JLabel("\t- Course Teacher: " + course.getCourseTeacher().getFirstName() + " " + course.getCourseTeacher().getLastName() + "\n");
			courseAssistants = new JLabel("\t- Course assistants:\n");
			
			Iterator<Assistant> it = course.getCourseAssistants().iterator();
			
			String assistants = "\t\t";
			
			while (it.hasNext()) {
				Assistant assistant = it.next();
				assistants += assistant.toString() + "\n";
				assistants += "\t\t";
			}
			
			assistants += "\n";
			
			myGroup = new JLabel("\t- My group:\n");
			
			Map<String, Group> map = course.getGroup();
			String id = "\t\tID: ";
			String students = "\t\tStudents:\n";
			
			User user = AssistantLoginPage.user;
			Assistant assistant = new Assistant(user.getFirstName(), user.getLastName());
			
			for (Map.Entry<String, Group> mp : map.entrySet()) {
				Group group = mp.getValue();
				
				if (group.getAssistant().equals(assistant)) {
					id += mp.getKey() + "\n";
					Iterator<Student> studentsIterator = group.iterator();
					
					while (studentsIterator.hasNext()) {
						Student s = studentsIterator.next();
						students += "\t\t" + s.toString() + "\n";
						Grade grade = course.getGrade(s);
						students += "\t\t\t" + grade.printPartialScore() + "\n";
					}
				}
			}
			
			String info = courseInformations.getText() + courseCredits.getText() + courseTeacher.getText() + courseAssistants.getText() 
				+ assistants + myGroup.getText() + 	id + students;
			
			informationsArea.setText(info);
			informationsArea.setEditable(false);
			
			SwingUtilities.updateComponentTreeUI(this);
		}
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
				this.dispose();
				parentLogin = new ParentLoginPage("Parent Login Page");
			} else if (e.getSource() == previousPageButton) {
				this.dispose();
				previousPage = new SelectionPage("Select");
			}
		}
	}
}

class ModifiableInformationsPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	JList allCourses;
	
	JButton addAssistant;
	JButton addStudent;
	JButton addGroup;
	JButton addGrade;
	
	JTextArea assistant;
	JTextArea student;
	JTextArea group;
	JTextArea grade;
	JTextArea informationsArea;
	
	JScrollPane assistantPane;
	JScrollPane studentPane;
	JScrollPane groupPane;
	JScrollPane gradePane;
	JScrollPane textPane;
	
	JPanel northPanel;
	JPanel centerPanel;
	JPanel actionPanel;
	JPanel textPanel;
	
	Vector<String> courses;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ModifiableInformationsPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		
		Catalog catalog = Catalog.getInstance();
		
		try {
			catalog.coursesParseJSON("./test/courses.json");
		} catch (java.text.ParseException | ParseException e) {
			e.printStackTrace();
		}
		
		courses = new Vector<>();
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			courses.add(catalog.courses.get(i).toString());
		}
		
		allCourses = new JList(courses);
		
		northPanel = new JPanel();
		northPanel.add(allCourses);
		
		addAssistant = new JButton("Add Assistant");
		addStudent = new JButton("Add Student");
		addGroup = new JButton("Add Group");
		addGrade = new JButton("Add Grade");
		
		assistant = new JTextArea();
		student = new JTextArea();
		group = new JTextArea();
		grade = new JTextArea();
		
		assistantPane = new JScrollPane(assistant);
		studentPane = new JScrollPane(student);
		groupPane = new JScrollPane(group);
		gradePane = new JScrollPane(grade);
		
		actionPanel = new JPanel(new GridLayout(4, 2));
		actionPanel.add(addAssistant);
		actionPanel.add(assistantPane);
		actionPanel.add(addStudent);
		actionPanel.add(studentPane);
		actionPanel.add(addGroup);
		actionPanel.add(groupPane);
		actionPanel.add(addGrade);
		actionPanel.add(gradePane);
		
		informationsArea = new JTextArea();
		
		textPane = new JScrollPane(informationsArea);
		
		textPanel = new JPanel();
		textPanel.add(textPane);
		
		centerPanel = new JPanel(new GridLayout(1, 2));
		centerPanel.add(actionPanel);
		centerPanel.add(textPanel);
		
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}