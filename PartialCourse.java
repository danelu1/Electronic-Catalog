import java.util.ArrayList;

class PartialCourse extends Course {
	public PartialCourse(Course.CourseBuilder builder) {
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
		public Course build() {
			return new PartialCourse(this);
		}
	}
}