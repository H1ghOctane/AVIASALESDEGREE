package api.tests;

import api.clients.BookerApi;
import api.models.booker.Booking;
import api.models.booker.BookingPatchRequest;
import api.models.booker.BookingResponse;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static api.data.BookingData.randomBooking;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-booker API")
@Feature("Бронирования")
@Owner("H1ghOctane")
@Tag("api")
public class BookerBookingTest {

    private final BookerApi api = new BookerApi();

    @Test
    @Story("Создание")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /booking — бронирование создаётся")
    void createBookingTest() {
        Booking booking = randomBooking();

        BookingResponse response = api.createBooking(booking);

        assertThat(response.getBookingid()).isPositive();
        assertThat(response.getBooking()).isEqualTo(booking);
    }

    @Test
    @Story("Получение")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /booking/{id} — возвращает созданное бронирование")
    void getBookingTest() {
        Booking booking = randomBooking();
        int bookingId = api.createBooking(booking).getBookingid();

        Booking received = api.getBooking(bookingId);

        assertThat(received).isEqualTo(booking);
    }

    @Test
    @Story("Обновление")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /booking/{id} — бронирование полностью обновляется")
    void updateBookingTest() {
        int bookingId = api.createBooking(randomBooking()).getBookingid();
        Booking updated = randomBooking();

        Booking received = api.updateBooking(bookingId, updated, api.adminToken());

        assertThat(received).isEqualTo(updated);
    }

    @Test
    @Story("Обновление")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("PATCH /booking/{id} — меняется только переданное поле")
    void partialUpdateBookingTest() {
        Booking booking = randomBooking();
        int bookingId = api.createBooking(booking).getBookingid();
        BookingPatchRequest patch = BookingPatchRequest.builder()
                .firstname("Updated")
                .build();

        Booking received = api.partialUpdateBooking(bookingId, patch, api.adminToken());

        assertThat(received.getFirstname()).isEqualTo("Updated");
        assertThat(received)
                .usingRecursiveComparison()
                .ignoringFields("firstname")
                .isEqualTo(booking);
    }

    @Test
    @Story("Удаление")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /booking/{id} — бронирование удаляется")
    void deleteBookingTest() {
        int bookingId = api.createBooking(randomBooking()).getBookingid();

        api.deleteBooking(bookingId, api.adminToken());

        assertThat(api.getBookingRaw(bookingId).statusCode()).isEqualTo(404);
    }

    @Test
    @Story("Удаление")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("DELETE /booking/{id} без токена — 403, бронирование остаётся")
    void deleteBookingWithoutTokenTest() {
        Booking booking = randomBooking();
        int bookingId = api.createBooking(booking).getBookingid();

        Response response = api.deleteBookingRaw(bookingId, null);

        assertThat(response.statusCode()).isEqualTo(403);
        assertThat(api.getBooking(bookingId)).isEqualTo(booking);
    }

    @ParameterizedTest(name = "GET /booking/{0} — бронирования не существует")
    @ValueSource(ints = {999999999, 888888888})
    @Story("Получение")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /booking/{id} несуществующего бронирования — 404")
    void getNonExistingBookingTest(int bookingId) {
        assertThat(api.getBookingRaw(bookingId).statusCode()).isEqualTo(404);
    }
}
