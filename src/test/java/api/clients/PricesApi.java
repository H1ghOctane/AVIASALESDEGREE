package api.clients;

import api.models.prices.PricesResponse;
import io.qameta.allure.Step;

import static api.specs.ApiSpecs.successResponseSpec;
import static api.specs.ApiSpecs.travelpayoutsAuthorizedSpec;
import static io.restassured.RestAssured.given;

public class PricesApi {

    private static final String PRICES_FOR_DATES = "/aviasales/v3/prices_for_dates";

    @Step("Найти билеты в одну сторону {origin} → {destination}")
    public PricesResponse getOneWayPrices(String origin, String destination) {
        return given().spec(travelpayoutsAuthorizedSpec())
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("one_way", true)
                .when()
                .get(PRICES_FOR_DATES)
                .then()
                .spec(successResponseSpec)
                .extract().as(PricesResponse.class);
    }

    @Step("Найти билеты из {origin}, не больше {limit}")
    public PricesResponse getPricesFrom(String origin, int limit) {
        return given().spec(travelpayoutsAuthorizedSpec())
                .queryParam("origin", origin)
                .queryParam("limit", limit)
                .when()
                .get(PRICES_FOR_DATES)
                .then()
                .spec(successResponseSpec)
                .extract().as(PricesResponse.class);
    }
}
