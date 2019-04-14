package comp3111.coursescraper;


import org.junit.Test;

import comp3111.coursescraper.Course;

import static org.junit.Assert.*;

import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class ItemTest {

	@Test
	public void testSetTitle() {
		Course i = new Course();
		i.setTitle("ABCDE");
		assertEquals(i.getTitle(), "ABCDE");
	}
	
	@Test
	public void testSlotTime() {
		Slot s = new Slot();
		s.setStart("02:00AM");
		assertEquals(s.getStartHour(), 2);
	}
	
	@Test
	public void testSetDescription() {
		Course c = new Course();
		c.setDescription("Test Description");
		assertEquals(c.getDescription(), "Test Description");
	}
	
	@Test
	public void testSetExclusion() {
		Course c = new Course();
		c.setExclusion("Test Exclusion");
		assertEquals(c.getExclusion(), "Test Exclusion");
	}
	
}
