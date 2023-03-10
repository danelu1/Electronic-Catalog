import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class Catalog implements Subject {
	private static Catalog single_instance = null;
	List<Course> courses;
	List<Observer> observers;
	List<User> users;
	List<Grade> grades;
	
	private Catalog() {
		courses = new ArrayList<>();
		observers = new ArrayList<>();
		users = new ArrayList<>();
		grades = new ArrayList<>();
	}
	
	public static Catalog getInstance() {
		if (single_instance == null) {
			single_instance = new Catalog();
		}
		return single_instance;
	}
	
	public void testParse(String path) throws org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		
		try (Reader reader = new FileReader(path)) {
			JSONObject coursesJSONObject = (JSONObject) parser.parse(reader);
			JSONArray coursesArray = (JSONArray) coursesJSONObject.get("courses");
			
			for (int i = 0; i < coursesArray.size(); i++) {
				JSONObject arrayEntry = (JSONObject) coursesArray.get(i);
				
				String courseName = (String) arrayEntry.get("name");
				String courseCredits = (String) arrayEntry.get("credits");
				
				JSONObject teacher = (JSONObject) arrayEntry.get("teacher");
				String teacherFirstName = (String) teacher.get("firstName");
				String teacherLastName = (String) teacher.get("lastName");
				Teacher courseTeacher = (Teacher) UserFactory.getUser("Teacher", teacherFirstName, teacherLastName);
				
				Set<Assistant> assistants = new LinkedHashSet<>();
				Map<String, Group> map = new LinkedHashMap<>();
				
				JSONArray assistantsArray = (JSONArray) arrayEntry.get("assistants");
				
				
				for (int j = 0; j < assistantsArray.size(); j++) {
					JSONObject assistant = (JSONObject) assistantsArray.get(j);
					String assistantFirstName = (String) assistant.get("firstName");
					String assistantLastName = (String) assistant.get("lastName");
					Assistant assistant_aux = (Assistant) UserFactory.getUser("Assistant", assistantFirstName, assistantLastName);
					assistants.add(assistant_aux);
				}
				
				JSONArray groupsArray = (JSONArray) arrayEntry.get("groups");
				
				for (int j = 0; j < groupsArray.size(); j++) {
					JSONObject group = (JSONObject) groupsArray.get(j);
					
					String id = (String) group.get("ID");
					
					JSONObject assistant = (JSONObject) group.get("assistant");
					String assistantFirstName = (String) assistant.get("firstName");
					String assistantLastName = (String) assistant.get("lastName");
					Assistant assistant_aux = (Assistant) UserFactory.getUser("Assistant", assistantFirstName, assistantLastName);
					
					Group group_aux = new Group(id, assistant_aux);
					
					JSONArray students = (JSONArray) group.get("students");
					
					for (int k = 0; k < students.size(); k++) {
						JSONObject student = (JSONObject) students.get(k);
						String firstName = (String) student.get("firstName");
						String lastName = (String) student.get("lastName");
						
						Student s = (Student) UserFactory.getUser("Student", firstName, lastName);
						
						JSONObject motherObject = (JSONObject) student.get("mother");
						JSONObject fatherObject = (JSONObject) student.get("father");
						
						if (motherObject != null) {
							String motherFirstName = (String) motherObject.get("firstName");
							String motherLastName = (String) motherObject.get("lastName");
							Parent mother = (Parent) UserFactory.getUser("Parent", motherFirstName, motherLastName);
							s.setMother(mother);
						}
						
						if (fatherObject != null) {
							String fatherFirstName = (String) fatherObject.get("firstName");
							String fatherLastName = (String) fatherObject.get("lastName");
							Parent father = (Parent) UserFactory.getUser("Parent", fatherFirstName, fatherLastName);
							s.setFather(father);
						}
						
						group_aux.add(s);
					}
					map.put(id, group_aux);
				}
				
				String strategyObject = (String) arrayEntry.get("strategy");
				
				Strategy strategy = null;
				
				if (strategyObject.equals("BestPartialScore")) {
					strategy = new BestPartialScore();
				} else if (strategyObject.equals("BestExamScore")) {
					strategy = new BestExamScore();
				} else if (strategyObject.equals("BestTotalScore")) {
					strategy = new BestTotalScore();
				}
				
				ArrayList<Grade> courseGrades = new ArrayList<>();
				
				for (Grade grade : grades) {
					if (grade.getCourse().equals(courseName)) {
						courseGrades.add(grade);
					}
				}
				
				String courseType = (String) arrayEntry.get("type");
				
				if (courseType.equals("FullCourse")) {
					Course course = new FullCourse.FullCourseBuilder(courseName, Integer.parseInt(courseCredits))
							.setCourseAssistants(assistants)
							.setMap(map)
							.setTeacher(courseTeacher)
							.setStrategy(strategy)
							.setGrades(courseGrades)
							.build();
					courses.add(course);
				} else if (courseType.equals("PartialCourse")) {
					Course course = new PartialCourse.PartialCourseBuilder(courseName, Integer.parseInt(courseCredits))
							.setCourseAssistants(assistants)
							.setMap(map)
							.setTeacher(courseTeacher)
							.setStrategy(strategy)
							.setGrades(courseGrades)
							.build();
					courses.add(course);
				}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		ArrayList<ArrayList<Grade>> result = new ArrayList<ArrayList<Grade>>();
		
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
			
			result.add(grades_aux);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public String addStudent(String loginPath, String path, Course course, String id) throws ParseException, org.json.simple.parser.ParseException, FileNotFoundException, IOException {
		String info = "";
		
		JSONParser parser = new JSONParser();
		
		try (Reader reader = new FileReader("./test/studentsLogin.json")) {
			JSONObject studentsLogin = (JSONObject) parser.parse(reader);
			JSONArray studentsArray = (JSONArray) studentsLogin.get("login_details");
			
			try (Reader reader_aux = new FileReader(loginPath)) {
				JSONObject studentObject = (JSONObject) parser.parse(reader_aux);
				
				String firstName = (String) studentObject.get("first_name");
				String lastName = (String) studentObject.get("last_name");
				String userName = (String) studentObject.get("user_name");
				String userPassword = (String) studentObject.get("user_password");
				String icon = (String) studentObject.get("icon");
		
				Student s = (Student) UserFactory.getUser("Student", firstName, lastName);
				s.setIcon(icon);
				s.setUserName(userName);
				s.setUserPassword(userPassword);
				
				course.addStudent(id, s);
				users.add(s);
				
				if (!studentsArray.contains(studentObject)) {
					studentsArray.add(studentObject);
					studentsLogin.put("login_details", studentsArray);
				} else {
					studentsLogin.put("login_details", studentsArray);
				}
				
				PrintWriter writer = new PrintWriter(new FileWriter("./test/studentsLogin.json"));
				
				writer.write(studentsLogin.toJSONString());
				writer.close();
			}
		}
		
		try (Reader reader = new FileReader("./test/courses.json")) {
			JSONObject coursesObject = (JSONObject) parser.parse(reader);
			JSONArray coursesArray = (JSONArray) coursesObject.get("courses");
			
			try (Reader reader_aux = new FileReader(path)) {
				JSONObject studentObject = (JSONObject) parser.parse(reader_aux);
				
				for (int i = 0; i < coursesArray.size(); i++) {
					JSONObject courseObject = (JSONObject) coursesArray.get(i);
					String courseName = (String) courseObject.get("course_name");
					JSONArray groupsArray = (JSONArray) courseObject.get("groups");
					
					if (courseName.equals(course.getCourseName())) {
						for (int j = 0; j < groupsArray.size(); j++) {
							JSONObject group = (JSONObject) groupsArray.get(j);
							
							String ID = (String) group.get("ID");
							
							JSONArray studentsArray = (JSONArray) group.get("students");
							
							if (ID.equals(id)) {
								studentsArray.add(studentObject);
								groupsArray.remove(group);
								group.put("students", studentsArray);
								groupsArray.add(group);
							}
						}
					}
					coursesArray.remove(courseObject);
					courseObject.put("groups", groupsArray);
					coursesArray.add(courseObject);
				}
				
				coursesObject.put("courses", coursesArray);
				
				PrintWriter writer = new PrintWriter(new FileWriter("./test/courses.json"));
				writer.write(coursesObject.toJSONString());
				writer.close();
			}
		}
		
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
		
		info += courseInformations + courseTeacher + courseCredits + courseAssistants + groups;
		return info;
	}
	
	public String addAssistant(String path, Course course) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
		String info = "";
		
		JSONParser parser = new JSONParser();
		
		try (Reader reader = new FileReader(path)) {
			JSONObject studentObject = (JSONObject) parser.parse(reader);
			
			String firstName = (String) studentObject.get("first_name");
			String lastName = (String) studentObject.get("last_name");
	
			Assistant a = new Assistant(firstName, lastName);
			Set<Assistant> assistants = course.getCourseAssistants();
			assistants.add(a);
		}
		
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
		
		info += courseInformations + courseTeacher + courseCredits + courseAssistants + groups;
		return info;
	}
	
	public String addGroup(String path, Course course) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
		String info = "";
		
		JSONParser parser = new JSONParser();
		
		try (Reader reader = new FileReader(path)) {
			JSONObject groupObject = (JSONObject) parser.parse(reader);	
			
			String groupID = (String) groupObject.get("ID");
			JSONObject assistant = (JSONObject) groupObject.get("assistant");
			String assistantFirstName = (String) assistant.get("first_name");
			String assistantLastName = (String) assistant.get("last_name");
			Assistant groupAssistant = new Assistant(assistantFirstName, assistantLastName);
			
			Group group = new Group(groupID, groupAssistant);
			
			JSONArray studentsArray = (JSONArray) groupObject.get("students");
			
			for (int i = 0; i < studentsArray.size(); i++) {
				JSONObject studentObject = (JSONObject) studentsArray.get(i);
				String firstName = (String) studentObject.get("first_name");
				String lastName = (String) studentObject.get("last_name");
				Student student = new Student(firstName, lastName);
				group.add(student);
				
				Map<String, Group> map = course.getGroup();
				map.put(groupID, group);
			}
			
			Set<Assistant> set = course.getCourseAssistants();
			set.add(groupAssistant);
		}
		
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
		
		info += courseInformations + courseTeacher + courseCredits + courseAssistants + groups;
		
		return info;
	}
	
	@SuppressWarnings("unchecked")
	public String addGrade(String path, Course course) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
		String info = "";
		
		JSONParser parser = new JSONParser();
		
		try (Reader reader_aux = new FileReader("./test/grades.json")) {
			JSONObject gradesObject = (JSONObject) parser.parse(reader_aux);
			JSONArray gradesArray = (JSONArray) gradesObject.get("grades");
			
			try (Reader reader = new FileReader(path)) {
				JSONObject gradeObject = (JSONObject) parser.parse(reader);
				String partialScore = (String) gradeObject.get("partial_score");
				String examScore = (String) gradeObject.get("exam_score");
				String name = (String) gradeObject.get("course_name");
				
				JSONObject student = (JSONObject) gradeObject.get("student");
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
				
				Grade grade = new Grade(Double.parseDouble(partialScore), Double.parseDouble(examScore), name, s);
				Notification notification = new Notification(name, grade, s.getMother(), s.getFather());
				
				grades.add(grade);
				observers.add(notification);
				
				if (!gradesArray.contains(gradeObject)) {
					gradesArray.add(gradeObject);
				}
			}
			gradesObject.put("grades", gradesArray);
			
			PrintWriter writer = new PrintWriter("./test/grades.json");
			writer.write(gradesObject.toJSONString());
			writer.close();
		}
		
		for (int i = 0; i < grades.size(); i++) {
			info += grades.get(i) + "\n";
		}
		
		return info;
	}
	
	public void gradesParseJSON(String path) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		
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
				Teacher courseTeacher = (Teacher) UserFactory.getUser("Teacher", teacherFirstName, teacherLastName);
				
				Set<Assistant> assistants = new LinkedHashSet<>();
				Map<String, Group> map = new LinkedHashMap<>();
				
				JSONArray assistantsArray = (JSONArray) arrayEntry.get("course_assistants");
				
				
				for (int j = 0; j < assistantsArray.size(); j++) {
					JSONObject assistant = (JSONObject) assistantsArray.get(j);
					String assistantFirstName = (String) assistant.get("first_name");
					String assistantLastName = (String) assistant.get("last_name");
					Assistant assistant_aux = (Assistant) UserFactory.getUser("Assistant", assistantFirstName, assistantLastName);
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
					Assistant assistant_aux = (Assistant) UserFactory.getUser("Assistant", assistantFirstName, assistantLastName);
					
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
						Parent mother = (Parent) UserFactory.getUser("Parent", motherFirstName, motherLastName);
						Parent father = (Parent) UserFactory.getUser("Parent", fatherFirstName, fatherLastName);
				
						Student s = (Student) UserFactory.getUser("Student", firstName, lastName);
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
				
				ArrayList<Grade> courseGrades = new ArrayList<>();
				
				for (Grade grade : grades) {
					if (grade.getCourse().equals(courseName)) {
						courseGrades.add(grade);
					}
				}
				
				String courseType = (String) arrayEntry.get("course_type");
				
				if (courseType.equals("full")) {
					Course course = new FullCourse.FullCourseBuilder(courseName, Integer.parseInt(courseCredits))
							.setCourseAssistants(assistants)
							.setMap(map)
							.setTeacher(courseTeacher)
							.setStrategy(strategy)
							.setGrades(courseGrades)
							.build();
					courses.add(course);
				} else if (courseType.equals("partial")) {
					Course course = new PartialCourse.PartialCourseBuilder(courseName, Integer.parseInt(courseCredits))
							.setCourseAssistants(assistants)
							.setMap(map)
							.setTeacher(courseTeacher)
							.setStrategy(strategy)
							.setGrades(courseGrades)
							.build();
					courses.add(course);
				}
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
					Student student_aux = (Student) UserFactory.getUser("Student", firstName, lastName);
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
					Assistant assistant_aux = (Assistant) UserFactory.getUser("Assistant", firstName, lastName);
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
					Teacher teacher_aux = (Teacher) UserFactory.getUser("Teacher", firstName, lastName);
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
					Parent parent_aux = (Parent) UserFactory.getUser("Parent", firstName, lastName);
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
		new SelectionPage("Select");
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