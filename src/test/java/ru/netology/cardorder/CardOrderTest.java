package ru.netology.cardorder;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

public class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        //System.setProperty("webdriver.chrome.driver", "./driver/win/chromedriver.exe");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        driver = null;
    }

    @Test
    void shouldSendFormWithValidData() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79770001122");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text);
    }

    @Test
    void shouldFillFormWithInvalidName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Ivan Ivanov");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79770001122");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String elementClass = driver.findElement(By.cssSelector("[data-test-id='name']")).getAttribute("class");
        assertTrue(elementClass.contains("input_invalid"));
    }

    @Test
    void shouldFillFormWithInvalidPhone() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79770001122");
        driver.findElement(By.tagName("button")).click();
        String elementClass = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getAttribute("class");
        assertTrue(elementClass.contains("input_invalid"));
    }

    @Test
    void shouldFillFormWithInvalidCheckbox() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+7977000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String elementClass = driver.findElement(By.cssSelector("[data-test-id='phone']")).getAttribute("class");
        assertTrue(elementClass.contains("input_invalid"));
    }

    @Test
    void shouldFillFormWithEmptyFields() {
        driver.findElement(By.tagName("button")).click();
        String elementClass = driver.findElement(By.cssSelector("[data-test-id='name']")).getAttribute("class");
        assertTrue(elementClass.contains("input_invalid"));
    }
}
