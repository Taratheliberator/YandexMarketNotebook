package org.example.tests;

import io.qameta.allure.Step;
import org.example.pageobjects.YandexPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.example.helpers.Assertions.assertTrue;

public class MarketTest extends TestBase {

    private YandexPage yandexPage;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        yandexPage = app.yandex();
    }

    @ParameterizedTest
    @MethodSource("provideDataForMarketTest")
    @Step("Тестирование фильтрации ноутбуков с параметрами: производители - {vendors}, цена от {priceFrom} до {priceTo}")
    public void pageMarketTest(List<String> vendors, int priceFrom, int priceTo) throws InterruptedException {
        yandexPage.goToMarket()
                .goToComputers()
                .goToNotebooks()
                .openFilter()
                .setDownRange(priceFrom)
                .setUpRange(priceTo);
        for (String vendor : vendors) {
            yandexPage.setVendorName(vendor);
        }
        yandexPage.showResults()
                .loadFirstPageNotebooks();

        List<WebElement> firstPageNotebooks = yandexPage.getList();
        System.out.println("\nКоличество загруженных ноутбуков на первой странице: " + firstPageNotebooks.size());
        String target = firstPageNotebooks.get(0).getText();
        System.out.println("\nПервый элемент:\n" + target);

        yandexPage.loadAllNotebooks();
        List<WebElement> allNotebooks = yandexPage.getList();
        System.out.println("\nКоличество загруженных ноутбуков на всех страницах: " + allNotebooks.size());
        for (WebElement notebook : allNotebooks) {
            String notebookInfo = notebook.getText();

            assertTrue(yandexPage.isLaptopValid(notebookInfo), "\nНоутбук не удовлетворяет условиям фильтра: \n" + notebookInfo);
        }
        yandexPage.validateFirstNotebookModel(target);
    }

    private static Stream<Arguments> provideDataForMarketTest() {
        return Stream.of(
                Arguments.of(Arrays.asList("HP", "Lenovo"), 10000, 30000)

        );
    }
}











