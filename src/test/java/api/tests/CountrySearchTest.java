package api.tests;

import api.models.Country;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static api.specs.ApiSpec.requestSpec;
import static api.specs.ApiSpec.responseSpec200;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Aviasales API")
@Feature("Поиск стран")
@Tag("api")
public class CountrySearchTest {

    @Test
    @Story("Поиск по названию")
    @DisplayName("GET /data/ru/countries.json — список содержит Россию")
    @Severity(SeverityLevel.NORMAL)
    void searchCountriesByName() {
        // Искомая страна
        String expectedCountry = "Россия";

        // Запрашиваем полный список (API не поддерживает поиск через query param)
        List<Country> countries = given()
                .spec(requestSpec)
                .when()
                .get("/data/ru/countries.json")
                .then()
                .spec(responseSpec200)
                .extract()
                .jsonPath()
                .getList(".", Country.class);

        // Извлекаем только названия стран
        List<String> countryNames = countries.stream()
                .map(Country::getName)
                .collect(Collectors.toList());

        // Проверяем, что в списке есть "Россия"
        assertThat(countryNames)
                .as("Результаты поиска должны содержать искомую страну")
                .anyMatch(name -> name.contains(expectedCountry));
    }
}