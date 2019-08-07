package com.andlvovsky.periodicals.ui;

import com.codeborne.selenide.Configuration;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoginTests extends UiTests {

    @LocalServerPort
    private int port;

    private String homeUrl;

    @BeforeClass
    public static void setup() {
        UiTests.setup();
    }

    @Before
    public void beforeEach() {
        super.beforeEach();
        homeUrl = Configuration.baseUrl + "/";
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void successfulLoginAsUser() {
        loginAsUser();
        backToHome();
        $("#login").shouldNotHave(exist);
        $("#logout").isDisplayed();
        $("#edit").shouldNotHave(exist);
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void successfulLoginAsAdmin() {
        loginAsAdmin();
        backToHome();
        $("#login").shouldNotHave(exist);
        $("#logout").isDisplayed();
        $("#edit").isDisplayed();
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void loginFails() {
        loginWithCredentials("wrong-name", "wrong-password");
        $("error-credentials").isDisplayed();
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void successfulLogout() {
        loginAsUser();
        logout();
        backToHome();
        checkUnauthenticatedHomePageElements();
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void unauthenticatedHomePage() {
        open("");
        checkUnauthenticatedHomePageElements();
    }

    @Override
    public int getPort() {
        return port;
    }

    private void checkUnauthenticatedHomePageElements() {
        $("#login").isDisplayed();
        $("#logout").shouldNotHave(exist);
        $("#edit").shouldNotHave(exist);
    }

    private void backToHome() {
        Wait().until(ExpectedConditions.urlToBe(homeUrl));
    }

}
