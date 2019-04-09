package comp3111.coursescraper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
	
	public String getInstructorString()	//Used for echo checking if instructors are read correctly
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
	
	public boolean isValidSection()
	{
		//By definition the section is invalid if a section isn't LX, LAX or TX
		if (code.substring(0, 1).equals("L") || code.substring(0, 1).equals("T") || code.substring(1, 2).equals("LA"))
			return true;
		return false;
	}
	
	public boolean isBusyAt(int day, String time)
	{
		LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mma", Locale.US));	//Parse the time parameter into LocalTime for comparison
		for (int i = 0; i < numSlots; i++)
		{
			if (slots[i].getDay() == day)
				if (!(slots[i].getStart().isAfter(parsedTime) || !slots[i].getEnd().isBefore(parsedTime)))	//If start <= time and end >= time, then this time slot is busy
					return true;
		}
		return false;
	}
	
}
