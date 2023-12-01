package org.example.pageobjects;



import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

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
        vendorName.click();
        return this;
    }

    public boolean isTargetPresent(String name) {
        WebElement note = wd.findElement(By.xpath(String.format("//span[contains(text(),'%s')]", name)));
        return note != null;
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

    public YandexPage showDozen() throws InterruptedException {
        showAs.click();
        showAs.sendKeys(Keys.UP, Keys.ENTER);
        Thread.sleep(2000);
        return this;
    }

    public List<WebElement> getList() {
        return noteList;
    }

    public String getSearch(String target) {
        searchField.click();
        searchField.sendKeys(target);
        searchButton.click();
        return (noteList.get(0).getAttribute("title"));
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

    // Removed the switchToNextPage method and the static app reference.
}
