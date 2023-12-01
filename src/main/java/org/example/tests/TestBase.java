package org.example.tests;

import org.junit.jupiter.api.AfterEach;
import org.example.appmanager.ApplicationManager;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    protected static final ApplicationManager app = new ApplicationManager();

    @BeforeEach
    public void setUp() throws Exception {
        app.init();
    }

    @AfterEach
    public void tearDown() {
        app.stop();
    }
}

