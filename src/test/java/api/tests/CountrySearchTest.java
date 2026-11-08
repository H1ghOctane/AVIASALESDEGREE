package api.tests;

import api.clients.GeoApi;
import api.models.Country;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Поиск страны в списке")
@Owner("H1ghOctane")
@Tag("api")
public class CountrySearchTest {

    private final GeoApi geoApi = new GeoApi();

    @ParameterizedTest(name = "Список содержит страну {0} с кодом {1}")
    @CsvSource({
            "Россия, RU",
            "Германия, DE",
            "Франция, FR"
    })
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /data/ru/countries.json — список содержит страну")
    void listContainsCountryTest(String name, String code) {
        assertThat(geoApi.getCountries())
                .filteredOn(Country::getCode, code)
                .extracting(Country::getName)
                .containsExactly(name);
    }
}
