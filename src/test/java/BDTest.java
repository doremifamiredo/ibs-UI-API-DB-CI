import Helpers.JDBC;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;

import static Helpers.JDBC.getVegetableDB;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Добавление товаров")
@Feature("Тестирование БД")
public class BDTest {

    @Story("Добавление и удаление овощей в БД запросами SQL")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("Adding a vegetable that is not in the database WITH SQL")
    void AddingFruitNotInDatabaseSQL(int exotic) throws SQLException {
        JDBC.ProductDB expected = getVegetableDB(exotic);
        assertEquals(0, JDBC.countFoodName(expected.name), "Продукт с таким названием уже есть в БД");
        assertEquals(1, JDBC.insertProductIntoDB(expected), "Продукт не добавлен в БД");
        JDBC.ProductDB actual = JDBC.getProductInfoFromDB(expected.name);
        assertAll(() -> assertEquals(expected.name, actual.name,
                        "Название добавленного продукта и не совпадает с БД"),
                () -> assertEquals(expected.type, actual.type,
                        "Тип добавленного продукта и не совпадает с БД"),
                () -> assertEquals(expected.exotic, actual.exotic,
                        "Экзотичность добавленного продукта не совпадает с БД"),
                () -> assertEquals(1, JDBC.resetDB(expected.name),
                        "Запрос на удаление продукта из БД не выполнен"));
    }
}
