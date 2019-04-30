package comp3111.coursescraper;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Simple class with the only functionality to update the Timetable tab.
 */
public class Timetable
{
	private static final int COLUMN_START = 40, SLOT_WIDTH = 99, ROW_START = 102, SLOT_HEIGHT = 20;
    private static final LocalTime START_TIME = LocalTime.parse("09:00AM", DateTimeFormatter.ofPattern("hh:mma", Locale.US));
    private static List<Color> colours = new ArrayList<Color>();	//List of different colours
 	
    /**
 	 * Updates the TimeTable tab to display the slots from enrolled sections.
 	 * <br>
 	 * Operations include adding and removing the slots from the tab according to enrolment status.
     * @param tabTimetable
     * @param cacheCourseList
     */
    public static void timetableUpdate(Tab tabTimetable, List<Course> cacheCourseList)
    {
    	//Initialize colours only once in runtime
    	if (colours.size() == 0)
    	{
    		for (int i = 0; i < 256; i++)
    		{
    			Random rand = new Random();
    			colours.add(Color.color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
    		}
    	}
    	List<Label>[] labelLists = new List[6];
     	for (int i = 0; i < 6; i++)
     	{
     		labelLists[i] = new ArrayList<Label>();
     	}
     	int sectionCount = 0;
     	AnchorPane ap = (AnchorPane)tabTimetable.getContent();
     	for (Course course : cacheCourseList)
     	{
        	for (int i = 0; i < course.getNumSections(); i++)
        	{
        		Section section = course.getSection(i);
        		if (section.getEnroll().isSelected())
        		{
        			for (int j = 0; j < section.getNumSlots(); j++)
        			{
        				Label label = new Label();
            			label.setBackground(new Background(new BackgroundFill(colours.get(sectionCount), CornerRadii.EMPTY, Insets.EMPTY)));
            			label.setLayoutX(ROW_START + 100 * section.getSlot(j).getDay());
            			label.setLayoutY(COLUMN_START + SLOT_HEIGHT * Duration.between(START_TIME, section.getSlot(j).getStart()).toMinutes() / 60.0);
            			label.setMinWidth(SLOT_WIDTH);
            			label.setMaxWidth(SLOT_WIDTH);
            			//slotHeight is the difference between start and end time in hours
            			//E.g. 09:00~11:30 will be 2.5
            			double slotHeight = Duration.between(section.getSlot(j).getStart(), section.getSlot(j).getEnd()).toMinutes() / 60.0;
            			//Get course and section code into the label text, add \n in between if the slot height allows
        				label.setText(course.getTitle().substring(0, course.getTitle().indexOf("-")) + (slotHeight <= 1? "": "\n") + section.getCode());
            			label.setMinHeight(SLOT_HEIGHT * slotHeight);
            	    	label.setMaxHeight(SLOT_HEIGHT * slotHeight);
            	    	//Set to an opacity lower than 1.0 for transparency, allowing overlapping of labels
            	    	label.setOpacity(0.4);
            	    	//White text to have a higher contrast with the label colour generally
            	    	label.setTextFill(Paint.valueOf("WHITE"));
            	    	labelLists[section.getSlot(j).getDay()].add(label);
            	    }
        			sectionCount++;	//Increment on the index to get the next colour from colours
        		}
        		else
        		{
        			String courseTitle = course.getTitle().substring(0, course.getTitle().indexOf("-")), sectionCode = section.getCode();
        			//Descending for loop as a precaution with remove() shifting index, potentially causing misses in remove process
        			for (int j = ap.getChildren().size()-1; j >= 0; j--)
        			{
        				//Check for the occurrence of the exact string
        				if (ap.getChildren().get(j).toString().contains(courseTitle + "\n" + sectionCode) || ap.getChildren().get(j).toString().contains(courseTitle + sectionCode))
        				{
        					ap.getChildren().remove(ap.getChildren().get(j));
        				}
        			}
    			}
        	}
        }
     	for (List<Label> labelList: labelLists)
     	{
     		for (Label label: labelList)
     		{
     			ap.getChildren().addAll(label);
     		}
     	}
    }
}