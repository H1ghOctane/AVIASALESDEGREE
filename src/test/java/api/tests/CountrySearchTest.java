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
import static org.assertj.core.api.Assertions.tuple;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Поиск страны в списке")
@Owner("H1ghOctane")
@Tag("api")
public class CountrySearchTest {

    private final GeoApi geoApi = new GeoApi();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /data/ru/countries.json — в списке есть Россия, Германия и Франция с верными кодами")
    void listContainsCountriesTest() {
        List<Country> countries = geoApi.getCountries();

        step("Проверить, что в списке есть RU — Россия, DE — Германия, FR — Франция", () ->
                assertThat(countries)
                        .extracting(Country::getCode, Country::getName)
                        .contains(
                                tuple("RU", "Россия"),
                                tuple("DE", "Германия"),
                                tuple("FR", "Франция")
                        )
        );
    }
}
