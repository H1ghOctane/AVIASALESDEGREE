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

    @Step("Получить валюту страны с кодом {code}")
    public String getCountryCurrency(String code) {
        return requestCountries()
                .extract().path("find { it.code == '%s' }.currency".formatted(code));
    }

    @Step("Попытаться добавить страну в справочник (POST)")
    public int addCountry(Country country) {
        return given().spec(travelpayoutsRequestSpec)
                .body(country)
                .when()
                .post(COUNTRIES)
                .then()
                .extract().statusCode();
    }

    @Step("Попытаться удалить справочник стран (DELETE)")
    public int deleteCountries() {
        return given().spec(travelpayoutsRequestSpec)
                .when()
                .delete(COUNTRIES)
                .then()
                .extract().statusCode();
    }

    private ValidatableResponse requestCountries() {
        return given().spec(travelpayoutsRequestSpec)
                .when()
                .get(COUNTRIES)
                .then()
                .spec(successResponseSpec);
    }
}
