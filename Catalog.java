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
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class Main {
	public static void main(String[] args) throws CloneNotSupportedException, ParseException, org.json.simple.parser.ParseException, FileNotFoundException, IOException {
		Catalog catalog = Catalog.getInstance();
		catalog.open();
		
//		catalog.coursesParseJSON("./test/courses.json");
//		
//		ScoreVisitor visitor = new ScoreVisitor();
//		
//		catalog.courses.get(0).getCourseTeacher().accept(visitor);
//		
//		ArrayList<Student> students = catalog.courses.get(0).getAllStudents();
//		
//		for (Student s : students) {
//			System.out.println(s.notifications);
//		}
	}
}

class Catalog implements Subject {
	private static Catalog single_instance = null;
	List<Course> courses;
	List<Observer> observers;
	List<User> users;
	List<Grade> grades;
	
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
		
		ArrayList<Grade> grades = course.getGrades();
		
		for (int i = 0; i < grades.size(); i++) {
			Grade grade = grades.get(i);
			Notification notification = new Notification(grade.getCourse(), grade, grade.getStudent().getMother(), grade.getStudent().getFather());
			
			if (!observers.contains(notification)) {
				addObserver(notification);
			}
			
			notifyObservers(grade);
		}
	}
	
	public void removeCourse(Course course) {
		courses.remove(course);
	}
	
	public Grade getGrade(Student student, Course course) {
		for (int i = 0; i < grades.size(); i++) {
			if (grades.get(i).getStudent().equals(student) && course.getCourseName().equals(grades.get(i).getCourse())) {
				return grades.get(i);
			}
		}
		return null;
	}
	
	public ArrayList<ArrayList<Grade>> addGradesToCatalog() {
		try {
			coursesParseJSON("./test/courses.json");
		} catch (ParseException | org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		
		ArrayList<ArrayList<Grade>> grades = new ArrayList<ArrayList<Grade>>();
		
		ScoreVisitor visitor = new ScoreVisitor();
		
		
		for (int i  = 0; i < courses.size(); i++) {
			Iterator<Assistant> it = courses.get(i).getCourseAssistants().iterator();
			ArrayList<Assistant> assistants = new ArrayList<>();
			ArrayList<Grade> grades_aux = new ArrayList<>();
			
			while (it.hasNext()) {
				assistants.add(it.next());
			}
			
			for (int j = 0; j < assistants.size(); j++) {
				ArrayList<Grade> gradesToAdd = visitor.combine(courses.get(i).getCourseTeacher(), assistants.get(j));
				grades_aux.addAll(gradesToAdd);
			}
			
			grades.add(grades_aux);
			
			courses.get(i).setCourseGrades(grades.get(i));
		}
		
		return grades;
	}
	
	public void gradesParseJSON(String path) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		grades = new ArrayList<>();
		
		try (Reader reader = new FileReader(path)) {
			JSONObject gradesJSONObject = (JSONObject) parser.parse(reader);
			JSONArray gradesArray = (JSONArray) gradesJSONObject.get("grades");
			
			for (int j = 0; j < gradesArray.size(); j++) {
				JSONObject grade = (JSONObject) gradesArray.get(j);
				String partialScore = (String) grade.get("partial_score");
				String examScore = (String) grade.get("exam_score");
				String name = (String) grade.get("course_name");
				
				JSONObject student = (JSONObject) grade.get("student");
				String firstName = (String) student.get("first_name");
				String lastName = (String) student.get("last_name");
				Student s = new Student(firstName, lastName);
				
				JSONObject motherObject = (JSONObject) student.get("mother");
				String motherFirstName = (String) motherObject.get("first_name");
				String motherLastName = (String) motherObject.get("last_name");
				s.setMother(new Parent(motherFirstName, motherLastName));
				
				JSONObject fatherObject = (JSONObject) student.get("father");
				String fatherFirstName = (String) fatherObject.get("first_name");
				String fatherLastName = (String)  fatherObject.get("last_name");
				s.setFather(new Parent(fatherFirstName, fatherLastName));
				
				Grade grade_aux = new Grade(Double.parseDouble(partialScore), Double.parseDouble(examScore), name, s);
				Notification notification = new Notification(name, grade_aux, s.getMother(), s.getFather());
				grades.add(grade_aux);
				observers.add(notification);
			}
		}
	}
	
	public void coursesParseJSON(String path) throws ParseException, org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		courses = new ArrayList<>();
		
		try {
			gradesParseJSON("./test/grades.json");
		} catch (IOException | org.json.simple.parser.ParseException e1) {
			e1.printStackTrace();
		}
		
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
				Map<String, Group> map = new LinkedHashMap<>();
				
				JSONArray assistantsArray = (JSONArray) arrayEntry.get("course_assistants");
				
				
				for (int j = 0; j < assistantsArray.size(); j++) {
					JSONObject assistant = (JSONObject) assistantsArray.get(j);
					String assistantFirstName = (String) assistant.get("first_name");
					String assistantLastName = (String) assistant.get("last_name");
					Assistant assistant_aux = new Assistant(assistantFirstName, assistantLastName);
					assistants.add(assistant_aux);
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
						
						JSONObject motherObject = (JSONObject) student.get("mother");
						JSONObject fatherObject = (JSONObject) student.get("father");
						String motherFirstName = (String) motherObject.get("first_name");
						String motherLastName = (String) motherObject.get("last_name");
						String fatherFirstName = (String) fatherObject.get("first_name");
						String fatherLastName = (String) fatherObject.get("last_name");
						Parent mother = new Parent(motherFirstName, motherLastName);
						Parent father = new Parent(fatherFirstName, fatherLastName);
				
						Student s = new Student(firstName, lastName);
						s.setMother(mother);
						s.setFather(father);
						
						group_aux.add(s);
						m++;
					}
					map.put(id, group_aux);
				}
				
				String strategyObject = (String) arrayEntry.get("strategy");
				
				Strategy strategy = null;
				
				if (strategyObject.equals("1")) {
					strategy = new BestPartialScore();
				} else if (strategyObject.equals("2")) {
					strategy = new BestExamScore();
				} else if (strategyObject.equals("3")) {
					strategy = new BestTotalScore();
				}
				
				Course.CourseBuilder course = new FullCourse.FullCourseBuilder(courseName, Integer.parseInt(courseCredits));
				course.setCourseAssistants(assistants).setMap(map).setTeacher(courseTeacher).setStrategy(strategy);
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
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers(Grade grade) {
		Notification notification = new Notification(grade.getCourse(), grade, grade.getStudent().getMother(), grade.getStudent().getFather());
		
		if (observers.contains(notification)) {
			observers.get(observers.indexOf(notification)).update(notification);
			notification.getGrade().getStudent().notifications.add(notification.toString());
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
	
	@SuppressWarnings("unused")
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
	private ArrayList<Grade> grades;
	private Map<String, Group> map;
	private int courseCredits;
	private Snapshot snapshot;
	private Strategy strategy;
	ArrayList<Grade> backup;
	
	public Course(CourseBuilder builder) {
		this.courseName = builder.courseName;
		this.courseTeacher = builder.courseTeacher;
		this.courseAssistants = builder.courseAssistants;
		this.grades = builder.grades;
		this.map = builder.map;
		this.courseCredits = builder.courseCredits;
		this.snapshot = new Snapshot(grades);
		this.strategy = builder.strategy;
		backup = new ArrayList<>();
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
	
	public ArrayList<Grade> getGrades() {
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
	
	public Strategy getStrategy() {
		return strategy;
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
	
	public void setCourseGrades(ArrayList<Grade> grades) {
		this.grades = grades;
	}
	
	public void setGroup(Map<String, Group> map) {
		this.map = map;
	}
	
	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}
	
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public void addAssistant(String ID, Assistant assistant) {
		if (map.get(ID).getAssistant() == null) {
			map.get(ID).setAssistant(assistant);
			return;
		} else {
			System.out.println("You can't assign an assistant to a group that already has one");
		}
		
		if (!courseAssistants.contains(assistant) && map.get(ID).getAssistant() == null) {
			courseAssistants.add(assistant);
		} else {
			System.out.println("The assistant is already assigned in this course");
		}
	}
	
	public void addStudent(String ID, Student student) {
		Group group = map.get(ID);
		
		if (group.contains(student)) {
			System.out.println("The student is already assigned in this group!");
		} else {
			group.add(student);
		}
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
		LinkedHashMap<Student, Grade> situation = getAllStudentGrades();
		if (situation.containsKey(student)) {
			return situation.get(student);
		}
		return null;
	}
	
	public void addGrade(Grade grade) {
		ArrayList<Student> students = getAllStudents();
		
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).equals(grade.getStudent())) {
				return;
			} else if (!students.get(i).equals(grade.getStudent()) && grades.get(i) == null) {
				grades.add(grade);
			}
		}
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
		Catalog catalog = Catalog.getInstance();
		
		ArrayList<ArrayList<Grade>> list = catalog.addGradesToCatalog();
		
		LinkedHashMap<Student, Grade> result = new LinkedHashMap<>();
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				if (this.getCourseName().equals(list.get(i).get(j).getCourse())) {
					result.put(list.get(i).get(j).getStudent(), list.get(i).get(j));
				}
			}
		}
		
		return result;
	}
	
	public abstract ArrayList<Student> getGraduatedStudents();
	
	static abstract class CourseBuilder {
		private String courseName;
		private Teacher courseTeacher;
		private Set<Assistant> courseAssistants;
		private ArrayList<Grade> grades;
		private Map<String, Group> map;
		private int courseCredits;
		private Strategy strategy;
		
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
		
		public CourseBuilder setGrades(ArrayList<Grade> grades) {
			this.grades = grades;
			return this;
		}
		
		public CourseBuilder setMap(Map<String, Group> map) {
			this.map = map;
			return this;
		}
		
		public CourseBuilder setStrategy(Strategy strategy) {
			this.strategy = strategy;
			return this;
		}
		
		abstract Course build();
	}
	
	public Student getBestStudent() {
		Catalog catalog = Catalog.getInstance();
		
		if (strategy instanceof BestPartialScore) {
			strategy = new BestPartialScore();
		} else if (strategy instanceof BestExamScore) {
			strategy = new BestExamScore();
		} else if (strategy instanceof BestTotalScore) {
			strategy = new BestTotalScore();
		}
		
		ArrayList<ArrayList<Grade>> list = catalog.addGradesToCatalog();
		ArrayList<Grade> arrayList = new ArrayList<>();
		
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				if (this.getCourseName().equals(list.get(i).get(j).getCourse())) {
					arrayList.add(list.get(i).get(j));
				}
			}
		}
		
		Grade score = strategy.getBestScore(arrayList);
		
		return score.getStudent();
	}
	
	private class Snapshot {
		private ArrayList<Grade> backup;
		
		public Snapshot(ArrayList<Grade> grades_aux) {
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
	ArrayList<String> notifications;
	
	public Student(String firstName, String lastName, String userName, String userPassword, String icon) {
		super(firstName, lastName, userName, userPassword, icon);
		notifications = new ArrayList<>();
	}
	
	public Student(String firstName, String lastName) {
		this(firstName, lastName, null, null, null);
		notifications = new ArrayList<>();
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
	
	public boolean equals(Object obj) {
		Student s = (Student) obj;
		if (this.getFirstName().equals(s.getFirstName()) && this.getLastName().equals(s.getLastName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		if (this.getFirstName() == null ^ this.getLastName() == null) {
			return 0;
		} else {
			return this.getFirstName().hashCode() ^ this.getLastName().hashCode();
		}
	}
}

class Parent extends User {	
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
	
	public int hashCode() {
		if (this.getFirstName() == null ^ this.getLastName() == null) {
			return 0;
		} else {
			return this.getFirstName().hashCode() ^ this.getLastName().hashCode();
		}
	}
	
	public boolean equals(Object obj) {
		Parent p = (Parent) obj;
		
		if (this.getFirstName().equals(p.getFirstName()) && this.getLastName().equals(p.getLastName())) {
			return true;
		} else {
			return false;
		}
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
	
	public boolean equals(Object obj) {
		Assistant a = (Assistant) obj;
		
		if (this.getFirstName().equals(a.getFirstName()) && this.getLastName().equals(a.getLastName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		if (this.getFirstName() == null ^ this.getLastName() == null) {
			return 0;
		} else {
			return this.getFirstName().hashCode() ^ this.getLastName().hashCode();
		}
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
	
	public boolean equals(Object obj) {
		Teacher t = (Teacher) obj;
		
		if (this.getFirstName().equals(t.getFirstName()) && this.getLastName().equals(t.getLastName())) {
			return true;
		} else {
			return false;
		}
	}
}

@SuppressWarnings("rawtypes")
class Grade implements Cloneable, Comparable {
	private Double partialScore;
	private Double examScore;
	private String course;
	private Student student;
	
	public Grade(Double partialScore, Double examScore, String course, Student student) {
		this.partialScore = partialScore;
		this.examScore = examScore;
		this.course = course;
		this.student = student;
	}
	
	public Grade(Double partialScore, String course, Student student) {
		this(partialScore, null, course, student);
	}
	
	public Grade(String course, Double examScore, Student student) {
		this(null, examScore, course, student);
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
		total = partialScore.doubleValue() + examScore.doubleValue();
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
		String firstName = student.getFirstName();
		String lastName = student.getLastName();
		
		String ans = "Student " + firstName + " " + lastName + " has at " + course + " the grades:\n";
		ans = "\t-> Partial score: " + partialScore + ";\n";
		ans += "\t-> Exam score: " + examScore + ";\n";
		ans += "\t-> Total score: " + getTotal() + ".\n";
		return ans;
	}
	
	public boolean equals(Object obj) {
		Grade g = (Grade) obj;
		if (this.getPartialScore() == g.getPartialScore() && this.getExamScore() == g.getExamScore()) {
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		if (this.getPartialScore() == null ^ this.getExamScore() == null) {
			return 0;
		} else {
			return this.getPartialScore().hashCode() ^ this.getExamScore().hashCode();
		}
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
	public void visit(Teacher teacher) {
		Catalog catalog = Catalog.getInstance();
		
		try {
			catalog.gradesParseJSON("./test/grades.json");
		} catch (IOException | org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			Course course = catalog.courses.get(i);
			String courseName = course.getCourseName();
			
			if (course.getCourseTeacher().equals(teacher)) {
				ArrayList<Student> students = course.getAllStudents();
				
				ArrayList<Tuple<Student, String, Double>> gradesList = new ArrayList<>();
				
				for (int j = 0; j < students.size(); j++) {
					Tuple<Student, String, Double> grade_aux = new Tuple<>(students.get(j), courseName, catalog.getGrade(students.get(j), course).getExamScore());
					gradesList.add(grade_aux);
				}
				
				examScores.put(teacher, gradesList);
				
				for (Tuple<Student, String, Double> tuple : gradesList) {
					catalog.notifyObservers(new Grade(0.0,  tuple.getDoubleGrade(), tuple.getCourse(), tuple.getStudent()));
				}
			}
		}
	}
	
	public String printGradesTeacher(Teacher teacher) {
		String grades = "Grades to validate:\n";
		
		ScoreVisitor visitor = new ScoreVisitor();
		
		ArrayList<Tuple<Student, String, Double>> list = examScores.get(teacher);
		
		teacher.accept(visitor);
		
		for (int i = 0; i < list.size(); i++) {
			grades += "\t" + list.get(i).toString() + "\n";
		}
		
		return grades;
	}

	@Override
	public void visit(Assistant assistant) {
		Catalog catalog = Catalog.getInstance();
		
		try {
			catalog.gradesParseJSON("./test/grades.json");
		} catch (IOException | org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			Course course = catalog.courses.get(i);
			String courseName = course.getCourseName();
			
			if (course.getCourseAssistants().contains(assistant)) {
				ArrayList<Student> students = new ArrayList<>();
				Map<String, Group> map = course.getGroup();
				
				for (Map.Entry<String, Group> mp : map.entrySet()) {
					if (mp.getValue().getAssistant().equals(assistant)) {
						Iterator<Student> it = mp.getValue().iterator();
						
						while (it.hasNext()) {
							students.add(it.next());
						}
					}
				}
				
				ArrayList<Tuple<Student, String, Double>> gradesList = new ArrayList<>();
				
				for (int j = 0; j < students.size(); j++) {
					Student student = students.get(j);
					Tuple<Student, String, Double> grade_aux = new Tuple<>(student, courseName, catalog.getGrade(student, course).getPartialScore());
					gradesList.add(grade_aux);
				}
				
				partialScores.put(assistant, gradesList);
				
				for (Tuple<Student, String, Double> tuple : gradesList) {
					catalog.notifyObservers(new Grade(tuple.getDoubleGrade(), 0.0, tuple.getCourse(), tuple.getStudent()));
				}
			}
		}
	}
	
	public String printGradesAssistant(Assistant assistant) {
		String grades = "Grades to validate:\n";
		
		ScoreVisitor visitor = new ScoreVisitor();
		
		ArrayList<Tuple<Student, String, Double>> list = partialScores.get(assistant);
		
		assistant.accept(visitor);
		
		for (int i = 0; i < list.size(); i++) {
			grades += "\t" + list.get(i).toString() + "\n";
		}
		
		return grades;
	}
	
	public ArrayList<Grade> combine(Teacher teacher, Assistant assistant) {
		Catalog catalog = Catalog.getInstance();
		
		ScoreVisitor visitor = new ScoreVisitor();
		
		teacher.accept(visitor);
		assistant.accept(visitor);
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			Course course = catalog.courses.get(i);
			
			if (course.getCourseTeacher().equals(teacher) && course.getCourseAssistants().contains(assistant)) {
				ArrayList<Tuple<Student, String, Double>> gradesAssistant = visitor.partialScores.get(assistant);
				ArrayList<Tuple<Student, String, Double>> gradesTeacher = visitor.examScores.get(teacher);
				ArrayList<Grade> grades = new ArrayList<>();
				
				for (int j = 0; j < gradesTeacher.size(); j++) {
					for (int k = 0; k < gradesAssistant.size(); k++) {
						if (gradesTeacher.get(j).getCourse().equals(gradesAssistant.get(k).getCourse()) && gradesTeacher.get(j).getStudent().
								equals(gradesAssistant.get(k).getStudent())) {
							Double partialScore = gradesAssistant.get(k).getDoubleGrade();
							Double examScore = gradesTeacher.get(j).getDoubleGrade();
							String name = course.getCourseName();
							Student s = gradesTeacher.get(j).getStudent();
							grades.add(new Grade(partialScore, examScore, name, s));
						}
					}
				}
				
				return grades;
			}
		}
		
		return null;
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
		
		public A getStudent() {
			return elementA;
		}
		
		public B getCourse() {
			return elementB;
		}
		
		public C getDoubleGrade() {
			return elementC;
		}
		
		public String toString() {
			return elementA + " got the grade " + elementC + " at the course " + elementB;
		}
		
		@SuppressWarnings("unchecked")
		public boolean equals(Object obj) {
			Tuple<A, B, C> tuple = (Tuple<A, B, C>) obj;
			
			if (this.getStudent().equals(tuple.getStudent()) && this.getCourse().equals(tuple.getCourse()) &&
					this.getDoubleGrade().equals(tuple.getDoubleGrade())) {
				return true;
			} else {
				return false;
			}
		}
		
		public int hashCode() {
			if (this.getStudent() == null ^ this.getCourse() == null ^ this.getDoubleGrade() == null) {
				return 0;
			} else {
				return this.getStudent().hashCode() ^ this.getCourse().hashCode() ^ this.getDoubleGrade().hashCode();
			}
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

class Notification implements Observer {
	private Grade grade;
	private Parent mother;
	private Parent father;
	public String courseName;
	
	public Notification(String courseName, Grade grade, Parent mother, Parent father) {
		this.courseName = courseName;
		this.grade = grade;
		this.mother = mother;
		this.father = father;
	}
	
	public Grade getGrade() {
		return grade;
	}
	
	public Parent getMother() {
		return mother;
	}
	
	public Parent getFather() {
		return father;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	public void setCourseName(String course) {
		this.courseName = course;
	}
	
	public void setMother(Parent mom) {
		this.mother = mom;
	}
	
	public void setFather(Parent dad) {
		this.father = dad;
	}
	
	public String toString() {
		String str = "";
		str += "Dear Mr. " + father.getFirstName() + " " + father.getLastName() + " and Ms. " + mother.getFirstName() 
			+ " " + mother.getLastName() + ", we announce you that your son/daughter has received the grade " + grade.getTotal() + 
			" at the " + courseName + " subject";
		
		return str;
	}

	@Override
	public void update(Notification notification) {
//		System.out.println(notification);
	}
	
	public Notification updateParent(Notification notification) {
		return notification;
	}
	
	public boolean equals(Object obj) {
		Notification notification = (Notification) obj;
		
		if (this.father.getFirstName().equals(notification.father.getFirstName()) && this.father.getLastName()
				.equals(notification.father.getLastName()) && this.mother.getFirstName().equals(notification
						.mother.getFirstName()) && this.mother.getLastName().equals(notification.mother.getLastName())) {
			return true;
		}
		
		return false;
	}
}