import java.util.ArrayList;

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