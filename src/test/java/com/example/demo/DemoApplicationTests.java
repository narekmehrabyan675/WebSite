package com.example.demo;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() throws InterruptedException {
		System.setProperty("web.chrome.driver" , "D:\\ProgrammingFiles\\chromedriver\\chrome.dll");
		WebDriver driver = new ChromeDriver();


		driver.get("http://localhost:8080/auth/login");

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		WebElement input1 = (new WebDriverWait(driver , Duration.ofSeconds(10))
				.until(ExpectedConditions.presenceOfElementLocated(By.id("username"))));
		input1.click();
		input1.sendKeys("admin");

		WebElement input2 = (new WebDriverWait(driver , Duration.ofSeconds(10))
				.until(ExpectedConditions.presenceOfElementLocated(By.id("password"))));
		input2.click();
		input2.sendKeys("nar");


		WebElement input3 = driver.findElement(By.id("submit"));
		input3.click();
		input3.submit();
	}

}
