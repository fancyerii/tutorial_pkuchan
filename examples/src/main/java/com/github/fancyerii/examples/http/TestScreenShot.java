package com.github.fancyerii.examples.http;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.google.common.io.Files;

public class TestScreenShot {

  public static void main(String[] args) throws Exception {
    System.setProperty("webdriver.chrome.driver", "/home/mc/soft/chromedriver");
    ChromeOptions options = new ChromeOptions();
    WebDriver driver = new ChromeDriver(options);
    driver.get("https://www.qq.com");
    Thread.sleep(3000);
    File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    // Now you can do whatever you need to do with it, for example copy
    // somewhere
    Files.copy(scrFile, new File("screenshot.png"));
    driver.quit();
  }

}
