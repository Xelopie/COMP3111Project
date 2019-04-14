package comp3111.coursescraper;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalTime;
import java.util.Locale;
import java.time.format.DateTimeFormatter;

/**
 * Stores the information of a slot. DAYS_MAP is used to assign the weekday codes with the corresponding number.
 */

public class Slot {
	private int day;
	private LocalTime start;
	private LocalTime end;
	private String venue;
	public static final String DAYS[] = {"Mo", "Tu", "We", "Th", "Fr", "Sa"};
	public static final Map<String, Integer> DAYS_MAP = new HashMap<String, Integer>();
	static {
		for (int i = 0; i < DAYS.length; i++)
			DAYS_MAP.put(DAYS[i], i);
	}

	/**
	 * Overrides the shallow copy default clone().
	 * @return A slot item clone of itself by deep copy
	 */
	@Override
	public Slot clone() {
		Slot s = new Slot();
		s.day = this.day;
		s.start = this.start;
		s.end = this.end;
		s.venue = this.venue;
		return s;
	}
	/**
	 * Can be called implicitly when slot is needed as String.
	 * @return the slot in String, formatted for display purpose
	 */
	@Override
	public String toString() {
		return DAYS[day] + start.toString() + "-" + end.toString() + " | " + venue;
	}
	/**
	 * Returns the start hour.
	 * @return the start hour in int
	 */
	public int getStartHour() {
		return start.getHour();
	}
	/**
	 * Returns the start minute.
	 * @return the start minute in int
	 */
	public int getStartMinute() {
		return start.getMinute();
	}
	/**
	 * Returns the end hour.
	 * @return the end hour in int
	 */
	public int getEndHour() {
		return end.getHour();
	}
	/**
	 * Returns the end minute.
	 * @return the end minute in int
	 */
	public int getEndMinute() {
		return end.getMinute();
	}
	/**
	 * Returns the start time.
	 * @return the start time in java.time.LocalTime
	 */
	public LocalTime getStart() {
		return start;
	}
	/**
	 * Sets the start time.
	 * @param start the start time to set
	 */
	public void setStart(String start) {
		this.start = LocalTime.parse(start, DateTimeFormatter.ofPattern("hh:mma", Locale.US));
	}
	/**
	 * Returns the end time.
	 * @return the end time in java.time.LocalTime
	 */
	public LocalTime getEnd() {
		return end;
	}
	/**
	 * Sets the end time.
	 * @param end the end time to set
	 */
	public void setEnd(String end) {
		this.end = LocalTime.parse(end, DateTimeFormatter.ofPattern("hh:mma", Locale.US));
	}
	/**
	 * Returns the venue.
	 * @return the venue in String
	 */
	public String getVenue() {
		return venue;
	}
	/**
	 * Sets the venue.
	 * @param venue the venue to set
	 */
	public void setVenue(String venue) {
		this.venue = venue;
	}

	/**
	 * Returns the day.
	 * @return the day in int
	 */
	public int getDay() {
		return day;
	}
	/**
	 * Sets the day.
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

}
