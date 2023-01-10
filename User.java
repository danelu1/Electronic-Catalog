abstract class User {
	private String firstName;
	private String lastName;
	private String userName;
	private String userPassword;
	private String icon;
	
	public User(String firstName, String lastName, String userName, String userPassword, String icon) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public User(String firstName, String lastName) {
		this(firstName, lastName, null, null, null);
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setFirstName(String name) {
		this.firstName = name;
	}
	
	public void setLastName(String name) {
		this.lastName = name;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String toString() {
		return firstName + " " + lastName;
	}
	
	public boolean equals(Object obj) {
		User user = (User) obj;
		
		if (this.getFirstName().equals(user.getFirstName()) && this.getLastName().equals(user.getLastName())) {
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		if (this.getFirstName() == null ^ this.getLastName() == null) {
			return 0;
		} else {
			return this.getFirstName().hashCode() ^ this.getLastName().hashCode();
		}
	}
}