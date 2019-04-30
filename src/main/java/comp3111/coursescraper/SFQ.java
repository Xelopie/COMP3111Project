package comp3111.coursescraper;
/**
 * 
 * Stores the Course title or instructors' name, and the score
 *
 */
public class SFQ {
	private String name;
	private double score = 0;
	/**
	 * Get the title of the course/ Instructor's name
	 * @return a String storing the course title
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get the score
	 * @return score in double
	 */
	public double getScore() {
		return score;
	}
	/**
	 * Set the course title/ Instuctor's name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Set the score
	 * @param score
	 */
	public void setScore(double score) {
		this.score = score;
	}
}
