package com.example.application.internationalization;

import java.util.Arrays;
//import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

//import com.vaadin.flow.i18n.I18NProvider;

@Component
public class LanguageConfig{

	public static final List<Locale> LOCALES = Arrays.asList(
													new Locale("en"), 
													new Locale("es"),
													new Locale("pt"));
	

//	@Override
//	public String getTranslation(String key, Locale locale, Object... params) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
