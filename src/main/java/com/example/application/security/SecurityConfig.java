package com.example.application.security;

import com.example.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//Enable Spring Security.
@EnableWebSecurity
@Configuration
//Extend the VaadinWebSecurity class to configure Spring Security for Vaadin.
public class SecurityConfig extends VaadinWebSecurity{

//	alternativa vid
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		//Allow access to LoginView.
		setLoginView(http, LoginView.class);
	}
	
	@Override
	protected void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers("/images/**");
		super.configure(web);
	}
	
	
//	alternativa docs
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		//Allow public access to the image directory.
//		http.authorizeHttpRequests()		
//			.requestMatchers("/images/*.png").permitAll();
//		super.configure(http);
//		//Allow access to LoginView.
//		setLoginView(http, LoginView.class);
//		
//	}
	
	@Bean
	public UserDetailsService users() {
		UserDetails user = User.builder()
							.username("user")
							//password = password with this hash, don't tell anybody :-)
							.password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
							.roles("USER")
							.build();
		UserDetails admin = User.builder()
							.username("admin")
							.password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
							.roles("USER","ADMIN")
							.build();
		
		//Configure an in-memory users for testing
		return new InMemoryUserDetailsManager(user, admin);
		
	}
	
}
