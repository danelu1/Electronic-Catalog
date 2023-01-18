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

class TeacherMainPage extends JFrame implements ActionListener, ListSelectionListener {
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
	JTextArea validationArea;
	
	JScrollPane leftScroll;
	JScrollPane middleScroll;
	JScrollPane rightScroll;
	
	Catalog catalog;
	Vector<Course> courses; 
	Vector<String> informations;
	SelectionPage page;
	static ArrayList<Student> allStudents;
	
	public TeacherMainPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1200, 800));
		setResizable(false);
		
		catalog = Catalog.getInstance();
		
		try {
			catalog.coursesParseJSON("./test/courses.json");
		} catch (java.text.ParseException | ParseException e) {
			e.printStackTrace();
		}
		
		User user = TeacherLoginPage.user;
		Teacher teacher = (Teacher) UserFactory.getUser("Teacher", user.getFirstName(), user.getLastName());
		
		courses = new Vector<>();
		
		for (Course course : catalog.courses) {
			if (course.getCourseTeacher().equals(teacher)) {
				courses.add(course);
			}
		}
		
		myCourses = new JList<>(courses);
		
		accountLabel = new JLabel("------------------------------ " + teacher.getFirstName() + " " + teacher.getLastName() + " Account ------------------------------");
		accountLabel.setFont(new Font("Times New Roman", Font.ITALIC, 30));
		
		welcomeLabel = new JLabel("Welcome back " + teacher.getFirstName() + " " + teacher.getLastName());
		welcomeLabel.setForeground(Color.blue);
		
		myPhotoLabel = new JLabel("My photo");
		
		photoLabel = new JLabel();
		ImageIcon imageIcon = new ImageIcon("./imagini/teachers/" + user.getIcon());
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
		validationArea = new JTextArea();
		
		middleScroll = new JScrollPane(validationArea);
		
		rightScroll = new JScrollPane(informationsArea);
		
		centerPanel = new JPanel(new GridLayout(1, 3));
		centerPanel.add(panel);
		centerPanel.add(middleScroll);
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
		validateButton.addActionListener(this);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == logoutButton) {
				this.dispose();
				page = new SelectionPage("Select");
			} else if (e.getSource() == validateButton) {
				User user = TeacherLoginPage.user;
				Teacher teacher = (Teacher) UserFactory.getUser("Teacher", user.getFirstName(), user.getLastName());
				
				ScoreVisitor visitor = new ScoreVisitor();
				
				teacher.accept(visitor);
				
				String grades = visitor.printGradesTeacher(teacher);
				
				validationArea.setText(grades);
				validationArea.setEditable(false);
				validationArea.setForeground(Color.green);
				validateButton.setEnabled(false);
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
			courseAssistants = new JLabel("\t- Course assistants:\n");
			
			Iterator<Assistant> it = course.getCourseAssistants().iterator();
			
			String assistants = "\t\t";
			
			while (it.hasNext()) {
				Assistant assistant = it.next();
				assistants += assistant.toString() + "\n";
				assistants += "\t\t";
			}
			
			assistants += "\n";
			
			String students = "\t- Course students:\n";
			
			ArrayList<Student> studentsArray = course.getAllStudents();
			allStudents = new ArrayList<>();
			
			for (Student s : studentsArray) {
				students += "\t\t" + s + "\n";
				allStudents.add(s);
			}
			
			String info = courseInformations.getText() + courseCredits.getText() + courseAssistants.getText() 
			+ assistants + students;
		
			informationsArea.setText(info);
			informationsArea.setEditable(false);
		
			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}