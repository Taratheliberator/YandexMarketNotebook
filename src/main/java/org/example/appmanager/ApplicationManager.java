package org.example.appmanager;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.example.pageobjects.YandexPage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
/**
 * Класс ApplicationManager управляет жизненным циклом веб-драйвера и предоставляет доступ к основным функциям приложения.
 * Он инициализирует веб-драйвер, загружает конфигурационные настройки и создаёт экземпляры страниц.
 */
public class ApplicationManager {

    private WebDriver wd;
    private final Properties properties;
    private YandexPage yandexPage;
    /**
     * Конструктор класса ApplicationManager.
     * Инициализирует объект Properties для хранения и доступа к настройкам тестирования.
     */
    public ApplicationManager() {
        properties = new Properties();
    }
    /**
     * Конструктор класса ApplicationManager.
     * Инициализирует объект Properties для хранения и доступа к настройкам тестирования.
     */
    public void init() throws IOException {
        wd = new ChromeDriver();
        properties.load(new FileReader(new File("src/test/resources/test.properties")));
        wd.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        wd.get(properties.getProperty("web.baseUrl"));
        wd.manage().window().maximize();
        yandexPage = new YandexPage(wd);
    }
    /**
     * Завершает работу веб-драйвера и закрывает браузер.
     * Этот метод вызывается после выполнения всех тестов для корректного закрытия браузера.
     */
    public void stop() {
        if (wd != null) {
            wd.quit();
        }
    }
    /**
     * Возвращает экземпляр YandexPage для взаимодействия с веб-страницей Яндекс Маркета.
     *
     * @return Экземпляр YandexPage.
     */
    public YandexPage yandex() {
        return yandexPage;
    }
}
