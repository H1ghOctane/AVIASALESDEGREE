package api.tests;

import api.clients.PricesApi;
import api.config.ApiConfigProvider;
import api.models.prices.PricesResponse;
import api.models.prices.TicketPrice;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Epic("Aviasales API")
@Feature("Цены на авиабилеты")
@Story("Самые дешёвые билеты по направлению")
@Owner("H1ghOctane")
@Tag("api")
public class TicketPricesTest {

    private final PricesApi pricesApi = new PricesApi();

    @BeforeAll
    static void checkToken() {
        String token = ApiConfigProvider.CONFIG.travelpayoutsToken();
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
        PricesResponse response = pricesApi.getOneWayPrices(origin, destination);

        step("Проверить, что запрос выполнен успешно", () ->
                assertThat(response.isSuccess()).as("success").isTrue()
        );
        step("Проверить, что все билеты из " + origin + " в " + destination + " и с ценой больше нуля", () ->
                assertThat(response.getData())
                        .as("Найденные билеты")
                        .isNotEmpty()
                        .allSatisfy(ticket -> {
                            assertThat(ticket.getOrigin()).isEqualTo(origin);
                            assertThat(ticket.getDestination()).isEqualTo(destination);
                            assertThat(ticket.getPrice()).isPositive();
                        })
        );
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /aviasales/v3/prices_for_dates — параметр limit ограничивает количество билетов")
    void limitParameterTest() {
        int limit = 3;

        PricesResponse response = pricesApi.getPricesFrom("MOW", limit);

        step("Проверить, что запрос выполнен успешно", () ->
                assertThat(response.isSuccess()).as("success").isTrue()
        );
        step("Проверить, что билетов не больше " + limit + " и все они из MOW", () ->
                assertThat(response.getData())
                        .hasSizeBetween(1, limit)
                        .extracting(TicketPrice::getOrigin)
                        .containsOnly("MOW")
        );
    }
}
