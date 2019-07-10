package com.andlvovsky.periodicals;

import com.codeborne.selenide.Configuration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;

// should reset database after each test to prevent order dependencies
// but @Transactional don't work with SpringBootTest with DEFINED_PORT
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EditPublicationsTests extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void setup() {
        Configuration.timeout = 10000;
    }

    @Test(priority = 1)
    public void shouldShowAllPublications() {
        open(url());
        $$("#ptbody tr").shouldHaveSize(6);
    }

    @Test(priority = 2)
    public void shouldSelectTheSecondPublication() {
        open(url());
        $$("#ptbody tr").shouldHaveSize(6);
        String secondId = $("#ptbody tr:nth-child(2) td:first-child").getText();
        $("#publicationId").setValue(secondId);
        $("#selectPublication").click();
        $$("#ptbody tr").shouldHaveSize(1);
        assertThat($("#ptbody").getText()).contains("30");
        assertThat($("#ptbody").getText()).doesNotContain("11");
    }

    @Test(priority = 3)
    public void shouldDeleteTheForthPublication() {
        open(url());
        $$("#ptbody tr").shouldHaveSize(6);
        String forthId = $("#ptbody tr:nth-child(4) td:first-child").getText();
        $("#publicationId").setValue(forthId);
        $("#deletePublication").click();
        $$("#ptbody tr").shouldHaveSize(5);
        assertThat($("#ptbody").getText()).doesNotContain("St. Louis Post-Dispatch");
    }

    @Test(priority = 4)
    public void shouldAddNewPublication() {
        open(url());
        enterPublication();
        $("#addPublication").click();
        $$("#ptbody tr").shouldHaveSize(6);
        assertThat($("#ptbody").getText()).contains("The Guardian");
    }

    @Test(priority = 5)
    public void shouldReplaceTheThirdPublication() {
        open(url());
        $$("#ptbody tr").shouldHaveSize(6);
        String thirdId = $("#ptbody tr:nth-child(3) td:first-child").getText();
        $("#publicationId").setValue(thirdId);
        enterPublication();
        $("#replacePublication").click();
        $("#ptbody").shouldNotHave(matchText(".*Philadelphia Inquirer.*"));
        assertThat($("#ptbody").getText()).contains("The Guardian");
    }

    private void enterPublication() {
        $("#publicationName").setValue("The Guardian");
        $("#publicationFrequency").setValue("7");
        $("#publicationCost").setValue("5.5");
        $("#publicationDescription").setValue("-");
    }

    private String url() {
        return "http://localhost:" + port + "/edit.html";
    }

}
