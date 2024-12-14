package Helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    public String name;
    public ProdType type;
    public boolean exotic;

    private enum ProdType {
        FRUIT,
        VEGETABLE
    }

    public static final Faker faker = new Faker();

    public static Product getFruit(boolean exotic) {
        return new Product(faker.food().fruit(), ProdType.FRUIT, exotic);
    }

    public static Stream<Product> dataStream() {
        return Stream.of(
                getVegetable(true),
                getVegetable(false)
        );
    }
    public static Product getVegetable(boolean exotic) {
           return new Product(faker.food().vegetable(), ProdType.VEGETABLE, exotic);
    }
}
