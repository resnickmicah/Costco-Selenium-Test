package com.lazerycode.selenium.tests;

import java.time.Duration;

import com.lazerycode.selenium.DriverBase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TestCostcoPage extends DriverBase {

    @Test
    public void interactWithPage() throws Exception {
        WebDriver driver = getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://localhost:8080/page.html");

        // We can wait for specific elements we're interested in interacting with to be ready,
        // or we can query the document's state by JS.
        // if doing the former, I would use a page object and have a method for querying for all the elements we're interested in
        // but I'll go with the latter because I've already spent too long on this:
        JavascriptExecutor jsex = (JavascriptExecutor) driver;
        assertThat(jsex.executeScript("return document.readyState")).isEqualTo(("complete"));

        // this is really brittle, and you're better off with elements having test ID attributes.
        // But this is how I'd do it in the absence of those, since I think indexing to a particular
        // Text input element from an array of them is even more brittle.
        // See https://webdriver.io/docs/selectors/ for an opinionated list of selectors by brittleness
        WebElement textInput = driver.findElement(By.xpath("//*[contains(text(), 'Text Box 2')]/..//input"));
        textInput.sendKeys("test");

        WebElement radioThree = driver.findElement(By.id("radio3"));
        // This will fail with the HTML as written, barring some other interaction with the page from the test script
        assertThat(radioThree.isSelected()).isTrue();

        WebElement submitButton = driver.findElement(By.xpath("//button[@type='button'][contains(text(), 'Submit')]"));
        submitButton.click();

        // as far as CSS vs. XPath selectors, I generally prefer CSS selectors when working with 
        // a front end team who will maintain these tests, since front end devs already know CSS,
        // but at my last job we were using XPath so that's what I went with because I remember it better.
    }
}
