package com.example.application.security;

import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Component
public class SecurityService {
	//Component de vaadin que facilita la autenticacion de usuarios
	private final AuthenticationContext authenticationContext;
	
	public SecurityService(AuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;		
	}
	
	public UserDetails getAuthenticatedUser() {
		return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
	}
	
	//utilizando AuthenticationContext de Vaadin
	public void logout() {
		authenticationContext.logout();
	}
	
	//utilizando SecurityContextLogoutHandler de Spring
//	public void logout() {
//		UI.getCurrent().getPage().setLocation("/");
//		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
//		logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletMapping(),null, null);
//	}
}
