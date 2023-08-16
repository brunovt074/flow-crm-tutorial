package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, 
 * tablets and some desktop browsers.
 * The @PWA annotation tells Vaadin to create a ServiceWorker and a 
 * manifest file. 
 * name is the full name of the application for the manifest file.
 * shortName should be short enough to fit under an icon when installed,
 * and shouldn’t exceed 12 characters.
 */


@SpringBootApplication
@Theme(value = "flowcrmtutorial")
@PWA(name="Vaadin CRM",
	shortName="CRM",
	offlinePath="offline.html",
	offlineResources = {"./icons/icon.png","./images/empty-plant.png"})
public class Application implements AppShellConfigurator {

    private static final long serialVersionUID = -5170116799200123108L;

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
