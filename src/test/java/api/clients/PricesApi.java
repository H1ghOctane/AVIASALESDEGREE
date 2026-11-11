package api.clients;

import api.models.prices.PricesResponse;
import io.qameta.allure.Step;

import java.util.Map;

import static api.specs.ApiSpecs.*;
import static io.restassured.RestAssured.given;

public class PricesApi {

    private static final String PRICES_FOR_DATES = "/aviasales/v3/prices_for_dates";
    private static final String INVALID_TOKEN = "invalid-token";

    @Step("Найти билеты в одну сторону {origin} → {destination}")
    public PricesResponse getOneWayPrices(String origin, String destination) {
        return getPrices(Map.of(
                "origin", origin,
                "destination", destination,
                "one_way", true
        ));
    }

    @Step("Найти только прямые билеты {origin} → {destination}")
    public PricesResponse getDirectPrices(String origin, String destination) {
        return getPrices(Map.of(
                "origin", origin,
                "destination", destination,
                "direct", true
        ));
    }

    @Step("Найти билеты из {origin}, не больше {limit}")
    public PricesResponse getPricesFrom(String origin, int limit) {
        return getPrices(Map.of(
                "origin", origin,
                "limit", limit
        ));
    }

    @Step("Запросить билеты {origin} → {destination} с неверным токеном")
    public int getPricesStatusWithInvalidToken(String origin, String destination) {
        return given().spec(travelpayoutsRequestSpec)
                .header("X-Access-Token", INVALID_TOKEN)
                .queryParams(Map.of(
                        "origin", origin,
                        "destination", destination
                ))
                .when()
                .get(PRICES_FOR_DATES)
                .then()
                .extract().statusCode();
    }

    private PricesResponse getPrices(Map<String, ?> queryParams) {
        return given().spec(travelpayoutsAuthorizedSpec())
                .queryParams(queryParams)
                .when()
                .get(PRICES_FOR_DATES)
                .then()
                .spec(successResponseSpec)
                .extract().as(PricesResponse.class);
    }
}
