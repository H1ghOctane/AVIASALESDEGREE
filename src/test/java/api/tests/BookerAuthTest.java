package api.tests;

import api.clients.BookerApi;
import api.models.booker.AuthRequest;
import api.models.booker.AuthResponse;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.specs.ApiSpecs.responseSpec;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@Epic("Restful-booker API")
@Feature("Авторизация")
@Owner("H1ghOctane")
@Tag("api")
public class BookerAuthTest {

    private final BookerApi api = new BookerApi();

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /auth — верные данные возвращают токен")
    void validCredentialsReturnTokenTest() {
        AuthRequest request = AuthRequest.builder()
                .username("admin")
                .password("password123")
                .build();

        AuthResponse response = api.createToken(request)
                .then()
                .spec(responseSpec(200))
                .extract().as(AuthResponse.class);

        assertThat(response.getToken()).isNotBlank();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /auth — неверный пароль не возвращает токен")
    void invalidCredentialsTest() {
        AuthRequest request = AuthRequest.builder()
                .username("admin")
                .password("wrong-password")
                .build();

        api.createToken(request)
                .then()
                .spec(responseSpec(200))
                .body("reason", equalTo("Bad credentials"))
                .body("token", nullValue());
    }
}
