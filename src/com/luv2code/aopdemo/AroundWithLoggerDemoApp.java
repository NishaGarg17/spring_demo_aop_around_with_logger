package com.luv2code.aopdemo;

import java.util.logging.Logger;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.luv2code.aopdemo.service.TrafficFortuneService;

public class AroundWithLoggerDemoApp {
	private static Logger logger = Logger.getLogger(AroundWithLoggerDemoApp.class.getName());
	public static void main(String[] args) {
		// read the spring java config class
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);

		// get the bean from spring container
		TrafficFortuneService trafficFortuneService = context.getBean("trafficFortuneService", TrafficFortuneService.class);
		logger.info("\nMain Program : Around Demo App");
		logger.info("Calling get forutne");
		logger.info("\nMy fortune is: " + trafficFortuneService.getFortune());
		logger.info("Finished");
		
		// close the context
		context.close();

	}

}
