package com.andlvovsky.periodicals.ui;

import com.codeborne.selenide.Configuration;
import com.github.database.rider.core.DBUnitRule;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public abstract class UiTests {

    @Autowired
    private DataSource dataSource;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> dataSource.getConnection());

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
        open("/logout");
    }

    public static void loginWithCredentials(String name, String password) {
        open("/login");
        $("#username").setValue(name);
        $("#password").setValue(password);
        $("#submit").click();
    }

    public static String baseUrl(int port) {
        return "http://localhost:" + port;
    }

}
