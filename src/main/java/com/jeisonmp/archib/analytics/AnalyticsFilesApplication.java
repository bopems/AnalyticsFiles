package com.jeisonmp.archib.analytics;

import com.jeisonmp.archib.analytics.service.WatcherFiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class AnalyticsFilesApplication implements ApplicationListener<ApplicationReadyEvent> {

	@Value("${INPUTPATH:${user.dir}}")
	private String inputPath;

	public static void main(String args[]) {
		SpringApplication.run(AnalyticsFilesApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		try {
			new WatcherFiles(inputPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
