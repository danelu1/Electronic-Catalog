import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

abstract class Course {
	private String courseName;
	private Teacher courseTeacher;
	private Set<Assistant> courseAssistants;
	private ArrayList<Grade> grades;
	private Map<String, Group> map;
	private int courseCredits;
	private String courseType;
	private Snapshot snapshot;
	private Strategy strategy;
	private int typeOfStrategy;
	
	public Course(CourseBuilder builder) {
		this.courseName = builder.courseName;
		this.courseTeacher = builder.courseTeacher;
		this.courseAssistants = builder.courseAssistants;
		this.grades = builder.grades;
		this.map = builder.map;
		this.courseCredits = builder.courseCredits;
		this.strategy = builder.strategy;
		this.courseType = builder.courseType;
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
	
	public int getStrategyType() {
		return typeOfStrategy;
	}
	
	public String getCourseType() {
		return courseType;
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
	
	public void setStrategyType(int type) {
		this.typeOfStrategy = type;
	}
	
	public void setCourseType(String type) {
		this.courseType = type;
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
		addGroup(group);
	}
	
	public void addGroup(String ID, Assistant assistant, Comparator<Student> comp) {
		Group group = new Group(ID, assistant, comp);
		addGroup(group);
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
		LinkedHashMap<Student, Grade> result = new LinkedHashMap<>();
		
		for (int i = 0; i < grades.size(); i++) {
			result.put(grades.get(i).getStudent(), grades.get(i));
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
		private String courseType;
		
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
		
		public CourseBuilder setCourseType(String type) {
			this.courseType = type;
			return this;
		}
		
		abstract Course build();
	}
	
	public Student getBestStudent() {	
		if (typeOfStrategy == 1) {
			strategy = new BestPartialScore();
		} else if (typeOfStrategy == 2) {
			strategy = new BestExamScore();
		} else if (typeOfStrategy == 3) {
			strategy = new BestTotalScore();
		}
		
		Grade score = strategy.getBestScore(grades);
		
		return score.getStudent();
	}
	
	private class Snapshot {
		private ArrayList<Grade> backup;
		
		public Snapshot() {
			backup = new ArrayList<>();
		}
	}
	
	public void makeBackup() throws CloneNotSupportedException {
		snapshot = new Snapshot();
		
		for (Grade grade : grades) {
			snapshot.backup.add(grade.clone());
		}
	}
	
	public void undo() {
		this.grades = snapshot.backup;
	}
	
	public String toString() {
		String ans = "";
		ans += "-> " + courseName + "\n";
		
		return ans;
	}
	
	public boolean equals(Object obj) {
		Course c = (Course) obj;
		if (c.getCourseName().equals(this.getCourseName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		if (this.getCourseName() == null) {
			return 0;
		} else {
			return this.getCourseName().hashCode();
		}
	}
}