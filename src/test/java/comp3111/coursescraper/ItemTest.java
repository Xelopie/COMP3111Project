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
	public void testCourse() {
		/* Default constructor */
		Course course = new Course();
		assertEquals(course.getNumSections(), 0);
		
		/* Getter and Setter */
		course.setTitle("Test Title");
		assertEquals(course.getTitle(), "Test Title");
		
		course.setAttribute("Test Attribute");
		assertEquals(course.getAttribute(), "Test Attribute");
		
		course.setDescription("Test Description");
		assertEquals(course.getDescription(), "Test Description");
		
		course.setExclusion("Test Exclusion");
		assertEquals(course.getExclusion(), "Test Exclusion");
		
		course.setNumSections(1);
		assertEquals(course.getNumSections(), 1);
	}
	
	@Test
	public void testSection() {
		
	}
	
	@Test
	public void testSlot() {
		
	}
	
	
	
}
