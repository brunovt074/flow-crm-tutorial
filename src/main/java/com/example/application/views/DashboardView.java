//package com.example.application.views;
//
//import org.springframework.context.MessageSource;
//
//import com.example.application.data.service.CrmService;
//import com.example.application.internationalization.AppLocaleResolver;
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.charts.Chart;
//import com.vaadin.flow.component.charts.model.ChartType;
//import com.vaadin.flow.component.charts.model.DataSeries;
//import com.vaadin.flow.component.charts.model.DataSeriesItem;
//import com.vaadin.flow.component.html.Span;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.theme.lumo.LumoUtility;
//
//import jakarta.annotation.security.PermitAll;
//import jakarta.servlet.http.HttpServletRequest;
//
///*
// *DashboardView is mapped to the "dashboard" path and uses 
// *MainLayout as a parent layout. 
// * */
//@PermitAll
//@Route(value = "dashboard", layout = MainLayout.class)
//@PageTitle("Dashboard | BVT CRM")
//public class DashboardView extends VerticalLayout {
//	
//	
//	private static final long serialVersionUID = 4160237643853322335L;
//	private final CrmService service;
//	private MessageSource messageSource;
//	private AppLocaleResolver appLocaleResolver;
//	private HttpServletRequest request;
//	
//	//Takes CrmService as a constructor parameter and saves it as a field.
//	public DashboardView(CrmService service, 
//						 MessageSource messageSource,
//						 AppLocaleResolver appLocaleResolver,
//						 HttpServletRequest request) {
//		
//		this.service = service;
//		this.messageSource=messageSource;
//		this.appLocaleResolver=appLocaleResolver;
//		this.request=request;		
//		
//		addClassName("dashboard-view");
//		//Centers the contents of the layout.
//		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//		add(getContactStats(), getCompaniesChart());		
//	}
//	
//	private Component getContactStats() {
//		String contactsLabel = messageSource.getMessage("contacts", null,
//														appLocaleResolver.resolveLocale(request));
//		//Calls the service to get the number of contacts.
//		Span stats = new Span(service.countContacts() + " " + contactsLabel);
//		
//		stats.addClassNames(LumoUtility.FontSize.XLARGE,
//							LumoUtility.Margin.Top.MEDIUM);		
//		
//		return stats;
//	}
//	
//	/*
//	 *Calls the service to get all companies, then creates a DataSeriesItem for each, 
//	 *containing the company name and employee count.
//	 * 
//	 **/
//	private Chart getCompaniesChart() {
//		Chart chart = new Chart(ChartType.PIE);
//		
//		DataSeries dataSeries = new DataSeries();
//		service.findAllCompanies().forEach(company -> dataSeries.add(
//									new DataSeriesItem(company.getName(), 
//														company.getEmployeeCount()
//														)
//									)
//		);
//		
//		chart.getConfiguration().setSeries(dataSeries);
//		
//		return chart;
//	}
//	
//	
//	
//}
