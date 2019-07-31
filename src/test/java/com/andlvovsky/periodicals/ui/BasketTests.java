package com.andlvovsky.periodicals.ui;

import com.codeborne.selenide.Configuration;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BasketTests extends UiTests {

    private static final String BASKET_URL = "/basket";

    private static final String CATALOG_URL = "/catalog";

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void setup() {
        UiTests.setup();
    }

    @Before
    public void beforeEach() {
        super.beforeEach();
        loginAsUser();
        addBasketItems();
    }

    @After
    public void afterEach() {
        logout();
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void showsBasketContent() {
        open(BASKET_URL);
        $$("#basketItems tr").shouldHaveSize(2);
        $("#basketItems tr:nth-child(2) td:nth-child(1)").shouldHave(text("Philadelphia Inquirer"));
        $("#basketItems tr:nth-child(2) td:nth-child(2)").shouldHave(text("2"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void showsBasketCost() {
        open(BASKET_URL);
        $$("#basketItems tr").shouldHaveSize(2);
        $("#basketCost").shouldHave(text("50.00$"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void redirectsToRegisterSuccess() {
        open(BASKET_URL);
        $("#register").click();
        redirectsTo("/register-success");
        $("#basketCost").shouldHave(text("50.0"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void deletesTheSecondBasketItem() {
        open(BASKET_URL);
        $$("#basketItems tr").shouldHaveSize(2);
        $("#basketItems tr:nth-child(2) td:nth-child(3) button").click();
        $$("#basketItems tr").shouldHaveSize(1);
        $("#basketItems tr:nth-child(1) td:nth-child(2)").shouldHave(text("6"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void deletesAllBasketItems() {
        open(BASKET_URL);
        $$("#basketItems tr").shouldHaveSize(2);
        $("#clear").click();
        $$("#basketItems tr").shouldHaveSize(0);
    }

    public int getPort() {
        return port;
    }

    private void addBasketItems() {
        open(CATALOG_URL);
        $$("div.publication").shouldHaveSize(6);
        $("#pn101").setValue("6");
        $("#ap101").click();
        $("#pn103").setValue("2");
        $("#ap103").click();
    }

    private void redirectsTo(String url) {
        Wait().until(ExpectedConditions.urlToBe(Configuration.baseUrl + url));
    }

}
