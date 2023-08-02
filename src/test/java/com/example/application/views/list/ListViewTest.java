package com.example.application.views.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.application.data.entity.Contact;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;

/*
 * The @SpringBootTest annotation makes sure that the 
 * Spring Boot application is initialized before the tests
 * are run and allows you to use the @Autowired annotation
 * in the test.
 * */
@SpringBootTest
public class ListViewTest {
	@Autowired
	private ListView listView;
	
	/*
	 *selects the first row in the grid and validates 
	 *that this shows the form with the selected Contact.
	 *
	 *The test verifies that the form logic works by asserting that the 
	 *form is initially hidden. It also does so by selecting the first 
	 *item in the grid and verifying that the form is visible and the 
	 *form is bound to the correct Contact by ensuring that the right 
	 *name is visible in the field.
	 **/
	@Test
	public void formShownWhenContactSelected() {
		Grid<Contact> grid = listView.grid;
		Contact firstContact = getFirstItem(grid);
		
		ContactForm form = listView.form;
		
		assertFalse(form.isVisible());
		grid.asSingleSelect().setValue(firstContact);
		assertTrue(form.isVisible());
		assertEquals(firstContact.getFirstName(), form.firstName.getValue());
	}

	private Contact getFirstItem(Grid<Contact> grid) {
		
		return ( (ListDataProvider<Contact>) grid.getDataProvider())
											.getItems().iterator().next();
	}
	
}
