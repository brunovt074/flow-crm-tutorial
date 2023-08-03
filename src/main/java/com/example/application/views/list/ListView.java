package com.example.application.views.list;

import java.util.Collections;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.application.data.entity.Contact;
import com.example.application.data.service.CrmService;
import com.example.application.internationalization.AppLocaleResolver;
import com.example.application.views.MainLayout;
//import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;

@Component
@Scope("prototype")
//allow all logged-in users to access them.
@PermitAll
//ListView still matches the empty path, but now uses MainLayout as its parent.
@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | BVT CRM")
public class ListView extends VerticalLayout {
	//clases para la i18n
	private MessageSource messageSource;
	private AppLocaleResolver appLocaleResolver;
	private HttpServletRequest request;
	
	//Instanciamos el grid para la tabla que contendra a la clase Contact
	Grid<Contact> grid = new Grid<>(Contact.class);
	//Instanciamos el textfield para filtrar los nombres
	TextField filterText = new TextField();
	//Agregamos el ContactForm 
	ContactForm form;
	//Agregamos el CrmService
	CrmService service;
	
	//Constructor de la vista 
    public ListView(CrmService service, MessageSource messageSource,
    				AppLocaleResolver appLocaleResolver,
    				HttpServletRequest request) {
    	this.service = service;
    	this.messageSource = messageSource;
		this.appLocaleResolver =appLocaleResolver;
		this.request = request;
		
    	//Nombre de la clase CSS
    	addClassName("list-view");
    	//Seteamos que ocupe todo el tamaño disponible
    	setSizeFull();
    	//Metodo para configurar el grid
    	configureGrid();
    	//Metodo para configurar el form
    	configureForm();
    	
    	//agregamos la barra de tareas donde ira el filtrado de nombres
    	add(
    		//metodo personalizado	
    		getToolbar(),
    		getContent()
    	);
    	
    	updateList();    
    	closeEditor();
    }
	
    //The closeEditor() call at the end of the constructor:
	//1 - sets the form contact to null, clearing out old values;    	
	//2 - hides the form;
	//3 - removes the "editing" CSS class from the view.
    private void closeEditor() {
    	
    	form.setContact(null);
    	form.setVisible(false);
    	removeClassName("editing");
    	
    }
    
    //creamos un componente que envuelva grid y form en un HorizontalLayout
    private HorizontalLayout getContent() {
    	
    	HorizontalLayout content = new HorizontalLayout(grid, form);
    	
    	//seteamos crecimiento flexible para que el grid tenga 2 veces el tamaño del form
    	content.setFlexGrow(2, grid);
    	content.setFlexGrow(1, form);
    	content.addClassName("content");
    	content.setSizeFull();
    	
    	return content;
    }
    
    private void configureForm(){    	
    	form = new ContactForm(messageSource,appLocaleResolver,request, service.findAllCompanies(), service.findAllStatuses());
    	form.setWidth("25em");
    	form.addSaveListener(this::saveContact);
    	form.addDeleteListener(this::deleteContact);
    	form.addCloseListener(e -> closeEditor());
    }
    
    /**
     *The save event listener calls saveContact(). It does a few 
     *things:
     *1 - Uses contactService to save the contact in the event 
     *to the database;
     *2 - Updates the list; and
     *3 - Closes the editor. 
     **/
    private void saveContact(ContactForm.SaveEvent event) {
    	service.saveContact(event.getContact());
    	updateList();
    	closeEditor();    	
    }
    
    /**
     *The delete event listener calls deleteContact(). 
     *In the process, it also does a few things:
     *Uses contactService to delete the contact from the database;
     *Updates the list; and
     *Closes the editor. 
     * */
    private void deleteContact(ContactForm.DeleteEvent event) {
    	service.deleteContact(event.getContact());
    	updateList();
    	closeEditor();    	
    }
    
    private HorizontalLayout getToolbar() {
    	String filterByName = messageSource.getMessage("filter", null,
    												appLocaleResolver.resolveLocale(request));
    	String addContact = messageSource.getMessage("add-contact", null,
									appLocaleResolver.resolveLocale(request));
    	
    	//nombre del placeholder
    	filterText.setPlaceholder(filterByName);
    	//dejamos visible al boton
    	filterText.setClearButtonVisible(true);
    	//evitamos que se conecte a la db innecesariamente y lo haga solo cuando el user deje de escribir
    	filterText.setValueChangeMode(ValueChangeMode.LAZY);
    	//llamamos a updateList() cada vez que el filtro cambia
    	filterText.addValueChangeListener(e->updateList());
    	
    	//Boton de agregar contacto
    	Button addContactButton = new Button(addContact);
    	//Call addContact() when the user clicks on the "Add 
    	//contact" button.
    	addContactButton.addClickListener(e -> addContact());
    	
    	//iniciamos el layout del toolbar
    	HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
    	toolbar.addClassName("toolbar");
    	
    	return toolbar;
    	
    	
    }
    
    //addContact() clears the grid selection and creates a new 
    //Contact
    private void addContact() {
    	
    	grid.asSingleSelect().clear();
    	editContact(new Contact());
    	
    }
    private void configureGrid(){
    	String firstName = messageSource.getMessage("firstname", null,appLocaleResolver.resolveLocale(request));
    	String lastName = messageSource.getMessage("lastname", null, appLocaleResolver.resolveLocale(request));
    	String status = messageSource.getMessage("status", null, appLocaleResolver.resolveLocale(request));
    	String company = messageSource.getMessage("company", null, appLocaleResolver.resolveLocale(request));
    	
    	grid.addClassName("contact-grid");
    	grid.setSizeFull();
    	//Nombres de las columnas
    	grid.setColumns("firstName", "lastName", "email");    	
    	grid.getColumnByKey("firstName").setHeader(firstName);
    	grid.getColumnByKey("lastName").setHeader(lastName);
    	//Obtenemos el nombre de cada uno de los objetos que representara esa columna
    	grid.addColumn(contact -> contact.getStatus().getName()).setHeader(status);
    	grid.addColumn(contact -> contact.getCompany().getName()).setHeader(company);
    	//Seteamos las columnas para que su tamaño se adapte dinamicamente al del contenido.    	
    	grid.getColumns().forEach(col -> col.setAutoWidth(true));
    	
    	/* addValueChangeListener() adds a listener to the grid. 
    	 * The Grid component supports multi- and single-selection modes. 
    	 * You only need to select a single Contact, so you can use the 
    	 * asSingleSelect() method. The getValue() method returns the 
    	 * Contact in the selected row, or null if there is no 
    	 * selection.*/
    	grid.asSingleSelect().addValueChangeListener(
    								e -> editContact(e.getValue()));
    }
    
    /*
     * 
     *editContact() sets the selected contact in the ContactForm 
     *and hides or shows the form, depending on the selection. 
     *It also sets the "editing" CSS class name when editing.
     *
     **/
    private void editContact(Contact contact) {
    	
    	if(contact == null){    		
    		
    		closeEditor();    		
    	
    	} else {
    		
    		form.setContact(contact);
    		form.setVisible(true);
    		addClassName("editing");
    	}  	
    	
    }
    
    //sets the grid items by calling the service with the value from the filter text field.
    private void updateList() {
    	grid.setItems(service.findAllContacts(filterText.getValue()));    	
    }
    
}
