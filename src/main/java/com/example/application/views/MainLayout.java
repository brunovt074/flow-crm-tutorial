package com.example.application.views;

import org.springframework.context.MessageSource;

import com.example.application.internationalization.AppLocaleResolver;
import com.example.application.security.SecurityService;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

import jakarta.servlet.http.HttpServletRequest;

//AppLayout is a Vaadin layout with a header and a 
//responsive drawer.
public class MainLayout extends AppLayout{
		
	private static final long serialVersionUID = -5236034067788054851L;
	private final SecurityService securityService;
	private MessageSource messageSource;
	private AppLocaleResolver appLocaleResolver;
	private HttpServletRequest request;
	
	public MainLayout(SecurityService securityService, 
						MessageSource messageSource, 
						AppLocaleResolver appLocaleResolver, 
						HttpServletRequest request) {
		//Autowire the SecurityService and save it in a field.
		this.securityService = securityService;
		this.messageSource = messageSource;
		this.appLocaleResolver = appLocaleResolver;
		this.request = request;
		createHeader();
		createDrawer();
	}

	/*
	 *Instead of styling the text with raw CSS, use 
	 *Lumo Utility Classes shipped with the default theme.
	 **/
	
	private void createHeader() {
		H1 logo = new H1("BVT CRM");
		String logoutText = messageSource.getMessage("logout", null, 
										appLocaleResolver.resolveLocale(request)); 
		
		logo.addClassNames(LumoUtility.FontSize.LARGE,
						   LumoUtility.Margin.MEDIUM);
		//alternativo video
		//logo.addClassNames("text-l","m-m");
		
		//String u = securityService.getAuthenticatedUser().getUsername();
		//Create a logout button that calls the logout() method in the service.
		Button logout = new Button(logoutText, e -> securityService.logout());
				
		/*
		 *1-DrawerToggle is a menu button that toggles the visibility 
		 *  of the sidebar. 
		 *2-Add the button to the header layout.
		 *
		 **/
		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(),logo, logout);
		//header.add(languagueSelector);
		//Centers the components in the header along the vertical axis.
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		/*
		 *Call header.expand(logo) to make the logo take up all of the 
		  extra space in the layout. This can push the logout button to 
		  the far right. 
		 **/		
		header.expand(logo);
		header.setWidthFull();
		header.addClassNames(LumoUtility.Padding.Vertical.NONE,
				 LumoUtility.Padding.Horizontal.MEDIUM);		
		
		addToNavbar(header);
	}
	
	/*
	 *Creates a RouterLink with the text "List" and ListView.class as the 
	 *destination view.
	 *
	 **/
	private void createDrawer() {
		String listText = messageSource.getMessage("sidebar-list", null, 
											appLocaleResolver.resolveLocale(request));
		String dashboardText = messageSource.getMessage("sidebar-dashboard", null, 
									appLocaleResolver.resolveLocale(request));
		
		addToDrawer(new VerticalLayout(
					new RouterLink(listText, ListView.class)/*,
										new RouterLink(dashboardText, DashboardView.class)
										)*/	));				
		
	}
	
}
