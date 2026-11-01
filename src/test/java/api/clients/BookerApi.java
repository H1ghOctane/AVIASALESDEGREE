package api.clients;

import api.config.ApiConfig;
import api.models.booker.AuthRequest;
import api.models.booker.Booking;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

import java.util.Map;

import static api.specs.ApiSpecs.bookerRequestSpec;
import static io.restassured.RestAssured.given;

public class BookerApi {

    private static final ApiConfig config = ConfigFactory.create(ApiConfig.class, System.getProperties());

    @Step("POST /auth — получить токен")
    public Response createToken(AuthRequest authRequest) {
        return given()
                .spec(bookerRequestSpec)
                .body(authRequest)
                .when()
                .post("/auth");
    }

    @Step("Получить токен администратора")
    public String adminToken() {
        AuthRequest admin = AuthRequest.builder()
                .username(config.bookerUsername())
                .password(config.bookerPassword())
                .build();
        return createToken(admin).path("token");
    }

    @Step("POST /booking — создать бронирование")
    public Response createBooking(Booking booking) {
        return given()
                .spec(bookerRequestSpec)
                .body(booking)
                .when()
                .post("/booking");
    }

    @Step("GET /booking/{bookingId} — получить бронирование")
    public Response getBooking(int bookingId) {
        return given()
                .spec(bookerRequestSpec)
                .when()
                .get("/booking/{id}", bookingId);
    }

    @Step("PUT /booking/{bookingId} — полностью обновить бронирование")
    public Response updateBooking(int bookingId, Booking booking, String token) {
        return withToken(token)
                .body(booking)
                .when()
                .put("/booking/{id}", bookingId);
    }

    @Step("PATCH /booking/{bookingId} — частично обновить бронирование")
    public Response partialUpdateBooking(int bookingId, Map<String, Object> fields, String token) {
        return withToken(token)
                .body(fields)
                .when()
                .patch("/booking/{id}", bookingId);
    }

    @Step("DELETE /booking/{bookingId} — удалить бронирование")
    public Response deleteBooking(int bookingId, String token) {
        return withToken(token)
                .when()
                .delete("/booking/{id}", bookingId);
    }

    private RequestSpecification withToken(String token) {
        RequestSpecification request = given().spec(bookerRequestSpec);
        if (token != null) {
            request.cookie("token", token);
        }
        return request;
    }
}
