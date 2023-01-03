import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.simple.parser.ParseException;

class ModifiableInformationsPage extends JFrame implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	JList allCourses;
	
	JButton addAssistant;
	JButton addStudent;
	JButton addGroup;
	JButton addGrade;
	JButton logoutButton;
	
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
	JPanel southPanel;
	
	Vector<Course> courses;
	SelectionPage previousPage;
	
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
			courses.add(catalog.courses.get(i));
		}
		
		allCourses = new JList(courses);
		
		northPanel = new JPanel();
		northPanel.add(allCourses);
		
		addAssistant = new JButton("Add Assistant");
		addStudent = new JButton("Add Student");
		addGroup = new JButton("Add Group");
		addGrade = new JButton("Add Grade");
		logoutButton = new JButton("Previous");
		
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
		
		textPanel = new JPanel(new GridLayout(1, 2));
		textPanel.add(textPane);
		
		centerPanel = new JPanel(new GridLayout(1, 2));
		centerPanel.add(actionPanel);
		centerPanel.add(textPanel);
		
		southPanel = new JPanel();
		southPanel.add(logoutButton);
		
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		allCourses.addListSelectionListener(this);
		addStudent.addActionListener(this);
		addAssistant.addActionListener(this);
		addGroup.addActionListener(this);
		addGrade.addActionListener(this);
		logoutButton.addActionListener(this);
		
		pack();
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			if (e.getSource() == addStudent) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					catalog.coursesParseJSON("./test/courses.json");
				} catch (java.text.ParseException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				String text = student.getText();
				String[] informations = text.split(" ");
				String loginFile = informations[0];
				String file = informations[1];
				String courseName = informations[2];
				String groupId = informations[3];
				String info = "";
				
				Course course = null;
				
				for (int i = 0; i < catalog.courses.size(); i++) {
					if (catalog.courses.get(i).getCourseName().equals(courseName)) {
						course = catalog.courses.get(i);
						break;
					}
				}
				
				try {
					info += catalog.addStudent(loginFile, file, course, groupId);
				} catch (java.text.ParseException | ParseException | IOException e1) {
					e1.printStackTrace();
				}
				
				informationsArea.setText(info);
			} else if (e.getSource() == addAssistant) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					catalog.coursesParseJSON("./test/courses.json");
				} catch (java.text.ParseException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				String text = assistant.getText();
				String[] informations = text.split(" ");
				String file = informations[0];
				String courseName = informations[1];
				String info = "";
				
				Course course = null;
				
				for (int i = 0; i < catalog.courses.size(); i++) {
					if (catalog.courses.get(i).getCourseName().equals(courseName)) {
						course = catalog.courses.get(i);
						break;
					}
				}
				
				try {
					info += catalog.addAssistant(file, course);
				} catch (ParseException | IOException e1) {
					e1.printStackTrace();
				}
				
				informationsArea.setText(info);
			} else if (e.getSource() == addGroup) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					catalog.coursesParseJSON("./test/courses.json");
				} catch (java.text.ParseException e1) {
					e1.printStackTrace();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				String text = group.getText();
				String[] informations = text.split(" ");
				String file = informations[0];
				String courseName = informations[1];
				String info = "";
				
				Course course = null;
				
				for (int i = 0; i < catalog.courses.size(); i++) {
					if (catalog.courses.get(i).getCourseName().equals(courseName)) {
						course = catalog.courses.get(i);
						break;
					}
				}
				
				try {
					info += catalog.addGroup(file, course);
				} catch (ParseException | IOException e1) {
					e1.printStackTrace();
				}
				
				informationsArea.setText(info);
			} else if (e.getSource() == addGrade) {
				Catalog catalog = Catalog.getInstance();
				
				try {
					catalog.gradesParseJSON("./test/grades.json");
				} catch (IOException | ParseException e2) {
					e2.printStackTrace();
				}
				
				String text = grade.getText();
				String[] informations = text.split(" ");
				String file = informations[0];
				String courseName = informations[1];
				String info = "";
				
				Course course = null;
				
				for (int i = 0; i < catalog.courses.size(); i++) {
					if (catalog.courses.get(i).getCourseName().equals(courseName)) {
						course = catalog.courses.get(i);
						break;
					}
				}
				
				try {
					info += catalog.addGrade(file, course);
				} catch (ParseException | IOException e1) {
					e1.printStackTrace();
				}
				
				informationsArea.setText(info);
			} else if (e.getSource() == logoutButton) {
				this.dispose();
				previousPage = new SelectionPage("Select");
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (allCourses.isSelectionEmpty()) {
			return;
		} else {
			Object value = allCourses.getSelectedValue();
			Course course = (Course) value;
			
			String courseInformations = "Course informations:\n";
			String courseTeacher = "\t- Course Teacher: " + course.getCourseTeacher() + "\n";
			String courseCredits = "\t- Course credits: " + course.getCourseCredits() + "\n";
			String courseAssistants = "\t- Course assistants:\n\t\t";
			
			Iterator<Assistant> it = course.getCourseAssistants().iterator();
			
			while (it.hasNext()) {
				Assistant assistant = it.next();
				courseAssistants += assistant.toString() + "\n";
				courseAssistants += "\t\t";
			}
			
			courseAssistants += "\n";
			
			String groups = "\t- Course groups:\n";
			
			Map<String, Group> map = course.getGroup();
			
			for (Map.Entry<String, Group> mp : map.entrySet()) {
				groups += "\t\t- ID: " + mp.getKey() + "\n";
				groups += "\t\t- Assistant: " + mp.getValue().getAssistant().getFirstName() + " " + mp.getValue().getAssistant().getLastName() + "\n";
				groups += "\t\t- Students:\n";
				
				Iterator<Student> itr = mp.getValue().iterator();
				
				while (itr.hasNext()) {
					groups += "\t\t\t" + itr.next();
					groups += "\n";
				}
				
				groups += "\n";
			}
			
			String courseStatistics = "Course statistics:\n";
			Student student = course.getBestStudent();
			courseStatistics += "\t- Best student(according to the course strategy) is: " + student.getFirstName() + " " + student.getLastName() + "\n";
			courseStatistics += "\t- Students' number: " + course.getAllStudents().size() + "\n";
			
			double sum1 = 0.0;
			double sum2 = 0.0;
			double sum3 = 0.0;
			
			double averagePartialScore = 0.0;
			double averageExamScore = 0.0;
			double averageScore = 0.0;
			
			ArrayList<Student> allStudents = course.getAllStudents();
			
			Map<Student, Grade> grades = course.getAllStudentGrades();
			
			for (Map.Entry<Student, Grade> entry : grades.entrySet()) {
				sum1 += entry.getValue().getPartialScore();
				sum2 += entry.getValue().getExamScore();
				sum3 += entry.getValue().getTotal();
			}
			
			averagePartialScore = sum1 / allStudents.size();
			averageExamScore = sum2 / allStudents.size();
			averageScore = sum3 / allStudents.size();
			
			courseStatistics += "\t- Average partial score: " + String.format("%.2f", averagePartialScore) + "\n";
			courseStatistics += "\t- Average exam score: " + String.format("%.2f", averageExamScore) + "\n";
			courseStatistics += "\t- Average total score: " + String.format("%.2f", averageScore) + "\n";
			
			String info = "";
			info += courseInformations + courseTeacher + courseCredits + courseAssistants + groups + courseStatistics;
			informationsArea.setText(info);
			informationsArea.setEditable(false);
			
			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}