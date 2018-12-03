package com.bopems.analytics.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class WatcherFilesTest {

    //@Test
    public void analyticsStart() throws IOException {

        WatcherFiles watcher = new WatcherFiles(System.getProperty("user.dir"));
    }

    @Test
    public void failTest() {

        Assert.assertEquals(1, 0);
    }
}
