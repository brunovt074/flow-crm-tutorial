package com.example.application.views;

import org.springframework.context.MessageSource;

import com.example.application.internationalization.AppLocaleResolver;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.servlet.http.HttpServletRequest;


/*
 *Map the view to the "loginForm" path. LoginView should encompass the 
 *entire browser window, so don’t use MainLayout as the parent. 
 * 
 **/
@Route("loginForm")
@PageTitle("Login | BVT CRM")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver{
	
	private static final long serialVersionUID = -4573410522892153365L;
	
	private MessageSource messageSource;
	private AppLocaleResolver appLocaleResolver;
	private HttpServletRequest request;
	//Instantiate a LoginForm component to capture username and password.
	private final LoginForm loginForm = new LoginForm();
	private LoginI18n loginI18n = LoginI18n.createDefault();
	
	public LoginView(MessageSource messageSource,
									AppLocaleResolver appLocaleResolver,
									HttpServletRequest request) {
		//I18n
		this.messageSource=messageSource;
		this.appLocaleResolver=appLocaleResolver;
		this.request=request;
		
		addClassName("loginForm-view");
		
		String language = appLocaleResolver.resolveLocale(request).toString();
		String loginDescription = messageSource.getMessage("login-description", null, appLocaleResolver.resolveLocale(request));
				
		if(language.equalsIgnoreCase("es") || language.equalsIgnoreCase("pt")) {	
			
			String loginTitleLabel = messageSource.getMessage("login-title", null, appLocaleResolver.resolveLocale(request));
			String loginUsernameLabel = messageSource.getMessage("login-username", null, appLocaleResolver.resolveLocale(request));  
			String loginPasswordLabel = messageSource.getMessage("login-password", null, appLocaleResolver.resolveLocale(request));  
			String loginSubmitLabel = messageSource.getMessage("login-submit", null, appLocaleResolver.resolveLocale(request));
			String loginForgotPasswordLabel = messageSource.getMessage("login-forgot-password", null, appLocaleResolver.resolveLocale(request));
			String loginErrorTitleLabel = messageSource.getMessage("login-error-title", null, appLocaleResolver.resolveLocale(request)); 
			String loginErrorMessage = messageSource.getMessage("login-error-message", null, appLocaleResolver.resolveLocale(request));
			String loginErrorUserRequired = messageSource.getMessage("login-user-required", null, appLocaleResolver.resolveLocale(request));
			String loginPasswordRequired = messageSource.getMessage("login-password-required", null, appLocaleResolver.resolveLocale(request));
			
			LoginI18n.Form loginI18nForm = loginI18n.getForm();			
			loginI18nForm.setTitle(loginTitleLabel);
			loginI18nForm.setUsername(loginUsernameLabel);
			loginI18nForm.setPassword(loginPasswordLabel);
			loginI18nForm.setSubmit(loginSubmitLabel);
			loginI18nForm.setForgotPassword(loginForgotPasswordLabel);			
			
			LoginI18n.ErrorMessage loginI18nErrorMessage = loginI18n.getErrorMessage();
			loginI18nErrorMessage.setTitle(loginErrorTitleLabel);
			loginI18nErrorMessage.setUsername(loginErrorUserRequired);
			loginI18nErrorMessage.setPassword(loginPasswordRequired);
			loginI18nErrorMessage.setMessage(loginErrorMessage);		
			
			loginI18n.setForm(loginI18nForm);
		}		
		
		loginI18n.setAdditionalInformation(loginDescription);		
		loginForm.setI18n(loginI18n);
		
		/*Make LoginView full size and center its content — both horizontally 
		 *and vertically — by calling setAlignItems(`Alignment.CENTER)` 
		 *and setJustifyContentMode(`JustifyContentMode.CENTER)`.
		 **/
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		//Set the LoginForm action to "loginForm" to post the loginForm form 
		//to Spring Security.		
		loginForm.setAction("loginForm");		
		
		add(new H1("BVT CRM"),  loginForm);		
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		//Read query parameters and show an error if a loginForm attempt fails.
		if(beforeEnterEvent.getLocation()
						   .getQueryParameters()
						   .getParameters()
						   .containsKey("error")) {
				
			loginForm.setError(true);
		}
		
	}

}
