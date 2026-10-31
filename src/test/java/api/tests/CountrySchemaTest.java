package api.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.specs.ApiSpec.requestSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("Aviasales API")
@Feature("Валидация схемы")
@Tag("api")
public class CountrySchemaTest {

    @Test
    @Story("JSON Schema")
    @DisplayName("GET /data/ru/countries.json — ответ соответствует схеме")
    @Severity(SeverityLevel.CRITICAL)
    void validateCountriesSchema() {
        given()
                .spec(requestSpec)
                .when()
                .get("/data/ru/countries.json")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/countries-schema.json"));
    }
}