package api.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.specs.ApiSpecs.responseSpec;
import static api.specs.ApiSpecs.travelpayoutsRequestSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Контракт ответа")
@Owner("H1ghOctane")
@Tag("api")
public class CountrySchemaTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /data/ru/countries.json — ответ соответствует JSON-схеме")
    void countriesResponseMatchesSchemaTest() {
        given()
                .spec(travelpayoutsRequestSpec)
                .when()
                .get("/data/ru/countries.json")
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("schemas/countries-schema.json"));
    }
}
