package Helpers;

import io.qameta.allure.Step;
import lombok.AllArgsConstructor;

import java.sql.*;

import static Helpers.Product.faker;

public class JDBC {
    private static final String count = "SELECT COUNT(FOOD_NAME) AS NAME FROM FOOD WHERE FOOD_NAME = ?";
    private static final String foodInfo = "SELECT * FROM FOOD WHERE FOOD_NAME = ?";
    private static final String deleteString = "DELETE FROM FOOD WHERE FOOD_NAME = ?";
    private static final String insertProd = "INSERT INTO FOOD (FOOD_NAME ,FOOD_TYPE ,FOOD_EXOTIC ) SELECT ?, ?, ?;";
    private static Connection connection;
    private static final String[] foodTypes = {"VEGETABLE", "FRUIT"};

    @Step("Проверить отсутствие добаляемого продукта в БД")
    public static int countFoodName(String foodName) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(count);
        statement.setString(1, foodName);
        ResultSet resultExp = statement.executeQuery();
        resultExp.next();
        int result = resultExp.getInt("NAME");
        connection.close();
        return result;
    }

    @Step("Добавить новый продукт в БД")
    public static int insertProductIntoDB(ProductDB prod) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(insertProd);
        statement.setString(1, prod.name);
        statement.setString(2, prod.type);
        statement.setInt(3, prod.exotic);
        int count = statement.executeUpdate();
        connection.close();
        return count;
    }

    @Step("Получить информацию о добавленном продукте")
    public static ProductDB getProductInfoFromDB(String name) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(foodInfo);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        boolean exotic = false;
        if (resultSet.getInt("FOOD_EXOTIC") == 1) exotic = true;
        ProductDB productInfo = new ProductDB(
                resultSet.getString("FOOD_NAME"),
                resultSet.getString("FOOD_TYPE"),
                resultSet.getInt("FOOD_EXOTIC"));
        connection.close();
        return productInfo;
    }

    @Step("Удалить продукт")
    public static int resetDB(String name) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:testdb",
                "user", "pass");
        PreparedStatement statement = connection.prepareStatement(deleteString);
        statement.setString(1, name);
        int count = statement.executeUpdate();
        connection.close();
        return count;
    }

    @AllArgsConstructor
    public static class ProductDB {
        public String name;
        public String type;
        public int exotic;
    }

    public static ProductDB getVegetableDB(int exotic) {
        return new ProductDB(faker.food().vegetable(), foodTypes[faker.random().nextInt(2)],
                exotic);
    }
}
