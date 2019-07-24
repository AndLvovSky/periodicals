package com.andlvovsky.periodicals.ui;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class UiTests {

    public static void loginAsAdmin() {
        open("/login");
        loginWithCredentials("a", "p");
    }

    public static void loginAsUser() {
        open("/login");
        loginWithCredentials("u", "p");
    }

    public static void logout() {
        open("/logout");
    }

    private static void loginWithCredentials(String name, String password) {
        $("input[name=\"username\"]").setValue(name);
        $("input[name=\"password\"]").setValue(password);
        $("button[type=\"submit\"]").click();
    }

}
