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
    void shouldSendFormWithInvalidName() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Ivan Ivanov");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79770001122");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String textError = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertTrue(textError.contains("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSendFormWithInvalidPhone() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+7977000");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String textError = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertTrue(textError.contains("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSendFormWithInvalidCheckbox() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79770001122");
        driver.findElement(By.tagName("button")).click();
        String attribute = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getAttribute("class");
        assertTrue(attribute.contains("input_invalid"));
    }

    @Test
    void shouldSendFormWithEmptyFields() {
        driver.findElement(By.tagName("button")).click();
        String elementClass = driver.findElement(By.cssSelector("[data-test-id='name']")).getAttribute("class");
        assertTrue(elementClass.contains("input_invalid"));
    }

    @Test
    void shouldSendFormWithEmptyName() {
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79770001122");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String textError = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertTrue(textError.contains("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithEmptyPhone() {
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String textError = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertTrue(textError.contains("Поле обязательно для заполнения"));
    }
}
