package com.ten68.marketing.webfront;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebfrontApplication {
	public static final Marker AUDIT_MARKER = MarkerFactory.getMarker("AUDIT");


	public static void main(String[] args) {
		SpringApplication.run(WebfrontApplication.class, args);
	}

}
