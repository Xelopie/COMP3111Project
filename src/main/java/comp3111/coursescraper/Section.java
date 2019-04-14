package comp3111.coursescraper;

import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;

/**
 * Stores the information of a section.
 */

public class Section {
	private static final int DEFAULT_MAX_SLOT = 3;
	private static final int DEFAULT_MAX_INSTRUCTOR = 4;
	
	private String id;
	private String code;
	private Slot [] slots;
	private int numSlots;
	private Instructor[] instructors;
	private int numInstructors;
	private CheckBox enroll;
		
	/**
	 * Default constructor. Initializes the static arrays for Slot and Instructor objects.
	 */
	public Section()
	{
		slots = new Slot[DEFAULT_MAX_SLOT];
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++) slots[i] = null;
		numSlots = 0;
		
		instructors = new Instructor[DEFAULT_MAX_INSTRUCTOR];
		for (int i = 0; i < DEFAULT_MAX_INSTRUCTOR; i++) instructors[i] = null;
		numInstructors = 0;
		enroll = new CheckBox("");
	}
	
	/**
	 * Overrides the shallow copy default clone().
	 * @return A section item clone of itself by deep copy
	 */
	@Override
	public Section clone()
	{
		Section sect = new Section();
		sect.id = this.id;
		sect.code = this.code;
		sect.enroll = this.enroll;
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
	
	/**
	 * Adds a slot to slots[]
	 * @param s the slot to add
	 */
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
	
	/**
	 * Adds an instructor to instructors[]
	 * @param inst the instructor to add
	 */
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
	
	/**
	 * Can be called implicitly when slot is needed as String.
	 * @return the instructor in String
	 */
	public String toString() { return code + " (" + id +  ")"; }
	
	/**
	 * Returns a String containing all instructor items
	 * @return all instructor items in this section object in String
	 */
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
	 * Returns the numSlots.
	 * @return the numSlots
	 */
	public int getNumSlots() { return numSlots; }

	/**
	 * Sets the numSlots.
	 * @param numSlots the number of slots to set
	 */
	public void setNumSlots(int numSlots) { this.numSlots = numSlots; }
	
	/**
	 * Sets the section ID.
	 * @param s the id to set
	 */
	public void setID(String s) { id = s; }
	/**
	 * Returns the course ID.
	 * @return the ID
	 */
	public String getID() { return id; }
	
	/**
	 * Sets the section code.
	 * @param s the code to set
	 */
	public void setCode(String s) { code = s; }
	/**
	 * Returns the course code.
	 * @return the code
	 */
	public String getCode() { return code; }
	
	/**
	 * Sets the numInstructors.
	 * @param i the number of instructors to set
	 */
	public void setNumInstructors(int i) { numInstructors = i; }
	/**
	 * Returns the numInstructors.
	 * @return the numInstructors
	 */
	public int getNumInstructors() { return numInstructors; }
	
	/**
	 * Returns the enroll.
	 * @return the enroll
	 */
	public CheckBox getEnroll() {
		return enroll;
	}
	
	/**
	 * Sets the enroll.
	 * @param enroll the CheckBox status to set
	 */
	public void setEnroll(CheckBox enroll) {
		this.enroll.setSelected(enroll.isSelected());
	}
	
	/**
	 * Returns a boolean value depending on whether the section is valid. A valid section contains "L", "LA", or "T" in the code.
	 * @return boolean - is the section valid
	 */
	public boolean isValidSection()
	{
		//By definition the section is invalid if a section isn't LX, LAX or TX
		if (code.substring(0, 1).equals("L") || code.substring(0, 1).equals("T") || code.substring(1, 2).equals("LA"))
			return true;
		return false;
	}
	
	/**
	 * Returns a boolean value depending on whether there are slots that covers the time parameters. Defined as start time is earlier or equal to the time, AND end time is equal to or after the time. 
	 * @param day the day being asked
	 * @param time the time being asked
	 * @return boolean - is the section busy at the time specified by the parameters
	 */
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
	
	
	/* Helper functions for filter (Task 2) */
	
	public boolean containsAMSlot() {
		for (int i = 0; i < numSlots; i++) {
			if (slots[i].getStart().isBefore(LocalTime.NOON)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsPMSlot() {
		for (int i = 0; i < numSlots; i++) {
			if (slots[i].getEnd().equals(LocalTime.NOON) || slots[i].getEnd().isAfter(LocalTime.NOON)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsAMPMSlot() {
		for (int i = 0; i < numSlots; i++) {
			if (slots[i].getStart().isBefore(LocalTime.NOON) && (slots[i].getEnd().equals(LocalTime.NOON) || slots[i].getEnd().isAfter(LocalTime.NOON))) {
				return true;
			}
		}
		
		if (containsAMSlot() && containsPMSlot()) {
			return true;
		}
		else return false;
	}
	
	public boolean[] containsDaySlot() {
		boolean[] bContainDaySlot = new boolean[6];
		for (int i = 0; i < numSlots; i++) {
			for (int j = 0; j < 6; j++) {
				if (slots[i].getDay() == j) {
					bContainDaySlot[j] = true;
				}
			}
		}
		return bContainDaySlot;
	}
	
	/* Helper functions for Task 3 */
	public String findCourseCode(List<Course> courseList) {
		for (Course course : courseList) {
			for (int i = 0; i < course.getNumSections(); i++) {
				if (course.getSection(i).getID().equals(this.id)) {
					String[] strTitle = course.getTitle().split(" ");
					return strTitle[0] + strTitle[1];
				}
			}
		}
		return null;
	}
	
	public String findCourseName(List<Course> courseList) {
		for (Course course : courseList) {
			for (int i = 0; i < course.getNumSections(); i++) {
				if (course.getSection(i).getID().equals(this.id)) {
					String[] strTitle = course.getTitle().split(" ", 4);
					return strTitle[3];
				}
			}
		}
		return null;
	}

}
