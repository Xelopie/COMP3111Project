package comp3111.coursescraper;

public class Course {
	private static final int DEFAULT_MAX_SECTION = 35;	//COMP4900 has 33 valid sections
	
	private String title ; 
	private String description ;
	private String exclusion;
	private String attribute;
	private Section[] sections;
	private int numSections;
	
	public Course() {
		sections = new Section[DEFAULT_MAX_SECTION];
		for (int i = 0; i < DEFAULT_MAX_SECTION; i++)
		{
			sections[i] = null;
		}
		numSections = 0;
	}
	
	public void addSection(Section s)
	{
		if (numSections >= DEFAULT_MAX_SECTION)
			return;
		sections[numSections++] = s.clone();
	}
	
	public Section getSection(int i)
	{
		if (i >= 0 && i < numSections)
			return sections[i];
		return null;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the exclusion
	 */
	public String getExclusion() {
		return exclusion;
	}

	/**
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}

	/**
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}
	
	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	public int getNumSections() { return numSections; }
	public void setNumSections(int numSections) { this.numSections = numSections; }
	

	public boolean isValidCourse() { return (getNumValidSections() > 0? true: false); }	//A course has to have at least 1 valid section

	/* Helper functions for filter (Task 2) */
	
	public boolean isCC4Y() {
		if (attribute.contains("Common Core") && attribute.contains("4Y")) {
			return true;
		}
		return false;
	}
	
	public boolean isNoEx() {
		if (exclusion.contains("null")) {
			return true;
		}
		return false;
	}
	
	public boolean containsAMPMSection() {
		for (int i = 0; i < numSections; i++) {
			if (sections[i].containsAMPMSlot())
				return true;
		}
		return false;
	}
	
	public boolean containsAMSection() {
		for (int i = 0; i < numSections; i++) {
			if (sections[i].containsAMSlot())
				return true;
		}
		return false;
	}
	
	public boolean containsPMSection() {
		for (int i = 0; i < numSections; i++) {
			if (sections[i].containsPMSlot())
				return true;
		}
		return false;
	}
	
	public boolean[] containsDaySection() {
		boolean[] bContainsDaySection = new boolean[6];
		for (int i = 0; i < numSections; i++) {
			for (int k = 0; k < 6; k++) {
				bContainsDaySection[k] |= sections[i].containsDaySlot()[k];
			}
		}
		return bContainsDaySection;
	}
	
	public boolean containsLabOrTut() {
		for (int i = 0; i < numSections; i++) {
			if (sections[i].getCode().contains("LA") || sections[i].getCode().contains("T"))
				return true;
		}
		return false;
	}


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
