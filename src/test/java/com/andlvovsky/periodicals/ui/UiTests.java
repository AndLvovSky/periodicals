package com.andlvovsky.periodicals.ui;

import com.andlvovsky.periodicals.meta.ClientPages;
import com.andlvovsky.periodicals.repository.RepositoryTests;
import com.codeborne.selenide.Configuration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public abstract class UiTests extends RepositoryTests {

    public abstract int getPort();

    public static void setup() {
        Configuration.timeout = 10000;
    }

    public void beforeEach() {
        Configuration.baseUrl = baseUrl(getPort());
    }

    public static void loginAsAdmin() {
        loginWithCredentials("a", "p");
    }

    public static void loginAsUser() {
        loginWithCredentials("u", "p");
    }

    public static void logout() {
        open(ClientPages.LOGOUT);
    }

    public static void loginWithCredentials(String name, String password) {
        open(ClientPages.LOGIN);
        $("#username").setValue(name);
        $("#password").setValue(password);
        $("#submit").click();
    }

    public static String baseUrl(int port) {
        return "http://localhost:" + port;
    }

}
