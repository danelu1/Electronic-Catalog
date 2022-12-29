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