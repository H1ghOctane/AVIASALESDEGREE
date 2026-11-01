package api.tests;

import api.models.Country;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static api.specs.ApiSpecs.responseSpec;
import static api.specs.ApiSpecs.travelpayoutsRequestSpec;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Поиск страны в списке")
@Owner("H1ghOctane")
@Tag("api")
public class CountrySearchTest {

    @ParameterizedTest(name = "Список содержит страну {0} с кодом {1}")
    @CsvSource({
            "Россия, RU",
            "Германия, DE",
            "Франция, FR"
    })
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /data/ru/countries.json — список содержит страну")
    void listContainsCountryTest(String name, String code) {
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
                .filteredOn(country -> code.equals(country.getCode()))
                .extracting(Country::getName)
                .containsExactly(name);
    }
}
