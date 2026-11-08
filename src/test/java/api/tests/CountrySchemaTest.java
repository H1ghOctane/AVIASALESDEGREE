package api.tests;

import api.clients.GeoApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.HamcrestCondition.matching;

@Epic("Aviasales API")
@Feature("Справочники")
@Story("Контракт ответа")
@Owner("H1ghOctane")
@Tag("api")
public class CountrySchemaTest {

    private final GeoApi geoApi = new GeoApi();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /data/ru/countries.json — ответ соответствует JSON-схеме")
    void countriesResponseMatchesSchemaTest() {
        String json = geoApi.getCountriesJson();

        assertThat(json).is(matching(matchesJsonSchemaInClasspath("schemas/countries-schema.json")));
    }
}
