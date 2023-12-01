package org.example.appmanager;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.example.pageobjects.YandexPage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ApplicationManager {

    private WebDriver wd;
    private final Properties properties;
    private YandexPage yandexPage;

    public ApplicationManager() {
        properties = new Properties();
    }

    public void init() throws IOException {
        wd = new ChromeDriver();
        properties.load(new FileReader(new File("src/test/resources/test.properties")));
        wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        wd.get(properties.getProperty("web.baseUrl"));
        wd.manage().window().maximize();
        yandexPage = new YandexPage(wd);
    }

    public void stop() {
        if (wd != null) {
            wd.quit();
        }
    }

    public YandexPage yandex() {
        return yandexPage;
    }
}
