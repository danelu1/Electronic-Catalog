import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.simple.parser.ParseException;

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
	Student student;
	Vector<String> icons;
	
	public StudentMainPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		setResizable(false);
		
		catalog = Catalog.getInstance();
		try {
			catalog.coursesParseJSON("./test/courses.json");
		} catch (java.text.ParseException | ParseException e) {
			e.printStackTrace();
		}
		
		defList = new Vector<>();
		User user = StudentLoginPage.user;
		Student s = (Student) UserFactory.getUser("Student", user.getFirstName(), user.getLastName());
		
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
		ImageIcon imageIcon = new ImageIcon("./imagini/students/" + user.getIcon());
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
				try {
					Thread.sleep(1500);
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
			Student s = (Student) UserFactory.getUser("Student", user.getFirstName(), user.getLastName());
			
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
			
			catalog.addGradesToCatalog();
			
			Grade grade = course.getGrade(s);
			myGrade += grade.printPartialScore() + "\n\t\t" + grade.printExamScore() + "\n\t\t" + grade.printTotalScore() + "\n";
			
			String str = "\t- ";
			
			String state = "\t- State: ";
			
			if (course instanceof FullCourse) {
				if (course.getGraduatedStudents().contains(s)) {
					state += "graduated\n";
				} else {
					state += "failed\n";
				}
			} else if (course instanceof PartialCourse) {
				if (course.getGraduatedStudents().contains(s)) {
					state += "graduated\n";
				} else {
					state += "failed\n";
				}
			}
			
			String info = courseInformations + courseCredits + courseTeacher + assistants + myGroup + myAssistant + myGrade + state;
			
			if (course.getBestStudent().equals(s)) {
				str += "Congratulations! You are the courses's best student!!!";
				info += str;
			}
			
			informationsArea.setText(info);
			informationsArea.setEditable(false);		

			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}