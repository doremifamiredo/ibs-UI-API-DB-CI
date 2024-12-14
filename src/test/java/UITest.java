import Helpers.ProductList;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Добавление товаров")
@Feature("Тестирование UI")
public class UITest {

    @Story("Добавление фркута и овоща через UI")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Adding a fruit and a vegetable that is not in the database to the list of products")
    void AddingFruitNotInDatabase(boolean isFruit) {
        ProductList.ProductInfo expected = new ProductList.ProductInfo(isFruit);
        assertFalse(productList.searchWordInTable(expected.name),
                "Продукт с таким названием уже есть в списке продуктов");
        productList.addProduct(expected);
        driver.navigate().refresh();
        ProductList.ProductInfo actual = new ProductList.ProductInfo(productList.getLastProduct());
        productList.dataReset();
        assertAll(() -> assertEquals(expected.name, actual.name,
                        "Название добавленного продукта и последнего в списке не совпадают"),
                () -> assertEquals(expected.isFruit, actual.isFruit,
                        "Тип добавленного продукта и последнего в списке не совпадают"),
                () -> assertEquals(expected.isExotic, actual.isExotic,
                        "Экзотичность добавленного продукта и последнего в списке не совпадают"));
    }

    ProductList productList = new ProductList();
    public static WebDriver driver;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void openSite() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        productList.init(driver);
        driver.get("https://qualit.applineselenoid.fvds.ru/");
        driver.manage().window().maximize();
        productList.openProductList();
    }

    @AfterEach
    void closing() {
        driver.close();
    }

    @AfterAll
    static void teardown() {
        driver.quit();
    }
}
