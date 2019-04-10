package comp3111.coursescraper;

import java.time.LocalTime;

public class Section {
	private static final int DEFAULT_MAX_SLOT = 20;
	
	private String id;
	private String code;
	private boolean enrollStatus;
	private Slot [] slots;
	private int numSlots;
	
	public Section()
	{
		slots = new Slot[DEFAULT_MAX_SLOT];
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++) slots[i] = null;
		numSlots = 0;
			
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

	public String toString() { return " " + code + " (" + id +  ")"; }
	/**
	 * @return the numSlots
	 */
	
	public int getNumSlots() { return numSlots; }

	/**
	 * @param numSlots the numSlots to set
	 */
	public void setNumSlots(int numSlots) { this.numSlots = numSlots; }
	
	public void setID(String i) { id = i; }
	public String getID() { return id; }
	
	public void setCode(String str) { code = str; }
	public String getCode() { return code; }
	
	public void setEnrollStatus(boolean bool) {	enrollStatus = bool; }
	public boolean getEnrollStatus() { return enrollStatus; }
	
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
}
