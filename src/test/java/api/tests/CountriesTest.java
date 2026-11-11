package api.tests;

import api.clients.GeoApi;
import api.models.Country;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Страны")
@Owner("H1ghOctane")
@Tag("api")
public class CountriesTest {

    private final GeoApi geoApi = new GeoApi();

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("GET /data/ru/countries.json — возвращает непустой список стран")
    void countriesListIsNotEmptyTest() {
        List<Country> countries = geoApi.getCountries();

        step("Проверить, что список стран не пустой", () ->
                assertThat(countries).as("Список стран").isNotEmpty()
        );
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /data/ru/countries.json — у каждой страны двухбуквенный код")
    void countryCodesHaveTwoLettersTest() {
        List<Country> countries = geoApi.getCountries();

        step("Проверить, что код каждой страны состоит из двух букв", () ->
                assertThat(countries)
                        .extracting(Country::getCode)
                        .allSatisfy(code -> assertThat(code).hasSize(2))
        );
    }
}
