import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
	
	Vector<Course> courses;
	
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
		centerPanel.add(textPane);
		
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		
		allCourses.addListSelectionListener(this);
		addStudent.addActionListener(this);
		addAssistant.addActionListener(this);
		
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
				String file = informations[0];
				String courseName = informations[1];
				String groupId = informations[2];
				String info = "";
				
				Course course = null;
				
				for (int i = 0; i < catalog.courses.size(); i++) {
					if (catalog.courses.get(i).getCourseName().equals(courseName)) {
						course = catalog.courses.get(i);
						break;
					}
				}
				
				try {
					info += catalog.addStudent(file, course, groupId);
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
			
			String info = "";
			info += courseInformations + courseTeacher + courseCredits + courseAssistants + groups;
			informationsArea.setText(info);
			informationsArea.setEditable(false);
			
			SwingUtilities.updateComponentTreeUI(this);
		}
	}
}