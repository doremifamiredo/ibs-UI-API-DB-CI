import Helpers.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static Helpers.APIHelper.*;
import static Helpers.Product.getFruit;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITest {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Adding exotic and non-exotic FRUITS to the list of products")
    void addingFruit(boolean exotic) throws JsonProcessingException {
        Product expected = getFruit(exotic);
        addingProduct(expected);
        Product[] allProducts = getProductList();
        Product actual = allProducts[allProducts.length - 1];
        resettingData();
        assertAll(() -> assertEquals(5, allProducts.length),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getType(), actual.getType()),
                () -> assertEquals(exotic, actual.isExotic()));
    }

    // Для разнообразия показан другой источник данных параметризированного теста
    @ParameterizedTest
    @MethodSource("Helpers.Product#dataStream")
    @DisplayName("Adding exotic and non-exotic VEGETABLES to the list of products")
    void addingVegetable(Product expected) throws JsonProcessingException {
        addingProduct(expected);
        Product[] allProducts = getProductList();
        Product actual = allProducts[allProducts.length - 1];
        resettingData();
        assertAll(() -> assertEquals(5, allProducts.length),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getType(), actual.getType()),
                () -> assertEquals(expected.isExotic(), actual.isExotic()));
    }
}
