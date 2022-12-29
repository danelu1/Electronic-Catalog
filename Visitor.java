import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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