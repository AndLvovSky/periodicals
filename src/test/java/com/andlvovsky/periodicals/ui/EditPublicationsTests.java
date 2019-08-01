package com.andlvovsky.periodicals.ui;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EditPublicationsTests extends UiTests {

    private static final String URL = "/edit";

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void setup() {
        UiTests.setup();
    }

    @Before
    public void beforeEach() {
        super.beforeEach();
        loginAsAdmin();
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void showsAllPublications() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void notAdminShouldNotSeeEditPage() {
        logout();
        loginAsUser();
        open(URL);
        assertThat($("body").getText()).contains("Access denied");
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void selectsTheSecondPublication() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
        String secondId = $("#ptbody tr:nth-child(2) td:first-child").getText();
        $("#publicationId").setValue(secondId);
        $("#selectPublication").click();
        $$("#ptbody tr").shouldHaveSize(1);
        assertThat($("#ptbody").getText()).contains("30");
        assertThat($("#ptbody").getText()).doesNotContain("11");
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void selectPublicationFails() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
        $("#publicationId").setValue("999");
        $("#selectPublication").click();
        $("#info").shouldHave(cssClass("bad"));
        $("#info").shouldNotHave(cssClass("d-none"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void deletesTheForthPublication() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
        String forthId = $("#ptbody tr:nth-child(4) td:first-child").getText();
        $("#publicationId").setValue(forthId);
        $("#deletePublication").click();
        $$("#ptbody tr").shouldHaveSize(5);
        assertThat($("#ptbody").getText()).doesNotContain("St. Louis Post-Dispatch");
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void deletePublicationFails() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
        $("#publicationId").setValue("888");
        $("#deletePublication").click();
        $("#info").shouldHave(cssClass("bad"));
        $("#info").shouldNotHave(cssClass("d-none"));
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void addsNewPublication() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
        enterPublication();
        $("#addPublication").click();
        $$("#ptbody tr").shouldHaveSize(7);
        assertThat($("#ptbody").getText()).contains("The Guardian");
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void addPublicationFails() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
        enterInvalidPublication();
        $("#addPublication").click();
        $$("#ptbody tr").shouldHaveSize(6);
        $("#periodPublicationError").shouldNotBe(empty);
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void replacesTheThirdPublication() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
        String thirdId = $("#ptbody tr:nth-child(3) td:first-child").getText();
        $("#publicationId").setValue(thirdId);
        enterPublication();
        $("#replacePublication").click();
        $("#ptbody").shouldNotHave(matchText(".*Philadelphia Inquirer.*"));
        assertThat($("#ptbody").getText()).contains("The Guardian");
    }

    @Test
    @DataSet("datasets/dataUi.json")
    public void replacePublicationFails() {
        open(URL);
        $$("#ptbody tr").shouldHaveSize(6);
        enterInvalidPublication();
        $("#replacePublication").click();
        $$("#ptbody tr").shouldHaveSize(6);
        $("#periodPublicationError").shouldNotBe(empty);
    }

    public int getPort() {
        return port;
    }

    private void enterPublication() {
        $("#publicationName").setValue("The Guardian");
        $("#publicationPeriod").setValue("7");
        $("#publicationCost").setValue("5.5");
        $("#publicationDescription").setValue("-");
    }

    private void enterInvalidPublication() {
        $("#publicationName").setValue("The Sun");
        $("#publicationPeriod").setValue("-7");
        $("#publicationCost").setValue("10.5");
        $("#publicationDescription").setValue("-");
    }

}
