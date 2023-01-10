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
		if (mother != null && father == null) {
			str += "Dear Ms. " + mother.getFirstName() + " " + mother.getLastName() +
					", we announce you that your son/daughter has received the grade " + grade.getTotal() + 
					" at the " + courseName + " subject";
		} else if (mother == null && father != null) {
			str += "Dear Mr. " + father.getFirstName() + " " + father.getLastName() + 
					", we announce you that your son/daughter has received the grade " + grade.getTotal() + 
					" at the " + courseName + " subject";
		} else if (mother != null && father != null) {
			str += "Dear Mr. " + father.getFirstName() + " " + father.getLastName() + " and Ms. " + mother.getFirstName() 
			+ " " + mother.getLastName() + ", we announce you that your son/daughter has received the grade " + grade.getTotal() + 
			" at the " + courseName + " subject";
		} else {
			str += "You received the grade " + grade.getTotal() + " at the " + courseName + " subject";
		}
		
		return str;
	}

	@Override
	public void update(Notification notification) {
		notification.getGrade().getStudent().notifications.add(notification);
		System.out.println(notification);
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