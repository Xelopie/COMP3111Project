package comp3111.coursescraper;

/**
 * Stores the information of a course.
 */

public class Course {
	private static final int DEFAULT_MAX_SECTION = 75;	//LANG1003S has 73 valid sections
	
	private String title ; 
	private String description ;
	private String exclusion;
	private String attribute;
	private Section[] sections;
	private int numSections;
	
	/**
	 * Default constructor. Initializes the static arrays for Section objects.
	 */
	public Course() {
		sections = new Section[DEFAULT_MAX_SECTION];
		for (int i = 0; i < DEFAULT_MAX_SECTION; i++)
		{
			sections[i] = null;
		}
		numSections = 0;
	}
	
	/**
	 * Adds a section to sections[]
	 * @param s the section to add
	 */
	public void addSection(Section s)
	{
		if (numSections >= DEFAULT_MAX_SECTION)
			return;
		sections[numSections++] = s.clone();
	}
	
	/**
	 * Returns a section[] item specified by the index
	 * @param i the index of the section item in the array
	 * @return the section[i] item
	 */
	public Section getSection(int i)
	{
		if (i >= 0 && i < numSections)
			return sections[i];
		return null;
	}
	
	/**
	 * Returns the course title.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the course description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the course description.
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the course exclusion.
	 * @return the exclusion
	 */
	public String getExclusion() {
		return exclusion;
	}

	/**
	 * Sets the course exclusion.
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}

	/**
	 * Returns the course attribute.
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}
	
	/**
	 * Sets the course attribute.
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	/**
	 * Returns the number of sections.
	 * @return the numSections
	 */
	public int getNumSections() { return numSections; }
	/**
	 * Sets the numSections
	 * @param numSections the numSections to set
	 */
	public void setNumSections(int numSections) { this.numSections = numSections; }
	
	/**
	 * Returns a boolean value on whether the course is valid. A valid course has at least 1 valid section.
	 * @return boolean - is the course valid
	 */
	public boolean isValidCourse() { return (getNumValidSections() > 0? true: false); }	//A course has to have at least 1 valid section

	/* Helper functions for filter (Task 2) */
	
	/**
	 * Determine if the course satisfies the filter condition (Common Core)
	 * @return true if the course is a common core
	 */
	public boolean isCC4Y() {
		if (attribute.contains("Common Core") && attribute.contains("4Y")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determine if the course satisfies the filter condition (No exclusion)
	 * @return true if the course has no exclusion
	 */
	public boolean isNoEx() {
		if (exclusion.contains("null")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Determine if the course satisfies the filter condition (AM/PM)
	 * @return true if the course contains both AM & PM slots
	 * @return true if the course contains a slot starting at AM and ending at PM
	 */
	public boolean containsAMPMSection() {
		for (int i = 0; i < numSections; i++) {
			if (sections[i].containsAMPMSlot())
				return true;
		}
		return false;
	}
	
	/**
	 * Determine if the course satisfies the filter condition (AM)
	 * @return true if the course contains a AM slot
	 */
	public boolean containsAMSection() {
		for (int i = 0; i < numSections; i++) {
			if (sections[i].containsAMSlot())
				return true;
		}
		return false;
	}
	
	/**
	 * Determine if the course satisfies the filter condition (PM)
	 * @return true if the course contains a PM slot
	 */
	public boolean containsPMSection() {
		for (int i = 0; i < numSections; i++) {
			if (sections[i].containsPMSlot())
				return true;
		}
		return false;
	}
	
	/**
	 * Determine if the course satisfies the filter condition (Day)
	 * @return boolean array of size 6, referring Mon to Sat 
	 */
	public boolean[] containsDaySection() {
		boolean[] bContainsDaySection = new boolean[6];
		for (int i = 0; i < numSections; i++) {
			for (int k = 0; k < 6; k++) {
				bContainsDaySection[k] |= sections[i].containsDaySlot()[k];
			}
		}
		return bContainsDaySection;
	}
	
	/**
	 * Determine if the course satisfies the filter condition (Contains Lab or Tutorial)
	 * @return true if the course contains Lab or tutorial
	 */
	public boolean containsLabOrTut() {
		for (int i = 0; i < numSections; i++) {
			if (sections[i].getCode().contains("LA") || sections[i].getCode().contains("T"))
				return true;
		}
		return false;
	}

	/**
	 * Returns the number of valid sections in this course.
	 * @return the number of valid sections in this course
	 */
	public int getNumValidSections()
	{
		int validCount = numSections;	//Using decrement strategy
		for (int i = 0; i < numSections; i++)
		{
			if (!sections[i].isValidSection())
				validCount--;
		}
		return validCount;
	}
	
}
