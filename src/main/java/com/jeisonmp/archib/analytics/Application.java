package com.jeisonmp.archib.analytics;

import com.jeisonmp.archib.analytics.service.WatcherFiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.net.UnknownHostException;

@SpringBootApplication
public class Application implements ApplicationListener<ApplicationReadyEvent> {

	@Value("${HOMEPATH:./}")
	private String homePath;

	public static void main(String args[]) throws UnknownHostException {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		try {
			new WatcherFiles(homePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
