package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.example.application.internationalization.AppLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;

public class ContactFormTest {
	private MessageSource messageSource;
	private AppLocaleResolver appLocaleResolver;
	private HttpServletRequest request;
	
	private List<Company> companies;
	private List<Status> statuses;
	private Contact marcUsher;
	private Company company1;
	private Company company2;
	private Status status1;
	private Status status2;
	
	
	/*
	 *The @BeforeEach annotation adds dummy data that’s used for testing.
	 *This method is executed before each @Test method.
	 * 
	 **/
	@BeforeEach
	public void setupData() {
		
		companies = new ArrayList<>();
		company1= new Company();
		company1.setName("Vaadin Ltd");
		company2= new Company();
		company2.setName("IT Mill");
		companies.add(company1);
		companies.add(company2);
		
		statuses = new ArrayList<>();
		status1 = new Status();
		status1.setName("Status 1");		
		status2 = new Status();
		status2.setName("Status 2");
		statuses.add(status1);
		statuses.add(status2);
		
		marcUsher = new Contact();
		marcUsher.setFirstName("Marc");
		marcUsher.setLastName("Usher");
		marcUsher.setEmail("marc@usher.com");
		marcUsher.setStatus(status1);
		marcUsher.setCompany(company2);
	}
	
	/*
	 * 1-Validates that the fields are populated correctly, by first 
	 * initializing the contact form with some companies, and then 
	 * setting a contact bean for the form.
	 * 2-Uses standard JUnit assertEquals() methods to compare the values from 
	 * the fields available through the ContactForm instance.
	 * **/
	@Test
	public void formFieldsPopulated() {
		//1
		ContactForm form = new ContactForm(messageSource,
											appLocaleResolver,
											request,
											companies, 
											statuses);
		form.setContact(marcUsher);
		//2
		assertEquals("Marc", form.firstName.getValue());
		assertEquals("Usher", form.lastName.getValue());
		assertEquals("marc@usher.com", form.email.getValue());
		assertEquals(company2, form.company.getValue());
		assertEquals(status1, form.status.getValue());
	} 
	
	/*
	 *1-Initialize the form with an empty Contact.
	 *2-Populate values into the form.
	 *3-Capture the saved contact into an AtomicReference.
	 *4-Click the save button and read the saved contact.
	 *5-Once the event data is available, verify that the 
	 * bean contains the expected values.
	 * 
	 * */
	@Test
	public void saveEventHasCorrectValues() {
		ContactForm form = new ContactForm(messageSource,
											appLocaleResolver,
											request,
											companies, 
											statuses);
		Contact contact = new Contact();
		form.setContact(contact);//1
		form.firstName.setValue("John");//2
		form.lastName.setValue("Doe");
		form.company.setValue(company1);
		form.email.setValue("john@doe.com");
		form.status.setValue(status2);
		
		//3
		AtomicReference<Contact> savedContactRef = new AtomicReference<>(null);
		form.addSaveListener(e -> {
							savedContactRef.set(e.getContact());
							});
		form.save.click();//4
		Contact savedContact = savedContactRef.get();
		//5
		assertEquals("John", savedContact.getFirstName());
		assertEquals("Doe", savedContact.getLastName());
		assertEquals("john@doe.com", savedContact.getEmail());
		assertEquals(company1, savedContact.getCompany());
		assertEquals(status2, savedContact.getStatus());
	}
}
