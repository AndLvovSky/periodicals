package com.andlvovsky.periodicals.ui;

import com.andlvovsky.periodicals.meta.ClientPages;
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
    }

    @After
    public void afterEach() {
        logout();
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void showsBasketContent() {
        addBasketItems();
        open(ClientPages.BASKET);
        $$("#basketItems tr").shouldHaveSize(2);
        $("#basketItems tr:nth-child(2) td:nth-child(1)").shouldHave(text("Philadelphia Inquirer"));
        $("#basketItems tr:nth-child(2) td:nth-child(2)").shouldHave(text("2"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void showsBasketCost() {
        addBasketItems();
        open(ClientPages.BASKET);
        $$("#basketItems tr").shouldHaveSize(2);
        $("#basketCost").shouldHave(text("50.00$"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void redirectsToRegisterSuccess() {
        addBasketItems();
        open(ClientPages.BASKET);
        $("#register").click();
        redirectsTo(ClientPages.REGISTRATION_SUCCESS);
        $("#basketCost").shouldHave(text("50.00$"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void registrationFailsEmptyBasket() {
        open(ClientPages.BASKET);
        $("#register").submit();
        redirectsTo(ClientPages.BASKET + "?registrationError");
        $("#registrationError").isDisplayed();
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void deletesTheSecondBasketItem() {
        addBasketItems();
        open(ClientPages.BASKET);
        $$("#basketItems tr").shouldHaveSize(2);
        $("#basketItems tr:nth-child(2) td:nth-child(3) button").click();
        $$("#basketItems tr").shouldHaveSize(1);
        $("#basketItems tr:nth-child(1) td:nth-child(2)").shouldHave(text("6"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void deletesAllBasketItems() {
        addBasketItems();
        open(ClientPages.BASKET);
        $$("#basketItems tr").shouldHaveSize(2);
        $("#clear").click();
        $$("#basketItems tr").shouldHaveSize(0);
    }

    @Override
    public int getPort() {
        return port;
    }

    private void addBasketItems() {
        open(ClientPages.PUBLICATIONS_VIEW);
        $$("div.publication").shouldHaveSize(6);
        $("#pn101").setValue("6");
        $("#ap101").click();
        $("#pn103").setValue("2");
        $("#ap103").click();
        $("#basketItemsNumber").shouldHave(text("2"));
    }

    private void redirectsTo(String url) {
        Wait().until(ExpectedConditions.urlToBe(Configuration.baseUrl + url));
    }

}
