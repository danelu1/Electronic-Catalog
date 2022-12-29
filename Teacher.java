class Teacher extends User implements Element {
	public Teacher(String firstName, String lastName, String userName, String userPassword, String icon) {
		super(firstName, lastName, userName, userPassword, icon);
	}
	
	public Teacher(String firstName, String lastName) {
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
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	public boolean equals(Object obj) {
		Teacher t = (Teacher) obj;
		
		if (this.getFirstName().equals(t.getFirstName()) && this.getLastName().equals(t.getLastName())) {
			return true;
		} else {
			return false;
		}
	}
}