package Helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class APIHelper {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://149.154.71.152:8080")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("products.json"))
            .log(LogDetail.BODY)
            .build();
    private static Cookie JSESSIONID;
    private static final ObjectMapper mapper = new ObjectMapper();

    private APIHelper() {
    }

    @Step("Добавить продукт с помощью REST-запроса")
    public static void addingProduct(Product product) {
        JSESSIONID = given()
                .spec(requestSpec)
                .body(product)
                .when().post("/api/food")
                .then()
                .statusCode(200)
                .extract().detailedCookie("JSESSIONID");
    }

    @Step("Получить список продуктов")
    public static Product[] getProductList() throws JsonProcessingException {
        return mapper.readValue(
                given()
                .spec(requestSpec)
                .cookie(JSESSIONID)
                .when().get("/api/food")
                .then().spec(responseSpec)
                .extract().body().asString(), Product[].class);
    }

    @Step("Сброс тестовых данных")
    public static void resettingData() {
        given()
                .spec(requestSpec)
                .when().post("/api/data/reset")
                .then()
                .statusCode(200);
    }
}
