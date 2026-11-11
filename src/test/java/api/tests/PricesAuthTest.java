package api.tests;

import api.clients.PricesApi;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Aviasales API")
@Feature("Цены на авиабилеты")
@Story("Авторизация")
@Owner("H1ghOctane")
@Tag("api")
public class PricesAuthTest {

    private final PricesApi pricesApi = new PricesApi();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /aviasales/v3/prices_for_dates — с неверным токеном сервер отвечает 401")
    void invalidTokenIsRejectedTest() {
        int statusCode = pricesApi.getPricesStatusWithInvalidToken("MOW", "LED");

        step("Проверить, что сервер ответил 401 Unauthorized", () ->
                assertThat(statusCode).as("HTTP-статус").isEqualTo(401)
        );
    }
}
