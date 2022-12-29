import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.simple.parser.ParseException;

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