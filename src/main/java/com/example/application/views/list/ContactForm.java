package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.example.application.internationalization.AppLocaleResolver;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.context.MessageSource;

public class ContactForm extends FormLayout {
	
	
	private static final long serialVersionUID = 8142962175529018835L;
	
	private MessageSource messageSource;
	private AppLocaleResolver appLocaleResolver;
	private HttpServletRequest request;

	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");
	EmailField email = new EmailField("Email");
	ComboBox<Status> status = new ComboBox<>("Status");
	ComboBox<Company> company = new ComboBox<>("Company");

	Button save;
	Button delete;
	Button close;

	Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

	public ContactForm(MessageSource messageSource,
					   AppLocaleResolver appLocaleResolver,
					   HttpServletRequest request, 
					   List<Company> companies, 
					   List<Status> statuses) {
		
		addClassName("contact-form");
		binder.bindInstanceFields(this);

		this.messageSource = messageSource;
		this.appLocaleResolver = appLocaleResolver;
		this.request = request;
		// Etiquetas de los campos del formulario
	    String firstNameLabel = messageSource.getMessage("firstname", null, appLocaleResolver.resolveLocale(request));
	    String lastNameLabel = messageSource.getMessage("lastname", null, appLocaleResolver.resolveLocale(request));
	    String statusLabel = messageSource.getMessage("status", null, appLocaleResolver.resolveLocale(request));
	    String companyLabel = messageSource.getMessage("company", null, appLocaleResolver.resolveLocale(request));
	    String saveButtonLabel = messageSource.getMessage("save", null, appLocaleResolver.resolveLocale(request));
	    String deleteButtonLabel = messageSource.getMessage("delete", null, appLocaleResolver.resolveLocale(request));
	    String cancelButtonLabel = messageSource.getMessage("cancel", null, appLocaleResolver.resolveLocale(request));

	    
	    save = new Button(saveButtonLabel);
		delete = new Button(deleteButtonLabel);
		close = new Button(cancelButtonLabel);
	    
		firstName.setLabel(firstNameLabel);
	    lastName.setLabel(lastNameLabel);
	    email.setLabel("Email");
	    status.setLabel(statusLabel);
	    company.setLabel(companyLabel);
	    
		company.setItems(companies);
		company.setItemLabelGenerator(Company::getName);
		status.setItems(statuses);
		status.setItemLabelGenerator(Status::getName);

		add(firstName, lastName, email, company, status, createButtonsLayout());
	}

	private Component createButtonsLayout() {
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);

		save.addClickListener(event -> validateAndSave()); // <1>
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // <2>
		close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
		return new HorizontalLayout(save, delete, close);
	}

	private void validateAndSave() {
		if (binder.isValid()) {
			fireEvent(new SaveEvent(this, binder.getBean())); // <6>
		}
	}

	public void setContact(Contact contact) {
		binder.setBean(contact); // <1>
	}

	// Events
	public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3301589023858113319L;
		private Contact contact;

		protected ContactFormEvent(ContactForm source, Contact contact) {
			super(source, false);
			this.contact = contact;
		}

		public Contact getContact() {
			return contact;
		}
	}

	public static class SaveEvent extends ContactFormEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8677583235623710537L;

		SaveEvent(ContactForm source, Contact contact) {
			super(source, contact);
		}
	}

	public static class DeleteEvent extends ContactFormEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3687135766429325193L;

		DeleteEvent(ContactForm source, Contact contact) {
			super(source, contact);
		}

	}

	public static class CloseEvent extends ContactFormEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 831299415299312069L;

		CloseEvent(ContactForm source) {
			super(source, null);
		}
	}

	public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
		return addListener(DeleteEvent.class, listener);
	}

	public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
		return addListener(SaveEvent.class, listener);
	}

	public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
		return addListener(CloseEvent.class, listener);
	}

}
