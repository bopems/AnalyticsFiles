package br.com.ibm.analytics.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Tasks {

    private static final Logger log = LoggerFactory.getLogger(Tasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${HOMEPATH:./}")
    private String homePath;

    @Scheduled(fixedDelayString = "${scheduler.importfiles.delay}")
    public void importfiles() {

        log.info("Start scheduler 'ImportFiles' now {} - PATH: {}", dateFormat.format(new Date()), homePath);

        File files[];
        File path = new File(homePath);
        files = path.listFiles(file -> file.getName().endsWith(".bat"));

        for(int i = 0; i < files.length; i++){
            //leia arquivos[i];
        }

    }
}
