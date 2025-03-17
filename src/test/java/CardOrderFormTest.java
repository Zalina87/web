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
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));
        nameInput.sendKeys("Иванов Иван");
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
        phoneInput.sendKeys("+79652345615");
        WebElement checkBox = driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box"));
        checkBox.click();
        WebElement button = driver.findElement(By.cssSelector("button.button"));
        button.click();

        WebElement successMessage = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
        assertTrue(successMessage.isDisplayed(), "сообщение об успешной отправке не отображается");
        assertEquals(
                "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                successMessage.getText().trim(),
                "текст успешного сообщения не соответствует ожидаемому"
        );
    }
    @Test
    public void shouldErrorFormWithInvalidDataName() {

        WebElement successMessage = driver.findElement(By.cssSelector(".input__sub"));
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));

        nameInput.sendKeys("Кол");
        WebElement button = driver.findElement(By.cssSelector("button.button"));
        button.click();
        assertTrue(successMessage.isDisplayed(), "сообщение об ошибке в поле фио не отображается");
        assertEquals(
                "Укажите точно как в паспорте",
                successMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );

        nameInput.sendKeys("89");
        button.click();
        assertTrue(successMessage.isDisplayed(), "сообщение об ошибке в поле фио не отображается");
        assertEquals(
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                successMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );

        nameInput.sendKeys("Ivanov Ivan");
        button.click();
        assertTrue(successMessage.isDisplayed(), "сообщение об ошибке в поле фио не отображается");
        assertEquals(
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                successMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }
    @Test
    public void shouldErrorFormWithInvalidDataPhone() {

        WebElement successMessage = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub"));
        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));

        nameInput.sendKeys("Иванов Иван");
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
        phoneInput.sendKeys("+796523456153");
        WebElement button = driver.findElement(By.cssSelector("button.button"));
        button.click();
        assertTrue(successMessage.isDisplayed(), "сообщение об ошибке в поле телефон не отображается");
        assertEquals(
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                successMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );

        phoneInput.sendKeys("+7965234561");
        button.click();
        assertTrue(successMessage.isDisplayed(), "сообщение об ошибке в поле телефон не отображается");
        assertEquals(
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                successMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );

        phoneInput.sendKeys("65262139524");
        button.click();
        assertTrue(successMessage.isDisplayed(), "сообщение об ошибке в поле телефон не отображается");
        assertEquals(
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                successMessage.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }
    @Test
    public void shouldErrorWithoutCheckBox() {

        WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));

        nameInput.sendKeys("Иванов Иван");
        WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
        phoneInput.sendKeys("+79652345615");
        WebElement button = driver.findElement(By.cssSelector("button.button"));
        button.click();
        WebElement checkBox = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));
        assertTrue(checkBox.isDisplayed());
        assertTrue(driver.findElements(By.cssSelector("[data-test-id=order-success]")).isEmpty(),
                "Заявка не должна отправляться с неверным номером телефона");
    }

    @Test
    public void shouldErrorWithEmptyFields() {

        WebElement button = driver.findElement(By.cssSelector("button.button"));
        button.click();
        WebElement nameError = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub"));
        assertEquals(
                "Поле обязательно для заполнения",
                nameError.getText().trim(),
                "текст сообщения об ошибке не соответствует ожидаемому"
        );
    }
}
