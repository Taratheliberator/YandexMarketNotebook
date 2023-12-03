package org.example.tests;

import io.qameta.allure.Step;
import org.example.pageobjects.YandexPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.Assertions.assertTrue;

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
        System.out.println("\nКоличество загруженных ноутбуков: " + firstPageNotebooks.size());
        String target = firstPageNotebooks.get(0).getText();
        System.out.println("\nПервый элемент:\n" + target);

        yandexPage.loadAllNotebooks();
        List<WebElement> allNotebooks = yandexPage.getList();
        for (WebElement notebook : allNotebooks) {
            String notebookInfo = notebook.getText();
            boolean isValid = isLaptopValid(notebookInfo);

            System.out.println("Ноутбук: " + notebookInfo);
            System.out.println("Валиден: " + isValid);
        }


        validateFirstNotebookModel(target);

    }


    private void validateFirstNotebookModel(String target) {
        Pattern pattern = Pattern.compile(".*(Ноутбук|ноутбук) [^\\n]+");

        Matcher matcher = pattern.matcher(target);

        assertTrue(matcher.find(), "Модель ноутбука не найдена " + target);

        String laptopModel = matcher.group();
        System.out.println("\nНайденная модель ноутбука: " + laptopModel);
        yandexPage.getSearch(laptopModel);
        assertTrue(yandexPage.isTargetPresent(laptopModel), "Ноутбук " + laptopModel + " не показан на странице поиска");
    }

    public static boolean isLaptopValid(String element) {
        String lowerCaseElement = element.toLowerCase();
        String manufacturer = "";
        if (lowerCaseElement.contains("hp")) {
            manufacturer = "HP";
        } else if (lowerCaseElement.contains("lenovo")) {
            manufacturer = "Lenovo";
        }

        // Использование функции findFirstPrice для нахождения первой цены
        String firstPriceStr = findFirstPrice(element);
        System.out.println("firstPriceStr" + firstPriceStr);
        // Проверка на наличие производителя
        if (manufacturer.isEmpty() || firstPriceStr.equals("Цена не найдена")) {
            return false;
        }

        // Проверка, что цена находится в диапазоне от 10000 до 30000
        try {
            int firstPrice = Integer.parseInt(firstPriceStr.replaceAll("[^\\d]", ""));
            System.out.println("firstPrice" + firstPrice);
            return firstPrice >= 10000 && firstPrice <= 30000;
        } catch (NumberFormatException e) {
            System.out.println("Цена: Невозможно определить (неверный формат)");
            return false;
        }
    }

    public static String findFirstPrice(String text) {
        // Регулярное выражение для поиска цены
        // Учитывает различные форматы чисел, включая специальные символы
        Pattern pattern = Pattern.compile("\\d{1,3}(?:[\\s ,\\xA0]?\\d{3})*\\s?₽");
        Matcher matcher = pattern.matcher(text);

        // Поиск первой цены
        if (matcher.find()) {
            // Возвращаем цену, удаляя все, кроме цифр
            return matcher.group(0).replaceAll("[^\\d]", "");
        } else {
            return "Цена не найдена";
        }
    }





}
