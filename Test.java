import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;

class Test {
	public static void main(String[] args) {
		Catalog catalog = Catalog.getInstance();
		try {
			catalog.open();
		} catch (java.text.ParseException | ParseException | IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			catalog.testParse("./test/Test.json");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		TestVisitor visitor = new TestVisitor();
		
		System.out.println("Catalog:");
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			Course course = catalog.courses.get(i);
			System.out.println("\t- Course name: " + course.getCourseName());
			System.out.println("\t- Course credits: " + course.getCourseCredits());
			System.out.println("\t- Course teacher: " + course.getCourseTeacher());
			System.out.println("\t- Course assistants:");
			
			Iterator<Assistant> it = course.getCourseAssistants().iterator();
			
			while (it.hasNext()) {
				Assistant a = it.next();
				System.out.println("\t\t-> " + a.getFirstName() + " " + a.getLastName());
			}
			
			ArrayList<Student> students = new ArrayList<>();
			
			System.out.println("\t- Course groups:");
			
			Map<String, Group> groups = course.getGroup();
			
			for (Map.Entry<String, Group> group : groups.entrySet()) {
				System.out.println("\t\t- ID: " + group.getKey());
				System.out.println("\t\t- Assistant: " + group.getValue().getAssistant().getFirstName() + " " +
				group.getValue().getAssistant().getLastName());
				System.out.println("\t\t- Students:");
				
				Iterator<Student> itr = group.getValue().iterator();
				
				while (itr.hasNext()) {
					Student s = itr.next();
					System.out.println("\t\t\t-> " + s.getFirstName() + " " + s.getLastName());
					students.add(s);
				}
			}
			System.out.println();
		}
		
		ArrayList<ArrayList<Assistant>> assistants = new ArrayList<>();
		ArrayList<Teacher> teachers = new ArrayList<>();
		
		for (int i = 0; i < catalog.courses.size(); i++) {
			Course course = catalog.courses.get(i);
			
			Iterator<Assistant> it = course.getCourseAssistants().iterator();
			ArrayList<Assistant> list = new ArrayList<>();
			while (it.hasNext()) {
				list.add(it.next());
			}
			assistants.add(list);
			teachers.add(course.getCourseTeacher());
		}
	
		for (int i = 0; i < assistants.size(); i++) {
			for (int j = 0; j < assistants.get(i).size(); j++) {
				assistants.get(i).get(j).accept(visitor);
			}
			teachers.get(i).accept(visitor);
		}
		
		LinkedHashSet<Course> coursesSet = new LinkedHashSet<>();
		for (Course course : catalog.courses) {
			coursesSet.add(course);
		}
		
		List<Course> courses = new ArrayList<>();
		Iterator<Course> it = coursesSet.iterator();
		while (it.hasNext()) {
			courses.add(it.next());
		}
		
		System.out.println("\t- Grades:");
		
		for (int i = 0; i < courses.size(); i++) {
			Course course = courses.get(i);
			LinkedHashMap<Student, Grade> situation = course.getAllStudentGrades();
			System.out.println("\t\t- " + course.getCourseName() + ":");
			for (Map.Entry<Student, Grade> grade : situation.entrySet()) {
				Grade g = grade.getValue();
				System.out.println("\t\t\t- Student " + g.getStudent().getFirstName() + " " + g.getStudent().getLastName());
				System.out.println("\t\t\t\t" + g.printPartialScore());
				System.out.println("\t\t\t\t" + g.printExamScore());
				System.out.println("\t\t\t\t" + g.printTotalScore());
			}
			System.out.println();
		}
		
		System.out.println("\t- Graduated students:");
		
		for (int i = 0; i < courses.size(); i++) {
			ArrayList<Student> graduatedStudents = courses.get(i).getGraduatedStudents();
			System.out.println("\t\t- " + courses.get(i).getCourseName() + ":");
			for (Student s : graduatedStudents) {
				System.out.println("\t\t\t-> " + s.getFirstName() + " " + s.getLastName());
			}
		}
		
		System.out.println("\t- Best students:");
		
		for (int i = 0; i < courses.size(); i++) {
			System.out.println("\t\t- " + courses.get(i).getCourseName() + ":");
			Student s = courses.get(i).getBestStudent();
			System.out.println("\t\t\t-> " + s.getFirstName() + " " + s.getLastName());
		}
		
		Course course = catalog.courses.get(1);
		try {
			course.makeBackup();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		course.getGrades().add(new Grade(3.4, 2.5, course.getCourseName(), new Student("Marian", "Andreica")));
		course.getGrades().add(new Grade(1.34, 2.42, course.getCourseName(), new Student("Alin", "Cernobil")));
		
		System.out.println("\t- Grades after modifications:");
		
		for (Grade grade : course.getGrades()) {
			System.out.println("\t\t- Student " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName());
			System.out.println("\t\t\t" + grade.printPartialScore());
			System.out.println("\t\t\t" + grade.printExamScore());
			System.out.println("\t\t\t" + grade.printTotalScore());
		}
		
		course.undo();
		
		System.out.println("\t- Grades after undo:");
		
		for (Grade grade : course.getGrades()) {
			System.out.println("\t\t- Student " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName());
			System.out.println("\t\t\t" + grade.printPartialScore());
			System.out.println("\t\t\t" + grade.printExamScore());
			System.out.println("\t\t\t" + grade.printTotalScore());
		}
		
		System.out.println();
		
		course.addAssistant("315CC", (Assistant) UserFactory.getUser("Assistant", "Mihaita", "Pufuletdulce"));
		
		Iterator<Assistant> itr = course.getCourseAssistants().iterator();
		
		System.out.println("\t- Course assistants after adding an assistant:");
		
		while (itr.hasNext()) {
			System.out.println("\t\t" + itr.next());
		}
		
		System.out.println();
		
		course.addStudent("314CC", (Student) UserFactory.getUser("Student", "Eustache", "Traista"));
		
		Iterator<Student> group = course.getGroup().get("314CC").iterator();
		
		System.out.println("\t- The group 314CC after adding a student:");
		
		while (group.hasNext()) {
			System.out.println("\t\t" + group.next());
		}
		
	}
}
