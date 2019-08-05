package com.andlvovsky.periodicals.ui;

import com.andlvovsky.periodicals.meta.ClientPages;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CatalogTests extends UiTests {

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

    @Test
    @DataSet("datasets/dataUi.json")
    public void showsAllPublications() {
        open(ClientPages.PUBLICATIONS_VIEW);
        $$("div.publication").shouldHaveSize(6);
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void addsItemsToBasket() {
        open(ClientPages.PUBLICATIONS_VIEW);
        $("#pn102").setValue("3");
        $("#ap102").click();
        $("#pn104").setValue("1");
        $("#ap104").click();
        $("#basketItemsNumber").shouldHave(text("2"));
    }

    public int getPort() {
        return port;
    }

}
