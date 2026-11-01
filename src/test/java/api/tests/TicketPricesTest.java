package api.tests;

import api.config.ApiConfig;
import api.models.prices.PricesResponse;
import api.models.prices.TicketPrice;
import io.qameta.allure.*;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static api.specs.ApiSpecs.responseSpec;
import static api.specs.ApiSpecs.travelpayoutsAuthorizedSpec;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Epic("Aviasales API")
@Feature("Цены на авиабилеты")
@Story("Самые дешёвые билеты по направлению")
@Owner("H1ghOctane")
@Tag("api")
public class TicketPricesTest {

    private static final String PRICES_ENDPOINT = "/aviasales/v3/prices_for_dates";

    @BeforeAll
    static void checkToken() {
        String token = ConfigFactory.create(ApiConfig.class, System.getProperties()).travelpayoutsToken();
        assumeTrue(token != null && !token.isBlank(),
                "Токен Travelpayouts не задан (travelpayouts.token), тесты цен пропущены");
    }

    @ParameterizedTest(name = "Билеты {0} → {1} относятся к запрошенному направлению")
    @CsvSource({
            "MOW, LED",
            "MOW, AER"
    })
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /aviasales/v3/prices_for_dates — билеты по направлению")
    void pricesMatchRequestedRouteTest(String origin, String destination) {
        PricesResponse response = given()
                .spec(travelpayoutsAuthorizedSpec())
                .queryParam("origin", origin)
                .queryParam("destination", destination)
                .queryParam("one_way", true)
                .when()
                .get(PRICES_ENDPOINT)
                .then()
                .spec(responseSpec(200))
                .body("success", equalTo(true))
                .extract().as(PricesResponse.class);

        assertThat(response.getData()).as("Найденные билеты").isNotEmpty();
        assertThat(response.getData()).allSatisfy(ticket -> {
            assertThat(ticket.getOrigin()).isEqualTo(origin);
            assertThat(ticket.getDestination()).isEqualTo(destination);
            assertThat(ticket.getPrice()).isPositive();
        });
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /aviasales/v3/prices_for_dates — параметр limit ограничивает количество билетов")
    void limitParameterTest() {
        int limit = 3;

        PricesResponse response = given()
                .spec(travelpayoutsAuthorizedSpec())
                .queryParam("origin", "MOW")
                .queryParam("limit", limit)
                .when()
                .get(PRICES_ENDPOINT)
                .then()
                .spec(responseSpec(200))
                .body("success", equalTo(true))
                .extract().as(PricesResponse.class);

        assertThat(response.getData())
                .hasSizeBetween(1, limit)
                .extracting(TicketPrice::getOrigin)
                .containsOnly("MOW");
    }
}
