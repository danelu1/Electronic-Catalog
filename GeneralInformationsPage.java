import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.simple.parser.ParseException;

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