import java.util.ArrayList;

class Student extends User implements Comparable<Student> {
	private Parent mother;
	private Parent father;
	ArrayList<Notification> notifications;
	
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
	
//	public boolean equals(Object obj) {
//		Student s = (Student) obj;
//		if (this.getFirstName().equals(s.getFirstName()) && this.getLastName().equals(s.getLastName())) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	public int hashCode() {
//		if (this.getFirstName() == null ^ this.getLastName() == null) {
//			return 0;
//		} else {
//			return this.getFirstName().hashCode() ^ this.getLastName().hashCode();
//		}
//	}
}