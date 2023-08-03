package com.example.application.internationalization;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AppLocaleResolver implements LocaleResolver{

	
    public AppLocaleResolver() {
		super();		
	}
	
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		//String language = request.getHeader("Accept-Language");
		String language = request.getLocale().getLanguage().toString();
		
		if(language == null || language.isEmpty()) {
			
			return Locale.forLanguageTag("en");
		}
		
		Locale locale = Locale.forLanguageTag(language);
		
		if(LanguageConfig.LOCALES.contains(locale)) {
			
			return locale;
		}
		
		return Locale.forLanguageTag("en");
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		// TODO Auto-generated method stub
		
	}

}
