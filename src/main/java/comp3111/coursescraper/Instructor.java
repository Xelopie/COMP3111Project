package comp3111.coursescraper;

public class Instructor
{
	private String name;
	
	@Override
	public Instructor clone()
	{
		Instructor i = new Instructor();
		i.name = this.name;
		return i;
	}
	
	public String toString() { return name; }
	
	public String getName() { return name; }
	public void setName(String s) { name = s; }
	
}