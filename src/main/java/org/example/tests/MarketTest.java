package org.example.tests;

import org.example.appmanager.ApplicationManager;
import org.example.pageobjects.YandexPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class MarketTest {

    private static ApplicationManager app;
    private static YandexPage yandexPage;

    @BeforeAll
    public static void setUp() throws Exception {
        app = new ApplicationManager();
        app.init();
        yandexPage = app.yandex();
    }

    @AfterAll
    public static void tearDown() {
        app.stop();
    }

    @Test
    public void pageMarketTest() throws InterruptedException {
        yandexPage.goToMarket()

                .goToComputers()
                .goToNotebooks()
                .openFilter()
                .setDownRange(10000)
                .setUpRange(30000)
                .setVendorName("HP")
                .setVendorName("Lenovo")
                .showResults()
                .loadAllNotebooks();

        List<WebElement> notebooks = yandexPage.getList();
        int numberOfNotebooks = notebooks.size();
        System.out.println("Количество загруженных ноутбуков: " + numberOfNotebooks);

        List<WebElement> noteList = yandexPage.getList();
        for (WebElement element : noteList) {
            System.out.println(element.getText());
        }
        System.out.println("Размер массива " + noteList.size());

        String target = noteList.get(0).getText();
        System.out.println("Первый элемент " + target);

        Pattern pattern = Pattern.compile("Ноутбук [^\\n]+");
        Matcher matcher = pattern.matcher(target);

        if (matcher.find()) {
            String laptopModel = matcher.group();
            System.out.println("Найденная модель ноутбука: " + laptopModel);

            yandexPage.getSearch(laptopModel);
            assertTrue(yandexPage.isTargetPresent(laptopModel), "Target Notebook doesn't shown on search page");
        } else {
            System.out.println("Модель ноутбука не найдена.");
        }
    }
}
