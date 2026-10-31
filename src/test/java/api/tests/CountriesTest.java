package api.tests;



import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import api.models.Country;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static api.specs.ApiSpec.requestSpec;
import static api.specs.ApiSpec.responseSpec200;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Страны")
@Owner("твой_ник")
@Tag("api")
public class CountriesTest {

    @Test
    @DisplayName("GET /data/ru/countries.json — возвращает непустой список стран")
    @Severity(SeverityLevel.BLOCKER)
    void countriesListIsNotEmpty() {
        List<Country> countries = given()
                .spec(requestSpec)
                .when()
                .get("/data/ru/countries.json")
                .then()
                .spec(responseSpec200)
                .extract()
                .jsonPath()
                .getList(".", Country.class);

        assertThat(countries)
                .as("Список стран не должен быть пустым")
                .isNotEmpty();
    }
}