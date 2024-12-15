import Helpers.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static Helpers.APIHelper.*;
import static Helpers.Product.getFruit;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Добавление товаров")
@Feature("Тестирование API")
public class APITest {

    @Story("Добавление фрукта с помощью API")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Adding exotic and non-exotic FRUITS to the list of products")
    void addingFruit(boolean exotic) throws JsonProcessingException {
        Product expected = getFruit(exotic);
        addingProduct(expected);
        Product[] allProducts = getProductList();
        Product actual = allProducts[allProducts.length - 1];
        resettingData();
        assertAll(() -> assertEquals(5, allProducts.length,
                        "Список продуктов не равен 5"),
                () -> assertEquals(expected.getName(), actual.getName(),
                        "Название добавленного продукта и последнего в списке не совпадают"),
                () -> assertEquals(expected.getType(), actual.getType(),
                        "Тип добавленного продукта и последнего в списке не совпадают"),
                () -> assertEquals(exotic, actual.isExotic(),
                        "Экзотичность добавленного продукта и последнего в списке не совпадают"));
    }


    @Story("Добавление овощей с помощью API")
    @ParameterizedTest
    // Для разнообразия показан другой источник данных параметризированного теста
    @MethodSource("Helpers.Product#dataStream")
    @DisplayName("Adding exotic and non-exotic VEGETABLES to the list of products")
    void addingVegetable(Product expected) throws JsonProcessingException {
        addingProduct(expected);
        Product[] allProducts = getProductList();
        Product actual = allProducts[allProducts.length - 1];
        resettingData();
        assertAll(() -> assertEquals(5, allProducts.length,
                        "Список продуктов не равен 5"),
                () -> assertEquals(expected.name, actual.name,
                        "Название добавленного продукта и последнего в списке не совпадают"),
                () -> assertEquals(expected.type, actual.type,
                        "Тип добавленного продукта и последнего в списке не совпадают"),
                () -> assertEquals(expected.exotic, actual.exotic,
                        "Экзотичность добавленного продукта и последнего в списке не совпадают"));
    }
}
