package api.tests;

import api.clients.GeoApi;
import api.models.Country;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Справочник доступен только на чтение")
@Owner("H1ghOctane")
@Tag("api")
public class CountriesReadOnlyTest {

    private final GeoApi geoApi = new GeoApi();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("POST /data/ru/countries.json — добавить страну в справочник нельзя, сервер отвечает 403")
    void addCountryIsForbiddenTest() {
        Country country = new Country();
        country.setCode("XX");
        country.setName("Тестландия");
        country.setCurrency("XXX");

        int statusCode = geoApi.addCountry(country);

        step("Проверить, что сервер ответил 403 Forbidden", () ->
                assertThat(statusCode).as("HTTP-статус").isEqualTo(403)
        );
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /data/ru/countries.json — удалить справочник нельзя, сервер отвечает 403")
    void deleteCountriesIsForbiddenTest() {
        int statusCode = geoApi.deleteCountries();

        step("Проверить, что сервер ответил 403 Forbidden", () ->
                assertThat(statusCode).as("HTTP-статус").isEqualTo(403)
        );
    }
}
