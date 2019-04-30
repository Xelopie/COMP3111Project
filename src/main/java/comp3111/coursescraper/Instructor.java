package comp3111.coursescraper;

/**
 * Stores only the name of instructor currently.
 */

public class Instructor
{
	private String name;
	
	/**
	 * Overrides the shallow copy default clone().
	 * @return An instructor item clone of itself by deep copy
	 */
	@Override
	public Instructor clone()
	{
		Instructor i = new Instructor();
		i.name = this.name;
		return i;
	}
	
	/**
	 * Can be called implicitly when slot is needed as String.
	 * @return the instructor in String
	 */
	public String toString() { return name; }
	
	/**
	 * Returns the name.
	 * @return the name in String
	 */
	public String getName() { return name; }
	
	/**
	 * Sets the name.
	 * @param s the name to set
	 */
	public void setName(String s) { name = s; }
	
}