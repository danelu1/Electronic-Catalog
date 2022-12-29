import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.simple.parser.ParseException;

class ParentMainPage extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	JTextArea notificationsArea;
	
	JButton notificationsButton;
	JButton logoutButton;
	
	JLabel accountLabel;
	
	JScrollPane scrollPane;
	
	JPanel northPanel;
	JPanel southPanel;
	
	UserFactory factory = new UserFactory();
	
	SelectionPage page;
	
	public ParentMainPage(String message) {
		super(message);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 800));
		getContentPane().setBackground(new Color(144, 169, 196));
		
		Catalog catalog = Catalog.getInstance();
		
		try {
			catalog.coursesParseJSON("./test/courses.json");
		} catch (java.text.ParseException | ParseException e) {
			e.printStackTrace();
		}
		
		User user = ParentLoginPage.user;
		
		accountLabel = new JLabel("--------------------- " + user.getFirstName() + " " + user.getLastName() + " Account ---------------------");
		accountLabel.setFont(new Font("Verdana", Font.BOLD, 25));
		
		
		northPanel = new JPanel();
		northPanel.add(accountLabel);
		
		notificationsArea = new JTextArea();
		
		scrollPane = new JScrollPane(notificationsArea);
		
		notificationsButton = new JButton("View");
		logoutButton = new JButton("Log out");
		
		southPanel = new JPanel(new GridLayout(1, 2));
		southPanel.add(notificationsButton);
		southPanel.add(logoutButton);
		
		add(northPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		logoutButton.addActionListener(this);
		notificationsButton.addActionListener(this);
		
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
			} else if (e.getSource() == notificationsButton) {
				ArrayList<Student> allStudents1 = TeacherMainPage.allStudents;
				ArrayList<Student> allStudents2 = AssistantMainPage.allStudents;
				
				User user = ParentLoginPage.user;
				Parent parent = (Parent) factory.getUser("Parent", user.getFirstName(), user.getLastName());
				int index = 0;
				int index2 = 0;
				
				String notification = "";
				
				if (allStudents1 == null && allStudents2 == null) {
					notification += "You don't have any notification yet";
				} else if (allStudents1 != null && allStudents2 == null) {
					for (int i = 0; i < allStudents1.size(); i++) {
						Student s = allStudents1.get(i);
						if (parent.equals(s.getFather()) || parent.equals(s.getMother())) {
							index = i;
							break;
						}
					}
					
					Student student = allStudents1.get(index);
					
					if (student.notifications.size() == 0) {
						notification += "You don't have any notification yet";
					} else {
						LinkedHashSet<String> set = new LinkedHashSet<>();
						
						for (int i = 0; i < student.notifications.size(); i++) {
							set.add(student.notifications.get(i).toString());
						}
						
						Iterator<String> it = set.iterator();
						
						while (it.hasNext()) {
							notification += it.next() + "\n";
						}
					}
				} else if (allStudents1 == null && allStudents2 != null) {
					for (int i = 0; i < allStudents2.size(); i++) {
						Student s = allStudents2.get(i);
						if (parent.equals(s.getFather()) || parent.equals(s.getMother())) {
							index = i;
							break;
						}
					}
					
					Student student = allStudents2.get(index);
					
					if (student.notifications.size() == 0) {
						notification += "You don't have any notification yet";
					} else {
						LinkedHashSet<String> set = new LinkedHashSet<>();
						
						for (int i = 0; i < student.notifications.size(); i++) {
							set.add(student.notifications.get(i).toString());
						}
						
						Iterator<String> it = set.iterator();
						
						while (it.hasNext()) {
							notification += it.next() + "\n";
						}
					}
				} else {
					for (int i = 0; i < allStudents1.size(); i++) {
						Student s1 = allStudents1.get(i);
						
						for (int j = 0; j < allStudents2.size(); j++) {
							Student s2 = allStudents2.get(j);
							
							if ((parent.equals(s1.getFather()) || parent.equals(s1.getMother())) &&
									(parent.equals(s2.getFather()) || parent.equals(s2.getMother()))) {
								index = i;
								index2 = j;
								break;
							}
						}
					}
					
					Student student = allStudents1.get(index);
					Student student2 = allStudents2.get(index2);
					
					if (student.notifications.size() == 0 && student2.notifications.size() == 0) {
						notification += "You don't have any notification yet";
					} else {
						LinkedHashSet<String> set = new LinkedHashSet<>();
						
						for (int i = 0; i < student.notifications.size(); i++) {
							set.add(student.notifications.get(i).toString());
						}
						
						for (int i = 0; i < student2.notifications.size(); i++) {
							set.add(student2.notifications.get(i).toString());
						}
						
						Iterator<String> it = set.iterator();
						
						while (it.hasNext()) {
							notification += it.next() + "\n";
						}
					}
				}
				
				notificationsArea.setText(notification);
			}
		}
	}
}