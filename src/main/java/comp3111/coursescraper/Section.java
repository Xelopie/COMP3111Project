package comp3111.coursescraper;

public class Section {
	private static final int DEFAULT_MAX_SLOT = 3;
	private static final int DEFAULT_MAX_INSTRUCTOR = 4;
	
	private String id;
	private String code;
	private boolean enrollStatus;
	private Slot [] slots;
	private int numSlots;
	private Instructor[] instructors;
	private int numInstructors;
	
	public Section()
	{
		slots = new Slot[DEFAULT_MAX_SLOT];
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++) slots[i] = null;
		numSlots = 0;
		
		instructors = new Instructor[DEFAULT_MAX_INSTRUCTOR];
		for (int i = 0; i < DEFAULT_MAX_INSTRUCTOR; i++) instructors[i] = null;
		numInstructors = 0;
	}
	
	@Override
	public Section clone()
	{
		Section sect = new Section();
		sect.id = this.id;
		sect.code = this.code;
		sect.enrollStatus = this.enrollStatus;
		sect.numSlots = this.numSlots;
		if (this.numSlots > 0)
		{
			for (Slot s: this.slots)
			{
				sect.addSlot(s);
			}
		}
		sect.numInstructors = this.numInstructors;
		if (this.numInstructors > 0)
		{
			for (Instructor i: this.instructors)
			{
				sect.addInstructor(i);
			}
		}
		return sect;
	}
	
	public void addSlot(Slot s) {
		if (numSlots >= DEFAULT_MAX_SLOT)
			return;
		slots[numSlots++] = s.clone();
	}
	
	public Slot getSlot(int i) {
		if (i >= 0 && i < numSlots)
			return slots[i];
		return null;
	}
	
	public void addInstructor(Instructor inst)
	{
		if (numInstructors >= DEFAULT_MAX_INSTRUCTOR)
			return;
		instructors[numInstructors++] = inst.clone();
	}
	
	public Instructor getInstructor(int i)
	{
		if (i >= 0 && i < numInstructors)
			return instructors[i];
		return null;
	}
	
	public String toString() { return code + " (" + id +  ")"; }
	
	public String getInstructorString()
	{
		String instString = "";
		for (int i = 0; i < numInstructors; i++)
		{
			instString += instructors[i] + "  ";
		}
		return instString;
	}
	
	/**
	 * @return the numSlots
	 */
	
	public int getNumSlots() { return numSlots; }

	/**
	 * @param numSlots the numSlots to set
	 */
	public void setNumSlots(int numSlots) { this.numSlots = numSlots; }
	
	public void setID(String s) { id = s; }
	public String getID() { return id; }
	
	public void setCode(String str) { code = str; }
	public String getCode() { return code; }
	
	public void setEnrollStatus(boolean bool) {	enrollStatus = bool; }
	public boolean getEnrollStatus() { return enrollStatus; }
	
	public void setNumInstructors(int i) { numInstructors = i; }
	public int getNumInstructors() { return numInstructors; }
	
}
