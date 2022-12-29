import java.util.Comparator;
import java.util.TreeSet;

class Group extends TreeSet<Student> {
	private static final long serialVersionUID = 1L;
	
	private Assistant assistant;
	private String ID;
	
	public Group(String ID, Assistant assistant, Comparator<Student> comp) {
		super(comp);
		this.ID = ID;
		this.assistant = assistant;
	}
	
	public Group(String ID, Assistant assistant) {
		this(ID, assistant, null);
	}
	
	public Assistant getAssistant() {
		return assistant;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setAssistant(Assistant a) {
		assistant = a;
	}
	
	public void setID(String toSet) {
		ID = toSet;
	}
}