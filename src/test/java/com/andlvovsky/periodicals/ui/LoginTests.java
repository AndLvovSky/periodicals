package com.andlvovsky.periodicals.ui;

import com.codeborne.selenide.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Selenide.$;
import static com.andlvovsky.periodicals.ui.UiTests.*;
import static com.codeborne.selenide.Selenide.Wait;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginTests {

    @BeforeClass
    public static void setup() {
        Configuration.timeout = 10000;
    }

    @Test
    public void successfulLogin() {
        loginAsUser();
        Wait().until(ExpectedConditions.urlToBe(baseUrl()));
    }

    @Test
    public void loginFails() {
        loginWithCredentials("wrong-name", "wrong-password");
        $("error-credentials").isDisplayed();
    }

    @Test
    public void successfulLogout() {
        loginAsUser();
        logout();
        $("logout-message").isDisplayed();
    }

    private String baseUrl() {
        return Configuration.baseUrl + "/";
    }

}
