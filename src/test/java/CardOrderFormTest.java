import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderFormTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSubmitFormWithValidData() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79652345615");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement successMessage = driver.findElement(By.cssSelector("[data-test-id=order-success]"));

        assertTrue(successMessage.isDisplayed(), "сообщение об успешной отправке не отображается");
        assertEquals(
                "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                successMessage.getText().trim(),
                "текст успешного сообщения не соответствует ожидаемому"
        );
    }

    @Test
    public void shouldErrorFormWithInvalidDataNameNumbers() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("45");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79652345615");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));

        assertTrue(errorMessage.isDisplayed(), "сообщение об ошибке в поле фио не отображается");
        assertEquals(
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                errorMessage.getText().
                        trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }

    @Test
    public void shouldErrorFormWithInvalidDataNameLanguage() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivanov Ivan");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79652345615");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));

        assertTrue(errorMessage.isDisplayed(), "сообщение об ошибке в поле фио не отображается");
        assertEquals(
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                errorMessage.getText().

                        trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }

    @Test
    public void shouldErrorFormWithoutDataName() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79652345615");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));

        assertTrue(errorMessage.isDisplayed(), "сообщение об ошибке в поле фио не отображается");
        assertEquals(
                "Поле обязательно для заполнения",
                errorMessage.getText().

                        trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }

    @Test
    public void shouldErrorFormWithInvalidDataPhoneMore() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+796523456153");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));

        assertTrue(errorMessage.isDisplayed(), "сообщение об ошибке в поле телефон не отображается");
        assertEquals(
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                errorMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }

    @Test
    public void shouldErrorFormWithInvalidDataPhoneLess() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7965234561");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));

        assertTrue(errorMessage.isDisplayed(), "сообщение об ошибке в поле телефон не отображается");
        assertEquals(
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                errorMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }

    @Test
    public void shouldErrorFormWithInvalidDataPhoneWithoutPlus() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("79652345613");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));

        assertTrue(errorMessage.isDisplayed(), "сообщение об ошибке в поле телефон не отображается");
        assertEquals(
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                errorMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }

    @Test
    public void shouldErrorFormWithoutDataPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));

        assertTrue(errorMessage.isDisplayed(), "сообщение об ошибке в поле телефон не отображается");
        assertEquals(
                "Поле обязательно для заполнения",
                errorMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }

    @Test
    public void shouldErrorWithoutCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79652345613");
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement checkBox = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));

        assertTrue(checkBox.isDisplayed());
        assertTrue(driver.findElements(By.cssSelector("[data-test-id=order-success]")).isEmpty(),
                "Заявка не должна отправляться с неверным номером телефона");
    }

    @Test
    public void shouldErrorWithEmptyFields() {
        driver.findElement(By.cssSelector("button.button")).click();

        WebElement nameError = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));

        assertEquals(
                "Поле обязательно для заполнения",
                nameError.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }
}
