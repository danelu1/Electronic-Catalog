class Assistant extends User implements Element {
	public Assistant(String firstName, String lastName, String userName, String userPassword, String icon) {
		super(firstName, lastName, userName, userPassword, icon);
	}
	
	public Assistant(String firstName, String lastName) {
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
	
	public String toString() {
		String ans = "-> ";
		ans += this.getFirstName() + " " + this.getLastName();
		
		return ans;
	}
}