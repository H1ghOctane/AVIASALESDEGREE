package api.clients;

import api.data.AuthData;
import api.models.booker.AuthRequest;
import api.models.booker.AuthResponse;
import api.models.booker.Booking;
import api.models.booker.BookingPatchRequest;
import api.models.booker.BookingResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static api.specs.ApiSpecs.bookerRequestSpec;
import static api.specs.ApiSpecs.successResponseSpec;
import static io.restassured.RestAssured.given;

public class BookerApi {

    private static final String AUTH = "/auth";
    private static final String BOOKING = "/booking";
    private static final String BOOKING_BY_ID = "/booking/{id}";

    @Step("Получить токен авторизации")
    public AuthResponse createToken(AuthRequest authRequest) {
        return given().spec(bookerRequestSpec)
                .body(authRequest)
                .when()
                .post(AUTH)
                .then()
                .spec(successResponseSpec)
                .extract().as(AuthResponse.class);
    }

    @Step("Получить токен администратора")
    public String adminToken() {
        return createToken(AuthData.adminCredentials()).getToken();
    }

    @Step("Создать бронирование")
    public BookingResponse createBooking(Booking booking) {
        return given().spec(bookerRequestSpec)
                .body(booking)
                .when()
                .post(BOOKING)
                .then()
                .spec(successResponseSpec)
                .extract().as(BookingResponse.class);
    }

    @Step("Получить бронирование {bookingId}")
    public Booking getBooking(int bookingId) {
        return sendGet(bookingId)
                .then()
                .spec(successResponseSpec)
                .extract().as(Booking.class);
    }

    @Step("Запросить бронирование {bookingId} без проверки ответа")
    public Response getBookingRaw(int bookingId) {
        return sendGet(bookingId);
    }

    @Step("Полностью обновить бронирование {bookingId}")
    public Booking updateBooking(int bookingId, Booking booking, String token) {
        return withToken(token)
                .body(booking)
                .when()
                .put(BOOKING_BY_ID, bookingId)
                .then()
                .spec(successResponseSpec)
                .extract().as(Booking.class);
    }

    @Step("Частично обновить бронирование {bookingId}")
    public Booking partialUpdateBooking(int bookingId, BookingPatchRequest patch, String token) {
        return withToken(token)
                .body(patch)
                .when()
                .patch(BOOKING_BY_ID, bookingId)
                .then()
                .spec(successResponseSpec)
                .extract().as(Booking.class);
    }

    @Step("Удалить бронирование {bookingId}")
    public void deleteBooking(int bookingId, String token) {
        sendDelete(bookingId, token)
                .then()
                .statusCode(201);
    }

    @Step("Отправить удаление бронирования {bookingId} без проверки ответа")
    public Response deleteBookingRaw(int bookingId, String token) {
        return sendDelete(bookingId, token);
    }

    private Response sendGet(int bookingId) {
        return given().spec(bookerRequestSpec)
                .when()
                .get(BOOKING_BY_ID, bookingId);
    }

    private Response sendDelete(int bookingId, String token) {
        return withToken(token)
                .when()
                .delete(BOOKING_BY_ID, bookingId);
    }

    private RequestSpecification withToken(String token) {
        RequestSpecification request = given().spec(bookerRequestSpec);
        if (token != null) {
            request.cookie("token", token);
        }
        return request;
    }
}
