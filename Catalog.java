// toate Builderele merg
// Strategy merge
// Singleton merge
// Clasa Course: merge toata (la fel si clasele FullCourse si PartialCourse)
	// getAllStudents merge
	// getAllStudentGrades merge
	// getGraduatedStudents merge
	// getGrade merge
	// addAssistant merge
	// addStudent merge
	// addGrade merge
	// addGroup(Group) merge
	// addGroup(String, Assistant) merge
	// addGroup(String, Assistant, Comparator) merge
// Clasa Grade merge
// Clasa Course merge
// Clasa Snapshot merge
// Clasa Catalog merge

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class Main {
	public static void main(String[] args) throws CloneNotSupportedException, ParseException, org.json.simple.parser.ParseException, FileNotFoundException, IOException {
		Catalog catalog = Catalog.getInstance();
		catalog.open();
	}
}

class Catalog implements Subject {
	private static Catalog single_instance = null;
	List<Course> courses;
	List<Observer> observers;
	List<User> users;
	
	private Catalog() {
		observers = new ArrayList<>();
		users = new ArrayList<>();
	}
	
	public static Catalog getInstance() {
		if (single_instance == null) {
			single_instance = new Catalog();
		}
		return single_instance;
	}
	
	public void addCourse(Course course) {
		courses.add(course);
	}
	
	public void removeCourse(Course course) {
		courses.remove(course);
	}
	
	public void coursesParseJSON(String path) throws ParseException, org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		courses = new ArrayList<>();
		
		try (Reader reader = new FileReader(path)) {
			JSONObject coursesJSONObject = (JSONObject) parser.parse(reader);
			JSONArray coursesArray = (JSONArray) coursesJSONObject.get("courses");
			
			for (int i = 0; i < coursesArray.size(); i++) {
				JSONObject arrayEntry = (JSONObject) coursesArray.get(i);
				
				String courseName = (String) arrayEntry.get("course_name");
				String courseCredits = (String) arrayEntry.get("course_credits");
				
				JSONObject teacher = (JSONObject) arrayEntry.get("course_teacher");
				String teacherFirstName = (String) teacher.get("first_name");
				String teacherLastName = (String) teacher.get("last_name");
				Teacher courseTeacher = new Teacher(teacherFirstName, teacherLastName);
				
				Set<Assistant> assistants = new LinkedHashSet<>();
				TreeSet<Grade> grades = new TreeSet<>();
				Map<String, Group> map = new LinkedHashMap<>();
				
				JSONArray assistantsArray = (JSONArray) arrayEntry.get("course_assistants");
				
				
				for (int j = 0; j < assistantsArray.size(); j++) {
					JSONObject assistant = (JSONObject) assistantsArray.get(j);
					String assistantFirstName = (String) assistant.get("first_name");
					String assistantLastName = (String) assistant.get("last_name");
					Assistant assistant_aux = new Assistant(assistantFirstName, assistantLastName);
					assistants.add(assistant_aux);
				}
				
				JSONArray gradesArray = (JSONArray) arrayEntry.get("grades");
				
				for (int j = 0; j < gradesArray.size(); j++) {
					JSONObject grade = (JSONObject) gradesArray.get(j);
					String partialScore = (String) grade.get("partial_score");
					String examScore = (String) grade.get("exam_score");
					String name = (String) grade.get("course_name");
					Grade grade_aux = new Grade(Double.parseDouble(partialScore), Double.parseDouble(examScore), name);
					grades.add(grade_aux);
				}
				
				ArrayList<Grade> studentsGrades = new ArrayList<>();
				Iterator<Grade> it = grades.iterator();
				
				while (it.hasNext()) {
					studentsGrades.add(it.next());
				}
				
				JSONArray groupsArray = (JSONArray) arrayEntry.get("groups");
				
				int m = 0;
				
				for (int j = 0; j < groupsArray.size(); j++) {
					JSONObject group = (JSONObject) groupsArray.get(j);
					
					String id = (String) group.get("ID");
					
					JSONObject assistant = (JSONObject) group.get("assistant");
					String assistantFirstName = (String) assistant.get("first_name");
					String assistantLastName = (String) assistant.get("last_name");
					Assistant assistant_aux = new Assistant(assistantFirstName, assistantLastName);
					
					Group group_aux = new Group(id, assistant_aux);
					
					JSONArray students = (JSONArray) group.get("students");
					
					for (int k = 0; k < students.size() && m < grades.size(); k++) {
						JSONObject student = (JSONObject) students.get(k);
						String firstName = (String) student.get("first_name");
						String lastName = (String) student.get("last_name");
				
						Student s = new Student(firstName, lastName);
						group_aux.add(s);
						m++;
					}
					map.put(id, group_aux);
				}
				
				Course.CourseBuilder course = new FullCourse.FullCourseBuilder(courseName, Integer.parseInt(courseCredits));
				course.setCourseAssistants(assistants).setGrades(grades).setMap(map).setTeacher(courseTeacher);
				Course newCourse = new FullCourse(course);
				courses.add(newCourse);
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loginParseJSON(String type) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		
		if (type.equals("Student")) {
			try (Reader reader = new FileReader("./test/studentsLogin.json")) {
				JSONObject studentsObject = (JSONObject) parser.parse(reader);
				JSONArray studentsArray = (JSONArray) studentsObject.get("login_details");
				
				for (int i = 0; i < studentsArray.size(); i++) {
					JSONObject student = (JSONObject) studentsArray.get(i);
					String firstName = (String) student.get("first_name");
					String lastName = (String) student.get("last_name");
					String userName = (String) student.get("user_name");
					String userPassword = (String) student.get("user_password");
					String icon = (String) student.get("icon");
					Student student_aux = new Student(firstName, lastName, userName, userPassword, icon);
					student_aux.setUserName(userName);
					student_aux.setUserPassword(userPassword);
					student_aux.setIcon(icon);
					users.add(student_aux);
				}
			}
		} else if (type.equals("Assistant")) {
			try (Reader reader = new FileReader("./test/assistantsLogin.json")) {
				JSONObject assistantsObject = (JSONObject) parser.parse(reader);
				JSONArray assistantsArray = (JSONArray) assistantsObject.get("login_details");
				
				for (int i = 0; i < assistantsArray.size(); i++) {
					JSONObject assistant = (JSONObject) assistantsArray.get(i);
					String firstName = (String) assistant.get("first_name");
					String lastName = (String) assistant.get("last_name");
					String userName = (String) assistant.get("user_name");
					String userPassword = (String) assistant.get("user_password");
					String icon = (String) assistant.get("icon");
					Assistant assistant_aux = new Assistant(firstName, lastName, userName, userPassword, icon);
					assistant_aux.setUserName(userName);
					assistant_aux.setUserPassword(userPassword);
					assistant_aux.setIcon(icon);
					users.add(assistant_aux);
				}
			}
		} else if (type.equals("Teacher")) {
			try (Reader reader = new FileReader("./test/teachersLogin.json")) {
				JSONObject teachersObject = (JSONObject) parser.parse(reader);
				JSONArray teachersArray = (JSONArray) teachersObject.get("login_details");
				
				for (int i = 0; i < teachersArray.size(); i++) {
					JSONObject teacher = (JSONObject) teachersArray.get(i);
					String firstName = (String) teacher.get("first_name");
					String lastName = (String) teacher.get("last_name");
					String userName = (String) teacher.get("user_name");
					String userPassword = (String) teacher.get("user_password");
					String icon = (String) teacher.get("icon");
					Teacher teacher_aux = new Teacher(firstName, lastName, userName, userPassword, icon);
					teacher_aux.setUserName(userName);
					teacher_aux.setUserPassword(userPassword);
					teacher_aux.setIcon(icon);
					users.add(teacher_aux);
				}
			}
		} else if (type.equals("Parent")) {
			try (Reader reader = new FileReader("./test/parentsLogin.json")) {
				JSONObject parentsObject = (JSONObject) parser.parse(reader);
				JSONArray parentsArray = (JSONArray) parentsObject.get("fathers");
				
				for (int i = 0; i < parentsArray.size(); i++) {
					JSONObject parent = (JSONObject) parentsArray.get(i);
					String firstName = (String) parent.get("first_name");
					String lastName = (String) parent.get("last_name");
					String userName = (String) parent.get("user_name");
					String userPassword = (String) parent.get("user_password");
					String icon = (String) parent.get("icon");
					Parent parent_aux = new Parent(firstName, lastName, userName, userPassword, icon);
					parent_aux.setUserName(userName);
					parent_aux.setUserPassword(userPassword);
					parent_aux.setIcon(icon);
					users.add(parent_aux);
				}
			}
		}
	}

	@Override
	public void addObserver(Observer observer) {
		// TODO Auto-generated method stub
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		// TODO Auto-generated method stub
		observers.remove(observer);
	}

	@Override
	public void notifyObservers(Grade grade) {
		// TODO Auto-generated method stub
		for (Observer o : observers) {
			o.update(new Notification("partial", grade));
		}
	}
	
	public User checkUserNamePassword(String type, String userName, String userPassword) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
		loginParseJSON(type);
		
		for (User user : users) {
			if (user.getUserName().equals(userName) && user.getUserPassword().equals(userPassword)) {
				return user;
			}
		}
		
		return null;
	}
	
	public void open() throws ParseException, org.json.simple.parser.ParseException, FileNotFoundException, IOException {
		SelectionPage page = new SelectionPage("Select");
	}
	
	public String toString() {
		String ans = "";
		
		for (int i = 0; i < courses.size(); i++) {
			ans += "\t-> " + courses.get(i).getCourseName();
			ans += "\n";
		}
		
		return ans;
	}
}

abstract class Course {
	private String courseName;
	private Teacher courseTeacher;
	private Set<Assistant> courseAssistants;
	private TreeSet<Grade> grades;
	private Map<String, Group> map;
	private int courseCredits;
	private Snapshot snapshot;
	TreeSet<Grade> backup;
	
	public Course(CourseBuilder builder) {
		this.courseName = builder.courseName;
		this.courseTeacher = builder.courseTeacher;
		this.courseAssistants = builder.courseAssistants;
		this.grades = builder.grades;
		this.map = builder.map;
		this.courseCredits = builder.courseCredits;
		this.snapshot = new Snapshot(grades);
		backup = new TreeSet<>();
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public Teacher getCourseTeacher() {
		return courseTeacher;
	}
	
	public Set<Assistant> getCourseAssistants() {
		return courseAssistants;
	}
	
	public TreeSet<Grade> getGrades() {
		return grades;
	}
	
	public Map<String, Group> getGroup() {
		return map;
	}
	
	public int getCourseCredits() {
		return courseCredits;
	}
	
	public Snapshot getSnapshot() {
		return snapshot;
	}
	
	public void setCourseName(String course) {
		this.courseName = course;
	}
	
	public void setCourseCredits(int credits) {
		this.courseCredits = credits;
	}
	
	public void setCourseTeacher(Teacher teacher) {
		this.courseTeacher = teacher;
	}
	
	public void setCourseAssistants(Set<Assistant> assistants) {
		this.courseAssistants = assistants;
	}
	
	public void setCourseGrades(TreeSet<Grade> grades) {
		this.grades = grades;
	}
	
	public void setGroup(Map<String, Group> map) {
		this.map = map;
	}
	
	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}
	
	public void addAssistant(String ID, Assistant assistant) {
		if (map.get(ID).getAssistant() == null) {
			map.get(ID).setAssistant(assistant);
		} else {
			System.out.println("You can't assign an assistant to a group that already has one");
		}
		
		if (!courseAssistants.contains(assistant)) {
			courseAssistants.add(assistant);
		} else {
			System.out.println("The assistant is already assigned in this course");
		}
	}
	
	public void addStudent(String ID, Student student) {
		map.get(ID).add(student);
	}
	
	public void addGroup(Group group) {
		String id = group.getID();
		map.put(id, group);
	}
	
	public void addGroup(String ID, Assistant assistant) {
		Group group = new Group(ID, assistant);
		map.put(group.getID(), group);
	}
	
	public void addGroup(String ID, Assistant assistant, Comparator<Student> comp) {
		Group group = new Group(ID, assistant, comp);
		map.put(group.getID(), group);
	}
	
	public Grade getGrade(Student student) {
		HashMap<Student, Grade> situation = getAllStudentGrades();
		return situation.get(student);
	}
	
	public void addGrade(Grade grade) {
		grades.add(grade);
	}
	
	public ArrayList<Student> getAllStudents() {
		ArrayList<Student> result = new ArrayList<>();
		
		for (Map.Entry<String, Group> mp : map.entrySet()) {
			Iterator<Student> it = mp.getValue().iterator();
			
			while (it.hasNext()) {
				result.add(it.next());
			}
		}
		
		return result;
	}
	
	public LinkedHashMap<Student, Grade> getAllStudentGrades() {
		LinkedHashMap<Student, Grade> result = new LinkedHashMap<>();
		ArrayList<Student> students = getAllStudents();
		ArrayList<Grade> list = new ArrayList<>();
		Iterator<Grade> it = grades.iterator();
		
		while (it.hasNext()) {
			list.add(it.next());
		}
		
		for (int i = 0; i < grades.size(); i++) {
			result.put(students.get(i), list.get(i));
		}
		
		return result;
	}
	
	public abstract ArrayList<Student> getGraduatedStudents();
	
	static abstract class CourseBuilder {
		private String courseName;
		private Teacher courseTeacher;
		private Set<Assistant> courseAssistants;
		private TreeSet<Grade> grades;
		private Map<String, Group> map;
		private int courseCredits;
		
		public CourseBuilder(String courseName, int courseCredits) {
			this.courseName = courseName;
			this.courseCredits = courseCredits;
		}
		
		public CourseBuilder setTeacher(Teacher courseTeacher) {
			this.courseTeacher = courseTeacher;
			return this;
		}
		
		public CourseBuilder setCourseAssistants(Set<Assistant> courseAssistants) {
			this.courseAssistants = courseAssistants;
			return this;
		}
		
		public CourseBuilder setGrades(TreeSet<Grade> grades) {
			this.grades = grades;
			return this;
		}
		
		public CourseBuilder setMap(Map<String, Group> map) {
			this.map = map;
			return this;
		}
		
		abstract Course build();
	}
	
	public Student getBestStudent() {
		Strategy strategy = null;
		Student result = null;
		
		System.out.println("Select the best student according to your preferences:");
		System.out.println("1) Best Partial Score;");
		System.out.println("2) Best Exam Score;");
		System.out.println("3) Best Total Score.");
		System.out.print("Choose from above: ");
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();
		
		while (true) {
			if (choice == 1) {
				strategy = new BestPartialScore();
				break;
			} else if (choice == 2) {
				strategy = new BestExamScore();
				break;
			} else if (choice == 3) {
				strategy = new BestTotalScore();
				break;
			} else {
				System.out.print("Invalid choice! Try again: ");
				choice = scanner.nextInt();
				System.out.println();
			}
			
			scanner.close();
		}
		
		ArrayList<Grade> list = new ArrayList<>();
		Iterator<Grade> it = grades.iterator();
		
		while(it.hasNext()) {
			list.add(it.next());
		}
		
		Grade score = strategy.getBestScore(list);
		
		HashMap<Student, Grade> situation = getAllStudentGrades();
		
		for (Map.Entry<Student, Grade> mp: situation.entrySet()) {
			if (mp.getValue().equals(score)) {
				result = mp.getKey();
			}
		}
		
		return result;
	}
	
	private class Snapshot {
		private TreeSet<Grade> backup;
		
		public Snapshot(TreeSet<Grade> grades_aux) {
			this.backup = grades_aux;
		}
		
		public String toString() {
			String ans = "";
			Iterator<Grade> it = backup.iterator();
			
			while (it.hasNext()) {
				ans += it.next() + "\n";
			}
			
			return ans;
		}
	}
	
	public void makeBackup() throws CloneNotSupportedException {
		Iterator<Grade> it = grades.iterator();
		
		while (it.hasNext()) {
			Grade grade = (Grade) it.next().clone();
			backup.add(grade);
		}
	}
	
	public void undo() {
		this.grades = backup;
	}
	
	public String toString() {
		String ans = "";
		ans += "-> " + courseName + "\n";
		
		return ans;
	}
}

class PartialCourse extends Course {
	private PartialCourse(Course.CourseBuilder builder) {
		super(builder);
	}

	@Override
	public ArrayList<Student> getGraduatedStudents() {
		ArrayList<Student> result = new ArrayList<>();
		ArrayList<Student> students = getAllStudents();
		
		for (int i = 0; i < students.size(); i++) {
			if (getGrade(students.get(i)).getTotal() >= 5) {
				result.add(students.get(i));
			}
		}
		
		return result;
	}
	
	public static class PartialCourseBuilder extends CourseBuilder {
		public PartialCourseBuilder(String name, int credits) {
			super(name, credits);
		}
	
		@Override
		public PartialCourse build() {
			return new PartialCourse(this);
		}
	}
}

class FullCourse extends Course {
	public FullCourse(Course.CourseBuilder builder) {
		super(builder);
	}

	@Override
	public ArrayList<Student> getGraduatedStudents() {
		ArrayList<Student> result = new ArrayList<>();
		ArrayList<Student> students = getAllStudents();
		
		for (int i = 0; i < students.size(); i++) {
			if (getGrade(students.get(i)).getPartialScore() >= 3 && getGrade(students.get(i)).getExamScore() >= 2) {
				result.add(students.get(i));
			}
		}
		
		return result;
	}
	
	static class FullCourseBuilder extends Course.CourseBuilder {
		public FullCourseBuilder(String name, int credits) {
			super(name, credits);
		}
		
		public Course build() {
			return new FullCourse(this);
		}
	}
}

class UserFactory {
	public User getUser(String userType, String firstName, String lastName) {
		if (userType == null) {
			return null;
		} else if (userType.equals("Parent")) {
			return new Parent(firstName, lastName);
		} else if (userType.equals("Student")) {
			return new Student(firstName, lastName);
		} else if (userType.equals("Assistant")) {
			return new Assistant(firstName, lastName);
		} else if (userType.equals("Teacher")) {
			return new Teacher(firstName, lastName);
		}
		return null;
	}
}

abstract class User {
	private String firstName;
	private String lastName;
	private String userName;
	private String userPassword;
	private String icon;
	
	public User(String firstName, String lastName, String userName, String userPassword, String icon) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public User(String firstName, String lastName) {
		this(firstName, lastName, null, null, null);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setFirstName(String name) {
		this.firstName = name;
	}
	
	public void setLastName(String name) {
		this.lastName = name;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String toString() {
		return firstName + " " + lastName;
	}
}

class Student extends User implements Comparable<Student> {
	private Parent mother;
	private Parent father;
	
	public Student(String firstName, String lastName, String userName, String userPassword, String icon) {
		super(firstName, lastName, userName, userPassword, icon);
	}
	
	public Student(String firstName, String lastName) {
		this(firstName, lastName, null, null, null);
	}
	
	public String getFirstName() {
		return super.getFirstName();
	}
	
	public String getLastName() {
		return super.getLastName();
	}
	
	public void setFirstName(String name) {
		super.setFirstName(name);
	}
	
	public void setLastName(String name) {
		super.setLastName(name);
	}
	
	public Parent getMother() {
		return mother;
	}
	
	public Parent getFather() {
		return father;
	}
	
	public void setMother(Parent parent) {
		this.mother = parent;
	}
	
	public void setFather(Parent parent) {
		this.father = parent;
	}
	
	@Override
	public int compareTo(Student s) {
		if (!this.getFirstName().equals(s.getFirstName())) {
			return this.getFirstName().compareTo(s.getFirstName());
		} else {
			return this.getLastName().compareTo(s.getLastName());
		}
	}
	
	public String toString() {
		String ans = "-> " + this.getFirstName() + " " + this.getLastName();
		return ans;
	}
}

class Parent extends User implements Observer {	
	public Parent(String firstName, String lastName, String userName, String userPassword, String icon) {
		super(firstName, lastName, userName, userPassword, icon);
	}
	
	public Parent(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
	public String getFirstName() {
		return super.getFirstName();
	}
	
	public String getLastName() {
		return super.getLastName();
	}
	
	public void setFirstName(String name) {
		super.setFirstName(name);
	}
	
	public void setLastName(String name) {
		super.setLastName(name);
	}
	
	public String toString() {
		String ans = "parent: {\n";
		ans += "\tfirst name : " + this.getFirstName() + ",\n";
		ans += "\tlast name : " + this.getLastName() + "\n" + "}";
		
		return ans;
	}

	@Override
	public void update(Notification notification) {
		// TODO Auto-generated method stub
		System.out.println(notification);
	}
}

class Assistant extends User implements Element {
	public Assistant(String firstName, String lastName, String userName, String userPassword, String icon) {
		super(firstName, lastName, userName, userPassword, icon);
	}
	
	public Assistant(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
	public String getFirstName() {
		return super.getFirstName();
	}
	
	public String getLastName() {
		return super.getLastName();
	}
	
	public void setFirstName(String name) {
		super.setFirstName(name);
	}
	
	public void setLastName(String name) {
		super.setLastName(name);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	public String toString() {
		String ans = "-> ";
		ans += this.getFirstName() + " " + this.getLastName();
		
		return ans;
	}
}

class Teacher extends User implements Element {
	public Teacher(String firstName, String lastName, String userName, String userPassword, String icon) {
		super(firstName, lastName, userName, userPassword, icon);
	}
	
	public Teacher(String firstName, String lastName) {
		super(firstName, lastName);
	}
	
	public String getFirstName() {
		return super.getFirstName();
	}
	
	public String getLastName() {
		return super.getLastName();
	}
	
	public void setFirstName(String name) {
		super.setFirstName(name);
	}
	
	public void setLastName(String name) {
		super.setLastName(name);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	public String toString() {
		String ans = "teacher: {\n";
		ans += "\tfirst name : " + this.getFirstName() + ",\n";
		ans += "\tlast name : " + this.getLastName() + "\n" + "}";
		
		return ans;
	}
}

@SuppressWarnings("rawtypes")
class Grade implements Cloneable, Comparable {
	private Double partialScore;
	private Double examScore;
	private String course;
	private Student student;
	
	public Grade(Double partialScore, Double examScore, String course) {
		this.partialScore = partialScore;
		this.examScore = examScore;
		this.course = course;
	}
	
	public Double getPartialScore() {
		return partialScore;
	}
	
	public Double getExamScore() {
		return examScore;
	}
	
	public String getCourse() {
		return course;
	}
	
	public Student getStudent() {
		return student;
	}
	
	public void setPartialScore(Double score) {
		partialScore = score;
	}
	
	public void setExamScore(Double score) {
		examScore = score;
	}
	
	public void setCourse(String toSet) {
		course = toSet;
	}
	
	public void setStudent(Student s) {
		student = s;
	}

	@Override
	public int compareTo(Object o) {
		Grade g = (Grade) o;
		
		if (g.getTotal() < this.getTotal()) {
			return 1;
		} else if (g.getTotal() == this.getTotal()) {
			return this.student.getFirstName().compareTo(g.student.getFirstName());
		} else {
			return -1;
		}
	}
	
	public Double getTotal() {
		Double total = null;
		total = partialScore + examScore;
		return total;
	}
	
	public Grade clone() throws CloneNotSupportedException {
		return (Grade) super.clone();
	}
	
	public String printPartialScore() {
		String ans = "-> Partial score: " + partialScore;
		return ans;
	}
	
	public String printExamScore() {
		return "-> Exam score: " + examScore;
	}
	
	public String printTotalScore() {
		return "-> Total score: " + getTotal();
	}
	
	public String toString() {
		String ans = "-> Partial score: " + partialScore + ";\n";
		ans += "-> Exam score: " + examScore + ";\n";
		ans += "-> Total score: " + getTotal() + ".\n";
		return ans;
	}
}

class Group extends TreeSet<Student> {
	private static final long serialVersionUID = 1L;
	
	private Assistant assistant;
	private String ID;
	
	public Group(String ID, Assistant assistant, Comparator<Student> comp) {
		super(comp);
		this.ID = ID;
		this.assistant = assistant;
	}
	
	public Group(String ID, Assistant assistant) {
		this(ID, assistant, null);
	}
	
	public Assistant getAssistant() {
		return assistant;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setAssistant(Assistant a) {
		assistant = a;
	}
	
	public void setID(String toSet) {
		ID = toSet;
	}
}

interface Strategy {
	public Grade getBestScore(List<Grade> grades);
}

class BestPartialScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getPartialScore();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getPartialScore()) {
				maxPartialScore = grades.get(i).getPartialScore();
				index = i;
			}
		}
		
		return grades.get(index);
	}
}

class BestExamScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getExamScore();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getExamScore()) {
				maxPartialScore = grades.get(i).getExamScore();
				index = i;
			}
		}
		
		return grades.get(index);
	}
}

class BestTotalScore implements Strategy {
	@Override
	public Grade getBestScore(List<Grade> grades) {
		double maxPartialScore = grades.get(0).getTotal();
		int index = 0;
		
		for (int i = 0; i < grades.size(); i++) {
			if (maxPartialScore < grades.get(i).getTotal()) {
				maxPartialScore = grades.get(i).getTotal();
				index = i;
			}
		}
		
		return grades.get(index);
	}	
}

interface Visitor {
	public void visit(Assistant assistant);
	public void visit(Teacher teacher);
}

interface Element {
	public void accept(Visitor visitor);
}

class ScoreVisitor implements Visitor {
	Map<Teacher, ArrayList<Tuple<Student, String, Double>>> examScores;
	Map<Assistant, ArrayList<Tuple<Student, String, Double>>> partialScores;
	
	public ScoreVisitor() {
		examScores = new LinkedHashMap<>();
		partialScores = new LinkedHashMap<>();
	}

	@Override
	public void visit(Assistant assistant) {
		ArrayList<Tuple<Student, String, Double>> list = partialScores.get(assistant);
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setC(1D);
		}
	}

	@Override
	public void visit(Teacher teacher) {
		ArrayList<Tuple<Student, String, Double>> list = examScores.get(teacher);
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setC(1D);
		}
	}
	
	private class Tuple<A, B, C> {
		private A elementA;
		private B elementB;
		private C elementC;
		
		public Tuple(A a, B b, C c) {
			this.elementA = a;
			this.elementB = b;
			this.elementC = c;
		}
		
		public A getA() {
			return elementA;
		}
		
		public B getB() {
			return elementB;
		}
		
		public C getC() {
			return elementC;
		}
		
		public void setA(A a) {
			elementA = a;
		}
		
		public void setB(B b) {
			elementB = b;
		}
		
		public void setC(C c) {
			elementC = c;
		}
	}
}

interface Observer {
	public void update(Notification notification);
}

interface Subject {
	public void addObserver(Observer observer);
	public void removeObserver(Observer observer);
	public void notifyObservers(Grade grade);
}

class Notification {
	private String gradeType;
	private Grade grade;
	
	public Notification(String message, Grade grade) {
		this.gradeType = message;
		this.grade = grade;
	}
	
	public String getGradeType() {
		return gradeType;
	}
	
	public void setGradeType(String message) {
		this.gradeType = message;
	}
	
	public Grade getGrade() {
		return grade;
	}
	
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	public String toString() {
		String str = "";
		str += "Your child just got the " + gradeType + " grade : ";
		
		if (gradeType.equals("partial")) {
			str += grade.getPartialScore();
		} else if (gradeType.equals("exam")) {
			str += grade.getExamScore();
		}
		
		return str;
	}
}