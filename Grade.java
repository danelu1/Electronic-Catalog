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