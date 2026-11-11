package api.clients;

import api.models.prices.PricesResponse;
import io.qameta.allure.Step;

import java.util.Map;

import static api.specs.ApiSpecs.successResponseSpec;
import static api.specs.ApiSpecs.travelpayoutsAuthorizedSpec;
import static io.restassured.RestAssured.given;

public class PricesApi {

    private static final String PRICES_FOR_DATES = "/aviasales/v3/prices_for_dates";

    @Step("Найти билеты в одну сторону {origin} → {destination}")
    public PricesResponse getOneWayPrices(String origin, String destination) {
        return getPrices(Map.of(
                "origin", origin,
                "destination", destination,
                "one_way", true
        ));
    }

    @Step("Найти билеты из {origin}, не больше {limit}")
    public PricesResponse getPricesFrom(String origin, int limit) {
        return getPrices(Map.of(
                "origin", origin,
                "limit", limit
        ));
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
