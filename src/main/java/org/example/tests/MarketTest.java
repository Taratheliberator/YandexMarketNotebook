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
/**
 * Класс MarketTest содержит тесты для проверки функциональности фильтрации ноутбуков на Яндекс Маркете.
 * Тесты параметризованы для возможности проверки различных комбинаций фильтров.
 */
public class MarketTest extends TestBase {

    private YandexPage yandexPage;
    /**
     * Начальная настройка для каждого теста.
     * Инициализирует страницу YandexPage.
     */
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        yandexPage = app.yandex();
    }
    /**
     * Тестирование функционала фильтрации ноутбуков на Яндекс Маркете.
     * Параметризованный тест, который выполняется для различных производителей и ценовых диапазонов.
     *
     * @param vendors   Список производителей для фильтрации.
     * @param priceFrom Нижняя граница ценового диапазона.
     * @param priceTo   Верхняя граница ценового диапазона.
     * @throws InterruptedException в случае прерывания потока во время ожидания.
     */
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
    /**
     * Предоставляет данные для параметризованных тестов.
     * Метод возвращает поток аргументов, содержащих различные комбинации производителей и ценовых диапазонов.
     *
     * @return Поток данных для тестирования.
     */
    private static Stream<Arguments> provideDataForMarketTest() {
        return Stream.of(
                Arguments.of(Arrays.asList("HP", "Lenovo"), 10000, 30000)

        );
    }
}











