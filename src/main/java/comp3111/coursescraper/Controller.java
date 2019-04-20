package comp3111.coursescraper;


import java.awt.event.ActionEvent;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.util.Random;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Controller {
	
    @FXML
    private Tab tabMain;

    @FXML
    private TextField textfieldTerm;

    @FXML
    private TextField textfieldSubject;

    @FXML
    private Button buttonSearch;

    @FXML
    private TextField textfieldURL;

    @FXML
    private Tab tabStatistic;

    @FXML
    private Tab tabFilter;

    @FXML
    private CheckBox cboxAM;

    @FXML
    private CheckBox cboxPM;

    @FXML
    private CheckBox cboxMon;

    @FXML
    private CheckBox cboxTue;

    @FXML
    private CheckBox cboxWed;

    @FXML
    private CheckBox cboxThur;

    @FXML
    private CheckBox cboxFri;

    @FXML
    private CheckBox cboxSat;

    @FXML
    private Button buttonSelectAll;

    @FXML
    private CheckBox cboxCC;

    @FXML
    private CheckBox cboxNoEx;

    @FXML
    private CheckBox cboxLabOrTut;

    @FXML
    private Tab tabList;

    @FXML
    private Tab tabTimetable;

    @FXML
    private Tab tabAllSubject;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Tab tabSfq;

    @FXML
    private TextField textfieldSfqUrl;

    @FXML
    private Button buttonSfqEnrollCourse;

    @FXML
    private Button buttonInstructorSfq;

    @FXML
    private TextArea textAreaConsole;
    
    @FXML
    private TableView<Section> tViewList;

    @FXML
    private TableColumn<Section, String> tColumnCode;

    @FXML
    private TableColumn<Section, String> tColumnSection;

    @FXML
    private TableColumn<Section, String> tColumnName;

    @FXML
    private TableColumn<Section, String> tColumnInstructor;

    @FXML
    private TableColumn<Section, CheckBox> tColumnEnroll;
    
    private Scraper scraper = new Scraper();
    
    // Cache list for searched course to prevent duplicate (Used to maintain the enroll)
    private List<Course> cacheCourseList = new Vector<Course>();
    // List we have after search
    private List<Course> searchedCourseList = new Vector<Course>();
    // List we have after filter
    private List<Course> filteredCourseList = new Vector<Course>();
    
    @FXML
    void allSubjectSearch() {
    	
    }

    @FXML
    void findInstructorSfq() {
    	buttonInstructorSfq.setDisable(true);
    	// Add line begin
    	textAreaConsole.setText(textAreaConsole.getText() + "\n" + textfieldSfqUrl.getText());
    	// Add line end
    }

    @FXML
    void findSfqEnrollCourse() {

    }
    
    @FXML
    void search() {
    	List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    	//The request URL is assembled by the 3 textfield inputs. If v == null, theoretically UnknownHostException is the only possible outcome
    	if (v == null)
    	{
    		textAreaConsole.setText("Page not found! Please check that the base URL, Term, and Subject are all correct.\n");
    		return;
    	}
    	//This for loop block generates the total number of courses and sections
    	int courseCount = 0, sectionCount = 0;
    	for (Course c : v)
    	{
    		if (c.isValidCourse())
    			courseCount++;
    		sectionCount += c.getNumValidSections();
    	}
    	textAreaConsole.setText("Total Number of Course in this search: " + courseCount + "\nTotal Number of difference sections in this search: " + sectionCount +  "\n");
    	
    	List<String> instList = new ArrayList<String>();
    	//This block of for loop generates a list of all distinct instructors that shows up in the search
    	for (Course c: v)
    	{
    		for (int i = 0; i < c.getNumSections(); i++)
    		{
    			Section sect = c.getSection(i);
    			for (int j = 0; j < sect.getNumInstructors(); j++)
    			{
    				if (!instList.contains(sect.getInstructor(j).toString()))
    					instList.add(sect.getInstructor(j).toString());
    			}
    		}
    	}
    	//This block of for loop eliminates the instructors that are busy at the time specified by queryDay and queryTime from the list
    	for (Course c: v)
    	{
    		for (int i = 0; i < c.getNumSections(); i++)
    		{
    			Section sect = c.getSection(i);
    			int queryDay = 2;
    			String queryTime = "03:10PM";
    			if (sect.isBusyAt(queryDay, queryTime))	//If the section is busy
    			{
					for (int j = 0; j < sect.getNumInstructors(); j++)	//Assume if the section is busy, all instructors that teach the section are busy
						instList.remove(sect.getInstructor(j).toString());	//Remove the name if the name exists, no need to do contains() check prior 
				}
    		}
    	}
    	Collections.sort(instList);	//Sort the instList, by default it is sorted with the natural ordering (in ascending order, alphabetically)
    	String queryStr = "Instructors who has teaching assignment this term but does not need to teach at Tu 3:10pm: \n"; 
    	if (instList.size() == 0)
    		queryStr += "None.\n";
    	else
    	{
    		int rowFactor = 4;	//How many names to display in a row before starting a new row
    		for (int i = 0; i < instList.size(); i++)
    		{
    			queryStr += (i % rowFactor != 0? " | ": "");	//If it is not the first name in a row, add the separator " | "
    			queryStr += instList.get(i);
    			//If it is the last name in a row, or it is the last name in the list, add "\n"
    			queryStr += ((i % rowFactor == rowFactor - 1) || (i == instList.size()-1)? "\n": "");
    		}
    	}
    	textAreaConsole.setText(textAreaConsole.getText() + "\n" + queryStr);
    	    	
    	for (Course c : v) {
    		String newline = c.getTitle() + "\n";
    		for (int i = 0; i < c.getNumSections(); i++)
    		{
	    		Section sect = c.getSection(i);
    			for (int j = 0; j < sect.getNumSlots(); j++)
	    		{
	    			Slot slot = sect.getSlot(j);
	    			newline += sect + " Slot " + j + " | " + slot + "\n";
	    			//Echo for checking instructors[]
	    			//newline += "Taught by: " + sect.getInstructorString() + "\n";
	    		}
    		}
    		textAreaConsole.setText(textAreaConsole.getText() + "\n" + newline);
    	}
    	
    	/* For-loop added for Task 3 */
    	// Save the scraped data for later use
    	// Here I will scan through all the existing courses we have got in (List<Course>)v
    	// and then add those not on the cache to the courseList and cacheCourseList
    	// if found the course exist in the cache, get it from the cache instead of getting a new one
    	searchedCourseList.clear();
    	for (Course newCourse : v) {
    		boolean bAddNewCourse = true;
    		
    		for (Course oldCourse : cacheCourseList) {
    			if (newCourse.getTitle().equals(oldCourse.getTitle())) {
    				bAddNewCourse = false;
    				searchedCourseList.add(oldCourse);
    				break;
    			}
    		}
    		
    		if (bAddNewCourse) {
    			searchedCourseList.add(newCourse);
    			cacheCourseList.add(newCourse);
    		}
    	}
    	
    }
    
    private static final int COLUMN_START = 40, SLOT_WIDTH = 99, ROW_START = 102, SLOT_HEIGHT = 20;
    private static final LocalTime START_TIME = LocalTime.parse("09:00AM", DateTimeFormatter.ofPattern("hh:mma", Locale.US));
    private List<Color> colours = new ArrayList<Color>();	//List of different colours
 	/**
 	 * Updates the TimeTable tab to display the slots from enrolled sections
 	 * Operations include adding and removing the slots according to enrolment status
 	 */
    private void timetableUpdate()
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
    
    // Button "Select All" function
    @FXML
    
    void selectAll() {
    	if (buttonSelectAll.getText().equals("Select All")) 
    	{
	    	cboxAM.setSelected(true);
	    	cboxPM.setSelected(true);
	    	
	    	cboxMon.setSelected(true);
	    	cboxTue.setSelected(true);
	    	cboxWed.setSelected(true);
	    	cboxThur.setSelected(true);
	    	cboxFri.setSelected(true);
	    	cboxSat.setSelected(true);
	    	
	    	cboxCC.setSelected(true);
	    	cboxNoEx.setSelected(true);
	    	
	    	cboxLabOrTut.setSelected(true);
	    	
	    	buttonSelectAll.setText("De-select All");
    	}
    	else {
	    	cboxAM.setSelected(false);
	    	cboxPM.setSelected(false);
	    	
	    	cboxMon.setSelected(false);
	    	cboxTue.setSelected(false);
	    	cboxWed.setSelected(false);
	    	cboxThur.setSelected(false);
	    	cboxFri.setSelected(false);
	    	cboxSat.setSelected(false);
	    	
	    	cboxCC.setSelected(false);
	    	cboxNoEx.setSelected(false);
	    	
	    	cboxLabOrTut.setSelected(false);
	    	
	    	buttonSelectAll.setText("Select All");
    	}
    }

    // Event used to update the info displayed in console in filter tab 
    @FXML
    void filter() {
    	// Clear the console first
    	textAreaConsole.setText("");
    	
    	// Return if courseList is empty
    	if (searchedCourseList.isEmpty()) return;
    	
    	// Clear the filteredCourseList
    	filteredCourseList.clear();
    	
    	// If all conditions are false -> filter is disabled    	
    	if (!cboxAM.isSelected() && 
    			!cboxPM.isSelected() && 
    			!cboxMon.isSelected() &&
    			!cboxTue.isSelected() &&
    			!cboxWed.isSelected() &&
    			!cboxThur.isSelected() &&
    			!cboxFri.isSelected() &&
    			!cboxSat.isSelected() &&
    			!cboxCC.isSelected() &&
    			!cboxNoEx.isSelected() &&
    			!cboxLabOrTut.isSelected()) 
    	{
    		// Display all courses normally
    		String output = "Unfiltered Output: (No conditions have been chosen)\n";
        	for (Course course : searchedCourseList) {
        		// newline for debug (disable the real newline when using)
//        		String newline = course.getTitle() + "\nAttribute: (Debug) " + course.getAttribute() + "\nExclusion: (Debug) " + course.getExclusion() + "\n";        		
        		
        		// newline for real
        		String newline = course.getTitle() + "\n";
        		
        		for (int i = 0; i < course.getNumSections(); i++)
        		{
    	    		Section section = course.getSection(i);
        			for (int j = 0; j < section.getNumSlots(); j++)
    	    		{
    	    			Slot slot = section.getSlot(j);
    	    			newline += section + " Slot " + j + ": " + slot + "\n";
    	    		}
        		}
        		output += newline + "\n";
        	}
        	filteredCourseList.addAll(searchedCourseList);
    		textAreaConsole.setText(output + "\n");
    	}
    	// Else some conditions are true -> filter is on
    	else {
    		String output = "Filtered Output: (Filter applied)\n";
        	for (Course course : searchedCourseList) {
        		// newline for debug (disable the real newline when using)
//        		String newline = course.getTitle() + "\nAttribute: (Debug) " + course.getAttribute() + "\nExclusion: (Debug) " + course.getExclusion() + "\n";        		
        		
        		// newline for real
        		String newline = course.getTitle() + "\n";
        		
        		/* Bools for filter */
        		boolean isTimeValid = false;
        		boolean isDayValid = false;
        		boolean isCCValid = false;
        		boolean isNoExValid = false;
        		boolean isLabOrTutValid = false;
        		
        		/* Bool array used for Day Filter */
        		boolean isDaySelected[] = {cboxMon.isSelected(), cboxTue.isSelected(), cboxWed.isSelected(), cboxThur.isSelected(), cboxFri.isSelected(), cboxSat.isSelected()};
        		
        		/* Filter conditions for courses */
        		// CC 4Y
        		if (cboxCC.isSelected()) {
	        		if (course.isCC4Y()) {
	        			isCCValid = true;
	        		}
	        		else continue;
        		}
        		else isCCValid = true;
        		
        		// No Exclusion
        		if (cboxNoEx.isSelected()) {
        			if (course.isNoEx()) {
        				isNoExValid = true;
        			}
        			else continue;
        		}
        		else isNoExValid = true;
        		
        		// Contains Labs or Tutorials
        		if (cboxLabOrTut.isSelected()) {
        			if (course.containsLabOrTut()) {
        				isLabOrTutValid = true;
        			}
        			else continue;
        		}
        		else isLabOrTutValid = true;
        		
        		// Days
        		boolean[] bContainsDaySection = course.containsDaySection();
        		for (int day = 0; day < 6; day++) {
        			if (isDaySelected[day]) {
        				if(!bContainsDaySection[day]) break;
        			}
        			if (day == 5) isDayValid = true;
        		}
        		
	    		// AM/PM 
	    		if (cboxAM.isSelected() && cboxPM.isSelected()) {
	    			if (course.containsAMPMSection()) {
	    				isTimeValid = true;
	    			}
	    		}
	    		else if (cboxAM.isSelected()) {
    				if (course.containsAMSection()) {
    					isTimeValid = true;
    				}
    			}
	    		else if (cboxPM.isSelected()) {
	    			if (course.containsPMSection()) {
	    				isTimeValid = true;
	    			}
	    		}
	    		else isTimeValid = true;
        		
        		for (int i = 0; i < course.getNumSections(); i++)
        		{
    	    		Section section = course.getSection(i);
    	    		   	    		
    	    		// Modify output function
        			for (int j = 0; j < section.getNumSlots(); j++)
    	    		{
    	    			Slot slot = section.getSlot(j);
    	    			newline += section + " Slot " + j + ": " + slot + "\n";
    	    		}
        		}
        		
        		// If satisfy all the criteria
        		if (isTimeValid && isDayValid && isCCValid && isNoExValid && isLabOrTutValid) {
        			// Add the line
        			output += newline + "\n";
        			filteredCourseList.add(course);
        		}
        		
        	}
        	textAreaConsole.setText(output);
    	}
    	

    }
        
    // Event handling the list (Task 3)
    @FXML
    void list() {
    	// Run the filter once to show filtered info
    	filter();
    	
    	// If the filteredCourseList is empty,
    	// then fetch the data from searchCourseList
    	// If searchCourseList is also empty,
    	// then return (do nothing)    	
    	if (filteredCourseList.isEmpty()) {
    		if (!searchedCourseList.isEmpty()) {
    			filteredCourseList.clear();
    			filteredCourseList.addAll(searchedCourseList);
    		}
    		else return;
    	}
    	
    	// Clear the table every time to prevent duplicate
    	tViewList.getItems().clear();
    	
    	// Add the items of the filteredCourseList into the table
    	for (Course course : filteredCourseList) {
    		for (int i = 0; i < course.getNumSections(); i++) {
    			Section section = course.getSection(i);
    			// Get the items for the table
    			tViewList.getItems().add(section);
    			// If the CheckBox hasn't set the OnAction Event 
    			// set the OnAction Event for the CheckBox -> to re-run the list
    			if (section.getEnroll().getOnAction() == null)
    				section.getEnroll().setOnAction(event -> { list(); });
    		}
    	}
    	
		/* Set the items for each column */
		// Set course code
        tColumnCode.setCellValueFactory(cellData -> {
            Section section = cellData.getValue();

            return new ReadOnlyStringWrapper(section.findCourseCode(filteredCourseList));
        });
        tViewList.getColumns().set(0, tColumnCode);
        
        // Set section code
        tColumnSection.setCellValueFactory(cellData -> {
            Section section = cellData.getValue();

            return new ReadOnlyStringWrapper(section.getCode());
        });
        tViewList.getColumns().set(1, tColumnSection);
        
        // Set course name
        tColumnName.setCellValueFactory(cellData -> {
            Section section = cellData.getValue();

            return new ReadOnlyStringWrapper(section.findCourseName(filteredCourseList));
        });
        tViewList.getColumns().set(2, tColumnName);
        
        // Set instructor
        tColumnInstructor.setCellValueFactory(cellData -> {
        	Section section = cellData.getValue();
        	
        	return new ReadOnlyStringWrapper(section.getInstructorString());
        });
        tViewList.getColumns().set(3, tColumnInstructor);
        
        // Set Enroll
        tColumnEnroll.setCellValueFactory(
        		new PropertyValueFactory<>("enroll")
        		);
        tViewList.getColumns().set(4, tColumnEnroll);
             
    	  // Feedback which courses you have enrolled
    	  String feedback = "The following sections are enrolled:" + "\n";
        for (Course course : cacheCourseList) {
        	for (int i = 0; i < course.getNumSections(); i++) {
        		Section section = course.getSection(i);
        		if (section.getEnroll().isSelected()) {
        			feedback += section.findCourseCode(cacheCourseList) + " " + section.getCode() + "\n";
        		}
        	}
        } 
        textAreaConsole.setText(feedback  + "\n" + textAreaConsole.getText());
        timetableUpdate();
    }       
}
