/**
 * 
 * You might want to uncomment the following code to learn testFX. Sorry, no tutorial session on this.
 * 
 */
package comp3111.coursescraper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class FxTest extends ApplicationTest {

	private Scene s;
	
	@Override
	public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("/ui.fxml"));
   		VBox root = (VBox) loader.load();
   		Scene scene =  new Scene(root);
   		stage.setScene(scene);
   		stage.setTitle("Course Scraper");
   		stage.show();
   		s = scene;
	}
	
	@Test
	public void testSelectAll() 
	{
		CheckBox cbox;
		clickOn("#tabFilter");
		clickOn("#buttonSelectAll");
		
		cbox = (CheckBox)s.lookup("#cboxAM");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxPM");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxMon");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxTue");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxWed");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxThur");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxFri");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxSat");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxSat");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxCC");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxNoEx");
		assertTrue(cbox.isSelected() == true);
		cbox = (CheckBox)s.lookup("#cboxLabOrTut");
		assertTrue(cbox.isSelected() == true);
		
		clickOn("#buttonSelectAll");
		cbox = (CheckBox)s.lookup("#cboxAM");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxPM");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxMon");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxTue");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxWed");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxThur");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxFri");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxSat");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxSat");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxCC");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxNoEx");
		assertTrue(cbox.isSelected() == false);
		cbox = (CheckBox)s.lookup("#cboxLabOrTut");
		assertTrue(cbox.isSelected() == false);
	}

	@Test
	public void testSearch() 
	{
		clickOn("#tabMain");
		clickOn("#buttonSearch");
		
		/* Test the basic function */
		TextArea console = (TextArea)s.lookup("#textAreaConsole");
		assertTrue(console.getText().contains("COMP 1001") && console.getText().contains("COMP 7990"));
		assertTrue(console.getText().contains("Total Number of Course in this search: 51"));
		assertTrue(console.getText().contains("Total Number of difference sections in this search: 188"));
		
		/* Test if no instructor */
		TextField term = (TextField)s.lookup("#textfieldTerm");
		TextField subject = (TextField)s.lookup("#textfieldSubject");
		term.setText("1840");
		subject.setText("UROP");
		clickOn("#buttonSearch");
		assertTrue(console.getText().contains("None."));	
		
		/* Test 404 handler */
		TextField url = (TextField)s.lookup("#textfieldURL");
		url.setText("Test Incorrect URL");
		clickOn("#buttonSearch");
		assertTrue(console.getText().contains("Page not found! Please check that the base URL, Term, and Subject are all correct.\n"));
	
	}
	
	@Test
	public void testFilter() 
	{
		clickOn("#tabMain");
		clickOn("#buttonSearch");
		
		clickOn("#tabFilter");
		
		/* Test the basic function */
		TextArea console = (TextArea)s.lookup("#textAreaConsole");
		assertTrue(console.getText().contains("Unfiltered Output: (No conditions have been chosen)"));
		assertTrue(console.getText().contains("COMP 1001") && console.getText().contains("COMP 7990"));
	
		/* Test the filter */
		clickOn("#cboxAM");
		clickOn("#cboxAM");		
		clickOn("#cboxPM");
		clickOn("#cboxPM");
		
		clickOn("#cboxMon");
		clickOn("#cboxMon");		
		clickOn("#cboxTue");
		clickOn("#cboxTue");		
		clickOn("#cboxWed");
		clickOn("#cboxWed");		
		clickOn("#cboxThur");
		clickOn("#cboxThur");		
		clickOn("#cboxFri");
		clickOn("#cboxFri");
		
		clickOn("#cboxSat");
		assertTrue(console.getText().contains("COMP 3071"));
		clickOn("#cboxSat");
		
		clickOn("#cboxCC");
		clickOn("#cboxCC");
		
		clickOn("#cboxNoEx");
		clickOn("#cboxNoEx");
		
		clickOn("#cboxLabOrTut");
		clickOn("#cboxLabOrTut");
		
		clickOn("#cboxMon");
		clickOn("#cboxFri");
		
		clickOn("#cboxNoEx");
		assertTrue(console.getText().contains("COMP 2011"));
		
		clickOn("#cboxCC");
		assertFalse(console.getText().contains("COMP"));
		
		clickOn("#cboxCC");
		clickOn("#cboxLabOrTut");
		
		clickOn("#cboxAM");
		clickOn("#cboxPM");
		assertTrue(console.getText().contains("COMP 2011") && console.getText().contains("COMP 3511") && console.getText().contains("COMP 4321") && console.getText().contains("COMP 4901I"));
	}
	
	@Test
	public void testList() {
		clickOn("#tabMain");
		clickOn("#buttonSearch");
		clickOn("#tabList");
		
		TableView<Section> table = (TableView<Section>) s.lookup("#tViewList");
		
		for (int i = 0; i < 3; i++) {
			clickOn(table.getItems().get(i).getEnroll());
		}
		for (int i = 0; i < 3; i++) {
			clickOn(table.getItems().get(i).getEnroll());
		}
	}
	
	@Test
	public void testTimetable() {
		clickOn("#tabMain");
		clickOn("#buttonSearch");
		clickOn("#tabList");
		
		TableView<Section> table = (TableView<Section>) s.lookup("#tViewList");
		
		for (int i = 0; i < 3; i++) {
			clickOn(table.getItems().get(i).getEnroll());
		}
		clickOn("#tabTimetable");
		clickOn("#tabList");
		for (int i = 0; i < 3; i++) {
			clickOn(table.getItems().get(i).getEnroll());
		}
		clickOn("#tabTimetable");
	}
}
