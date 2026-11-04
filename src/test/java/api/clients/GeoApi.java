package api.clients;

import api.models.Country;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.List;

import static api.specs.ApiSpecs.successResponseSpec;
import static api.specs.ApiSpecs.travelpayoutsRequestSpec;
import static io.restassured.RestAssured.given;

public class GeoApi {

    private static final String COUNTRIES = "/data/ru/countries.json";

    @Step("Получить список стран")
    public List<Country> getCountries() {
        return requestCountries()
                .extract().jsonPath().getList(".", Country.class);
    }

    @Step("Получить список стран как JSON")
    public String getCountriesJson() {
        return requestCountries()
                .extract().asString();
    }

    private ValidatableResponse requestCountries() {
        return given().spec(travelpayoutsRequestSpec)
                .when()
                .get(COUNTRIES)
                .then()
                .spec(successResponseSpec);
    }
}
