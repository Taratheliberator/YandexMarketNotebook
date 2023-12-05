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
/**
 * Класс YandexPage представляет собой объектную модель веб-страницы Яндекс Маркета.
 * Он предоставляет методы для взаимодействия с элементами веб-страницы и выполнения различных действий,
 * таких как навигация по разделам, применение фильтров и поиск товаров.
 */

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
    /**
     * Конструктор класса YandexPage.
     * @param wd WebDriver, используемый для управления браузером.
     */
    public YandexPage(WebDriver wd) {
        PageFactory.initElements(wd, this);
        this.wd = wd;
    }
    /**
     * Выполняет переход в раздел Маркета на Яндексе.
     * @return Экземпляр YandexPage для возможности цепочечного вызова методов.
     */
    public YandexPage goToMarket() {
        marketButton.click();
        return this;
    }
    /**
     * Выполняет переход в раздел "Ноутбуки и компьютеры" на Яндекс Маркете.
     * Этот метод кликает по соответствующему элементу на странице, инициируя переход.
     *
     * @return Экземпляр YandexPage для возможности цепочечного вызова методов.
     */
    @Step("Переход в раздел ноутбуков и компьютеров")
    public YandexPage goToComputers() {
        computersButton.click();
        return this;
    }
    /**
     * Выполняет переход к списку ноутбуков на Яндекс Маркете.
     * Этот метод кликает по ссылке "Ноутбуки", инициируя переход к списку ноутбуков.
     *
     * @return Экземпляр YandexPage для возможности цепочечного вызова методов.
     */
    @Step("Переход к списку ноутбуков")
    public YandexPage goToNotebooks() {
        notebooksButton.click();
        return this;
    }
    /**
     * Открывает интерфейс фильтров на странице с ноутбуками Яндекс Маркета.
     * Этот метод кликает по кнопке "Все фильтры", позволяя пользователю установить желаемые параметры фильтрации.
     *
     * @return Экземпляр YandexPage для возможности цепочечного вызова методов.
     */
    @Step("Открытие фильтров")
    public YandexPage openFilter() {
        allFilters.click();
        return this;
    }
    /**
     * Нажимает на кнопку для отображения результатов фильтра на Яндекс Маркете.
     * Этот метод используется после установки всех необходимых фильтров поиска,
     * чтобы инициировать поиск и отобразить результаты на странице.
     *
     * @return Экземпляр YandexPage для возможности цепочечного вызова методов.
     */
    @Step("Отображение результатов фильтра")
    public YandexPage showResults() {
        resultsButton.click();
        return this;
    }
    /**
     * Выбирает производителя для фильтрации результатов поиска.
     * @param name Имя производителя для фильтрации.
     * @return Экземпляр YandexPage для возможности цепочечного вызова методов.
     */
    @Step("Выбор производителя: {name}")
    public YandexPage setVendorName(String name) {
        WebElement vendorName = wd.findElement(By.xpath(String.format("//label[contains(.,'%s')]", name)));
        JavascriptExecutor js = (JavascriptExecutor) wd;
        js.executeScript("arguments[0].scrollIntoView(true);", vendorName);
        vendorName.click();

        return this;
    }
    /**
     * Проверяет, присутствует ли указанный элемент на странице.
     * @param name Имя элемента для проверки.
     * @return true, если элемент присутствует и видим, иначе false.
     */
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

    @Step("Установка нижнего предела цены: {i}")
    public YandexPage setDownRange(int i) {
        rangeFrom.click();
        rangeFrom.sendKeys(Integer.toString(i));
        return this;
    }
    /**
     * Устанавливает верхний предел цены для фильтрации результатов поиска на Яндекс Маркете.
     * Этот метод вводит указанное значение в поле для верхнего предела цены.
     *
     * @param i Верхний предел цены для фильтрации.
     * @return Экземпляр YandexPage для возможности цепочечного вызова методов.
     */
    @Step("Установка верхнего предела цены: {i}")
    public YandexPage setUpRange(int i) {
        rangeTo.click();
        rangeTo.sendKeys(Integer.toString(i));
        return this;
    }
    /**
     * Возвращает список веб-элементов, соответствующих ноутбукам на странице результатов поиска.
     * Этот метод используется для получения всех элементов списка ноутбуков, доступных на текущей странице.
     *
     * @return Список веб-элементов, представляющих ноутбуки.
     */
    public List<WebElement> getList() {
        return noteList;
    }
    /**
     * Выполняет поиск по заданному запросу на Яндекс Маркете.
     * Метод вводит запрос в поле поиска и нажимает кнопку поиска, инициируя поиск.
     *
     * @param target Поисковый запрос для выполнения.
     */
    public void getSearch(String target) {
        searchField.click();
        searchField.sendKeys(target);
        searchButton.click();
    }
    /**
     * Загружает первую страницу списка ноутбуков на Яндекс Маркете.
     * Метод прокручивает страницу вниз для загрузки элементов, повторяя действие до тех пор,
     * пока не будут загружены все доступные на первой странице элементы или не будет достигнуто максимальное количество попыток.
     * После загрузки проверяется, что количество элементов превышает 12.
     *
     * @throws AssertionError если количество элементов на первой странице меньше или равно 12.
     */
    @Step("Загрузка первой страницы ноутбуков и проверка на загрузку больше 12 штук")
    public void loadFirstPageNotebooks() {
        JavascriptExecutor js = (JavascriptExecutor) wd;
        int maxAttempts = 20;
        int attempt = 0;

        while (true) {
            List<WebElement> currentItems = wd.findElements(By.xpath("//*[@data-autotest-id='offer-snippet' or @data-autotest-id='product-snippet']"));
            int initialCount = currentItems.size();
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<WebElement> newItems = wd.findElements(By.xpath("//*[@data-autotest-id='offer-snippet' or @data-autotest-id='product-snippet']"));
            if (newItems.size() > initialCount) {
//                for (WebElement item : newItems.subList(initialCount, newItems.size())) {
//                    System.out.println("Новый элемент: " + item.getText());
//                }
                continue;
            }
            attempt++;
            if (attempt >= maxAttempts) {
                // Проверка, что элементов больше 12
                assert newItems.size() > 12 : "Количество элементов меньше или равно 12";
                break;
            }
        }

    }
    /**
     * Загружает список всех ноутбуков на странице Яндекс Маркета.
     * Этот метод автоматически прокручивает страницу и нажимает на кнопку "Показать еще",
     * если она доступна, чтобы загрузить все доступные ноутбуки в текущем поисковом запросе.
     */
    @Step("Загрузка списка всех ноутбуков")
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
    /**
     * Проверяет наличие указанной модели ноутбука на странице поиска.
     * Метод ищет на странице элемент, соответствующий указанной модели, и проверяет его наличие.
     *
     * @param target Название модели ноутбука для поиска.
     * @throws AssertionError если модель ноутбука не найдена или не отображается на странице поиска.
     */
    @Step("Проверка наличия искомой модели ноутбука {target} на странице поиска:")
    public void validateFirstNotebookModel(String target) {
        Pattern pattern = Pattern.compile(".*(Ноутбук|ноутбук) [^\\n]+");
        Matcher matcher = pattern.matcher(target);

        assertTrue(matcher.find(), "Модель ноутбука не найдена " + target);

        String laptopModel = matcher.group();
        System.out.println("\nИскомая модель ноутбука: " + laptopModel);
        getSearch(laptopModel);
        assertTrue(isTargetPresent(laptopModel), "Ноутбук " + laptopModel + " не показан на странице поиска");
    }
    /**
     * Определяет, соответствует ли указанный ноутбук заданным критериям фильтрации.
     * @param element Строка, содержащая информацию о ноутбуке.
     * @return true, если ноутбук соответствует условиям фильтрации, иначе false.
     */
    @Step("Проверка ноутбуков на соответствие условиям фильтра ")
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
    /**
     * Ищет первую цену в тексте.
     * @param text Текст, в котором необходимо найти цену.
     * @return Строку с первой найденной ценой или сообщение о том, что цена не найдена.
     */
    public static String findFirstPrice(String text) {

        Pattern pattern = Pattern.compile("\\d{1,3}(?:[\\s ,\\xA0\u2009]*\\d{3})*[\\s ,\\xA0\u2009]*₽");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(0).replaceAll("[^\\d]", "");
        } else {
            return "Цена не найдена";
        }
    }
}
