package org.example.pageobjects;



import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class YandexPage {

    private WebDriver wd;

    @FindBy(xpath = "//span[contains(text(),'Каталог')]")
    private WebElement marketButton;

    @FindBy(xpath = "//span[contains(text(),'Ноутбуки и компьютеры')]")
    private WebElement computersButton;

    @FindBy(linkText = "Ноутбуки")
    private WebElement notebooksButton;

    @FindBy(xpath = "//span[contains(.,'Все фильтры')]")
    private WebElement allFilters;

    @FindBy(xpath = "//*[@id='glprice']//div[@data-prefix='от']//input")
    private WebElement rangeFrom;

    @FindBy(xpath = "//*[@id='glprice']//div[@data-prefix='до']//input")
    private WebElement rangeTo;

    @FindBy(xpath = "//a[contains(text(),'Показать')]")
    private WebElement resultsButton;

    @FindBy(xpath = "//button[contains(.,'Показывать по')]")
    private WebElement showAs;

    @FindBy(name = "text")
    private WebElement searchField;

    @FindBy(xpath = "//*[@data-autotest-id='offer-snippet' or @data-autotest-id='product-snippet']")
    private List<WebElement> noteList;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(css = ".n-search-preciser__text")
    private WebElement endSearch;

    public YandexPage(WebDriver wd) {
        PageFactory.initElements(wd, this);
        this.wd = wd;
    }
    @Step("Нажатие на каталог")
    public YandexPage goToMarket() {
        marketButton.click();
        return this;
    }

    public YandexPage goToComputers() {
        computersButton.click();
        return this;
    }

    public YandexPage goToNotebooks() {
        notebooksButton.click();
        return this;
    }

    public YandexPage openFilter() {
        allFilters.click();
        return this;
    }

    public YandexPage showResults() {
        resultsButton.click();
        return this;
    }

    public YandexPage setVendorName(String name) {
        WebElement vendorName = wd.findElement(By.xpath(String.format("//label[contains(.,'%s')]", name)));

        // Прокрутка страницы с помощью JavaScript
        JavascriptExecutor js = (JavascriptExecutor) wd;
        js.executeScript("arguments[0].scrollIntoView(true);", vendorName);

        // Клик по элементу
        vendorName.click();

        return this;
    }


    public boolean isTargetPresent(String name) {
        WebDriverWait wait = new WebDriverWait(wd, 10);


        Actions actions = new Actions(wd);
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();


        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));


        try {
            WebElement note = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//span[contains(text(),'%s')]", name))));
            return note != null && note.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public YandexPage setDownRange(int i) {
        rangeFrom.click();
        rangeFrom.sendKeys(Integer.toString(i));
        return this;
    }

    public YandexPage setUpRange(int i) {
        rangeTo.click();
        rangeTo.sendKeys(Integer.toString(i));
        return this;
    }

    public List<WebElement> getList() {
        return noteList;
    }

    public void  getSearch(String target) {
        searchField.click();
        searchField.sendKeys(target);
        searchButton.click();
    }
    @Step("Выполнение loadFirstPageNotebooks")
    public void loadFirstPageNotebooks() {
        JavascriptExecutor js = (JavascriptExecutor) wd;
        int maxAttempts = 20; // Максимальное количество попыток
        int attempt = 0; // Счетчик попыток

        while (true) {
            List<WebElement> currentItems = wd.findElements(By.xpath("//*[@data-autotest-id='offer-snippet' or @data-autotest-id='product-snippet']"));
            int initialCount = currentItems.size();

            // Прокрутка до конца страницы
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            // Задержка перед следующей проверкой
            try {
                Thread.sleep(500); // Задержка в полсекунды
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Проверка наличия новых элементов
            List<WebElement> newItems = wd.findElements(By.xpath("//*[@data-autotest-id='offer-snippet' or @data-autotest-id='product-snippet']"));
            if (newItems.size() > initialCount) {

//                for (WebElement item : newItems.subList(initialCount, newItems.size())) {
//                    System.out.println("Новый элемент: " + item.getText());
//                }
                continue; // Если появились новые элементы, продолжаем цикл
            }

            attempt++;
            if (attempt >= maxAttempts) {
                // Проверка, что элементов больше 12
                assert newItems.size() > 12 : "Количество элементов меньше или равно 12";
                break; // Если достигнуто максимальное количество попыток, выходим из цикла
            }
        }

        // Продолжение выполнения кода после выхода из цикла
    }

    public void loadAllNotebooks() {
        WebDriverWait wait = new WebDriverWait(wd, 10);
        while (true) {
            List<WebElement> showMoreButtons = wd.findElements(By.cssSelector("button[data-auto='pager-more']"));
            if (!showMoreButtons.isEmpty()) {
                WebElement showMoreButton = showMoreButtons.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(showMoreButton));
                showMoreButton.click();
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.invisibilityOf(showMoreButton),
                        ExpectedConditions.elementToBeClickable(showMoreButton)
                ));
            } else {
                break;
            }
        }
    }

    public void validateFirstNotebookModel(String target) {
        Pattern pattern = Pattern.compile(".*(Ноутбук|ноутбук) [^\\n]+");

        Matcher matcher = pattern.matcher(target);

        assertTrue(matcher.find(), "Модель ноутбука не найдена " + target);

        String laptopModel = matcher.group();
        System.out.println("\nИскомая модель ноутбука: " + laptopModel);
        getSearch(laptopModel);
        assertTrue(isTargetPresent(laptopModel), "Ноутбук " + laptopModel + " не показан на странице поиска");
    }
    public static boolean isLaptopValid(String element) {
        String lowerCaseElement = element.toLowerCase();
        String manufacturer = "";
        if (lowerCaseElement.contains("hp")) {
            manufacturer = "HP";
        } else if (lowerCaseElement.contains("lenovo")) {
            manufacturer = "Lenovo";
        }

        String firstPriceStr = findFirstPrice(element);
        if (manufacturer.isEmpty() || firstPriceStr.equals("Цена не найдена")) {
            return false;
        }

        try {
            int firstPrice = Integer.parseInt(firstPriceStr.replaceAll("[^\\d]", ""));
            return firstPrice >= 10000 && firstPrice <= 30000;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static String findFirstPrice(String text) {
        // Регулярное выражение для поиска цены
        // Учитывает различные форматы чисел, включая специальные символы
        Pattern pattern = Pattern.compile("\\d{1,3}(?:[\\s ,\\xA0\u2009]*\\d{3})*[\\s ,\\xA0\u2009]*₽"

        );
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
