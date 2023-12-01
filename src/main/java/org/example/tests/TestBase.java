package org.example.tests;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.BrowserType;
import org.example.appmanager.ApplicationManager;

public class TestBase {

    private static final ApplicationManager app = new ApplicationManager();

    @BeforeAll
    public static void setUp() throws Exception {
        app.init();
    }

    @AfterAll
    public static void tearDown() {
        app.stop();
    }
}

