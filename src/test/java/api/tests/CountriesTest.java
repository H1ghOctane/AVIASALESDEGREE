package api.tests;

import api.models.Country;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static api.specs.ApiSpecs.responseSpec;
import static api.specs.ApiSpecs.travelpayoutsRequestSpec;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Страны")
@Owner("H1ghOctane")
@Tag("api")
public class CountriesTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("GET /data/ru/countries.json — возвращает непустой список стран")
    void countriesListIsNotEmptyTest() {
        List<Country> countries = given()
                .spec(travelpayoutsRequestSpec)
                .when()
                .get("/data/ru/countries.json")
                .then()
                .spec(responseSpec(200))
                .extract()
                .jsonPath()
                .getList(".", Country.class);

        assertThat(countries).as("Список стран").isNotEmpty();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /data/ru/countries.json — у каждой страны двухбуквенный код")
    void countryCodesHaveTwoLettersTest() {
        List<Country> countries = given()
                .spec(travelpayoutsRequestSpec)
                .when()
                .get("/data/ru/countries.json")
                .then()
                .spec(responseSpec(200))
                .extract()
                .jsonPath()
                .getList(".", Country.class);

        assertThat(countries)
                .extracting(Country::getCode)
                .allSatisfy(code -> assertThat(code).hasSize(2));
    }
}
