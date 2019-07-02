package com.andlvovsky.periodicals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EditPublicationsTests {

    @LocalServerPort
    private int port;

    private static WebDriver browser;

    private final int MAX_WAIT = 10;

    {
        // ChromeDriver 75.0.3770.90
        System.setProperty("webdriver.chrome.driver",
                "C:\\Program Files\\webdrivers\\chromedriver.exe");
    }

    @Before
    public void setup() {
        if (browser == null)
            browser = new ChromeDriver();
    }

    @AfterClass
    public static void tearDown() {
        browser.close();
    }

    @Test
    public void testUpdate() {
        browser.get(url());
        new WebDriverWait(browser, MAX_WAIT).until(browser -> rowsCount() > 1);
        assertThat(browser.findElement(By.id("ptbody")).getText()).contains("New Jersey Star-Ledger");
    }

    @Test
    public void testSelect() {
        browser.get(url());
        JavascriptExecutor js = (JavascriptExecutor) browser;
        js.executeScript("document.getElementById('publicationId').setAttribute('value', '2')");
        new WebDriverWait(browser, MAX_WAIT).until(browser -> rowsCount() > 1);
        browser.findElement(By.id("selectPublication")).click();
        new WebDriverWait(browser, MAX_WAIT).until(browser -> rowsCount() == 1);
        assertThat(browser.findElement(By.id("ptbody")).getText()).contains("30");
        assertThat(browser.findElement(By.id("ptbody")).getText()).doesNotContain("11");
    }

    @Test
    public void testDelete() {
        browser.get(url());
        JavascriptExecutor js = (JavascriptExecutor) browser;
        js.executeScript("document.getElementById('publicationId').setAttribute('value', '4')");
        new WebDriverWait(browser, MAX_WAIT).until(
                ExpectedConditions.elementToBeClickable(By.id("deletePublication")));
        browser.findElement(By.id("deletePublication")).click();
        new WebDriverWait(browser, MAX_WAIT).until(browser -> rowsCount() == 6);
        assertThat(browser.findElement(By.id("ptbody")).getText()).doesNotContain("St. Louis Post-Dispatch");
    }

    @Test
    public void testAdd() {
        browser.get(url());
        enterPublication();
        browser.findElement(By.id("addPublication")).click();
        new WebDriverWait(browser, MAX_WAIT).until(browser -> rowsCount() == 7);
        assertThat(browser.findElement(By.id("ptbody")).getText()).contains("The Guardian");
    }

    @Test
    public void testReplace() {
        browser.get(url());
        ((JavascriptExecutor) browser)
                .executeScript("document.getElementById('publicationId').setAttribute('value', '3')");
        enterPublication();
        browser.findElement(By.id("replacePublication")).click();
        new WebDriverWait(browser, MAX_WAIT).until(browser ->
                !browser.findElement(By.id("ptbody")).getText().contains("Philadelphia Inquirer"));
        assertThat(browser.findElement(By.id("ptbody")).getText()).contains("The Guardian");
    }

    private int rowsCount() {
        return browser.findElements(By.tagName("tr")).size() - 1;
    }

    private void enterPublication() {
        browser.findElement(By.id("publicationName")).sendKeys("The Guardian");
        browser.findElement(By.id("publicationFrequency")).sendKeys("7");
        browser.findElement(By.id("publicationCost")).sendKeys("5.5");
        browser.findElement(By.id("publicationDescription")).sendKeys("-");
    }

    private String url() {
        return "http://localhost:" + port + "/edit.html";
    }

}
