package comp3111.coursescraper;

public class Section {
	
	private String id;
	private String code;
	private boolean enrollStatus = false;

	@Override
	public Section clone()
	{
		Section s = new Section();
		s.id = this.id;
		s.code = this.code;
		s.enrollStatus = this.enrollStatus;
		return s;
	}
	
	public String toString()
	{
		return " " + code + " (" + id +  ")";
	}
	
	public void setID(String i) { id = i; }
	public String getID() { return id; }
	
	public void setCode(String str) { code = str; }
	public String getCode() { return code; }
	
	public void setEnrollStatus(boolean bool) {	enrollStatus = bool; }
	public boolean getEnrollStatus() { return enrollStatus; }
	
}
