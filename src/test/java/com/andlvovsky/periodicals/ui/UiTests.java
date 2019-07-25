package com.andlvovsky.periodicals.ui;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class UiTests {

    public static void loginAsAdmin() {
        loginWithCredentials("a", "p");
    }

    public static void loginAsUser() {
        loginWithCredentials("u", "p");
    }

    public static void logout() {
        open("/logout");
    }

    public static void loginWithCredentials(String name, String password) {
        open("/login");
        $("#username").setValue(name);
        $("#password").setValue(password);
        $("#submit").click();
    }

}
