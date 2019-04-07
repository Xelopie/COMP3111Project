package comp3111.coursescraper;


import java.awt.event.ActionEvent;
import java.time.LocalTime;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.List;
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
    
    private Scraper scraper = new Scraper();
    
    private List<Course> courseList;
    
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
    	for (Course c : v) {
    		String newline = c.getTitle() + "\n";
    		int totalSlot = 0;
    		for (int i = 0; i < c.getNumSections(); i++)
    		{
	    		Section current = c.getSection(i);
    			for (int j = 0; j < current.getNumSlots(); j++)
	    		{
	    			Slot t = current.getSlot(j);
	    			newline += "Slot " + totalSlot++ + ": " + current + " " + t + "\n";
	    		}
    		}
    		textAreaConsole.setText(textAreaConsole.getText() + "\n" + newline);
    	}
    	
    	// Save the scraped data for later use
    	courseList = v;
    	
    	//Add a random block on Saturday
    	AnchorPane ap = (AnchorPane)tabTimetable.getContent();
    	Label randomLabel = new Label("COMP1022\nL1");
    	Random r = new Random();
    	double start = (r.nextInt(10) + 1) * 20 + 40;

    	randomLabel.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    	randomLabel.setLayoutX(600.0);
    	randomLabel.setLayoutY(start);
    	randomLabel.setMinWidth(100.0);
    	randomLabel.setMaxWidth(100.0);
    	randomLabel.setMinHeight(60);
    	randomLabel.setMaxHeight(60);
    
    	ap.getChildren().addAll(randomLabel);
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
    void updateConsole() {
//    	// Test function
//    	if (cboxAM.isSelected()) 
//    		textAreaConsole.setText("AM: ON");
//    	else
//    		textAreaConsole.setText("AM: OFF");
    	
    	// Clear the console first
    	textAreaConsole.setText("");
    	
    	// Return if courseList is empty
    	if (courseList == null) return;
    	
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
    		String output = "Unfiltered Output: (No conditions have been chosen)\n";
        	for (Course course : courseList) {
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
    		textAreaConsole.setText(output + "\n");
    	}
    	// Else some conditions are true -> filter is on
    	else {
    		String output = "Filtered Output: (Filter applied)\n";
        	for (Course course : courseList) {
        		String newline = course.getTitle() + "\n Attribute: (Debug) " + course.getAttribute() + "\n Exclusion: (Debug) " + course.getExclusion() + "\n";
        		
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
        		}
        		else isCCValid = true;
        		
        		// No Exclusion
        		if (cboxNoEx.isSelected()) {
        			if (course.isNoEx()) {
        				isNoExValid = true;
        			}
        		}
        		else isNoExValid = true;
        		
        		// Contains Labs or Tutorials
        		if (cboxLabOrTut.isSelected()) {
        			if (course.containsLabOrTut()) {
        				isLabOrTutValid = true;
        			}
        		}
        		else isLabOrTutValid = true;
        		
        		for (int i = 0; i < course.getNumSections(); i++)
        		{
    	    		Section section = course.getSection(i);
    	    		
    	    		/* Filter conditions for sections */
    	    		// AM/PM 
    	    		if (cboxAM.isSelected() && cboxPM.isSelected()) {
    	    			if (section.containsAMPMSlot()) {
    	    				isTimeValid = true;
    	    			}
    	    		}
    	    		else if (cboxAM.isSelected()) {
	    				if (section.containsAMSlot()) {
	    					isTimeValid = true;
	    				}
	    			}
    	    		else if (cboxPM.isSelected()) {
    	    			if (section.containsPMSlot()) {
    	    				isTimeValid = true;
    	    			}
    	    		}
    	    		else isTimeValid = true;
    	    		
    	    		// Days
    	    		boolean[] bContainDaySlot = section.containDaySlot();
    	    		for (int day = 0; day < 6; day++) { 
    	    			if (isDaySelected[day]) {
    	    				if (!bContainDaySlot[day]) break;
    	    			}
    	    			if (day == 5) isDayValid = true;
    	    		}
    	    		
    	    		// 
    	    		
    	    		// Modify output function
        			for (int j = 0; j < section.getNumSlots(); j++)
    	    		{
    	    			Slot slot = section.getSlot(j);

    	    			newline += section + " Slot " + j + ": " + slot + "\n";
    	    		}
        		}
        		if (isTimeValid && isDayValid && isCCValid && isNoExValid && isLabOrTutValid) 
        			output += newline + "\n";
        	}
        	textAreaConsole.setText(output + "\n");
    	}
    	

    }
        

}
