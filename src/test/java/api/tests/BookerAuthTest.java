package api.tests;

import api.clients.BookerApi;
import api.models.booker.AuthResponse;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static api.data.AuthData.adminCredentials;
import static api.data.AuthData.wrongPassword;
import static org.assertj.core.api.Assertions.assertThat;

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
        AuthResponse response = api.createToken(adminCredentials());

        assertThat(response.getToken()).isNotBlank();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("POST /auth — неверный пароль не возвращает токен")
    void invalidCredentialsTest() {
        AuthResponse response = api.createToken(wrongPassword());

        assertThat(response.getToken()).isNull();
        assertThat(response.getReason()).isEqualTo("Bad credentials");
    }
}
