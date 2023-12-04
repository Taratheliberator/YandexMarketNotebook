package org.example.tests;

import io.qameta.allure.Step;
import org.example.pageobjects.YandexPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

//import static org.example.Assertions.assertTrue;

public class MarketTest extends TestBase {

    private static YandexPage yandexPage;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        yandexPage = app.yandex();
    }

    @Test
    public void pageMarketTest() throws InterruptedException {
        executeMarketTest();
    }


    private void executeMarketTest() throws InterruptedException {
        yandexPage.goToMarket()
                .goToComputers()
                .goToNotebooks()
                .openFilter()
                .setDownRange(10000)
                .setUpRange(30000)
                .setVendorName("HP")
                .setVendorName("Lenovo")
                .showResults()
                .loadFirstPageNotebooks();
        List<WebElement> firstPageNotebooks = yandexPage.getList();
        System.out.println("\nКоличество загруженных ноутбуков на первой странице: " + firstPageNotebooks.size());
        String target = firstPageNotebooks.get(0).getText();
        System.out.println("\nПервый элемент:\n" + target);

        yandexPage.loadAllNotebooks();
        List<WebElement> allNotebooks = yandexPage.getList();
        System.out.println("\nКоличество загруженных ноутбуков на всех страницах: " + firstPageNotebooks.size());
        for (WebElement notebook : allNotebooks) {
            String notebookInfo = notebook.getText();

            assertTrue(yandexPage.isLaptopValid(notebookInfo), "Ноутбук невалиден: " + notebookInfo);
        }
        yandexPage.validateFirstNotebookModel(target);
    }












}
