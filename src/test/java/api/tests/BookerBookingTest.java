package api.tests;

import api.clients.BookerApi;
import api.models.booker.Booking;
import api.models.booker.BookingResponse;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static api.data.BookingData.randomBooking;
import static api.specs.ApiSpecs.responseSpec;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

        BookingResponse response = api.createBooking(booking)
                .then()
                .spec(responseSpec(200))
                .body("booking.firstname", equalTo(booking.getFirstname()))
                .extract().as(BookingResponse.class);

        assertThat(response.getBookingid()).isPositive();
        assertThat(response.getBooking()).isEqualTo(booking);
    }

    @Test
    @Story("Получение")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /booking/{id} — возвращает созданное бронирование")
    void getBookingTest() {
        Booking booking = randomBooking();
        int bookingId = createBooking(booking);

        Booking received = api.getBooking(bookingId)
                .then()
                .spec(responseSpec(200))
                .body("lastname", equalTo(booking.getLastname()))
                .extract().as(Booking.class);

        assertThat(received).isEqualTo(booking);
    }

    @Test
    @Story("Обновление")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /booking/{id} — бронирование полностью обновляется")
    void updateBookingTest() {
        int bookingId = createBooking(randomBooking());
        Booking updated = randomBooking();
        updated.setAdditionalneeds("Dinner");

        Booking received = api.updateBooking(bookingId, updated, api.adminToken())
                .then()
                .spec(responseSpec(200))
                .body("additionalneeds", equalTo("Dinner"))
                .extract().as(Booking.class);

        assertThat(received).isEqualTo(updated);
    }

    @Test
    @Story("Обновление")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("PATCH /booking/{id} — меняется только переданное поле")
    void partialUpdateBookingTest() {
        Booking booking = randomBooking();
        int bookingId = createBooking(booking);

        Booking received = api.partialUpdateBooking(bookingId, Map.of("firstname", "Updated"), api.adminToken())
                .then()
                .spec(responseSpec(200))
                .body("firstname", equalTo("Updated"))
                .extract().as(Booking.class);

        assertThat(received.getLastname()).isEqualTo(booking.getLastname());
        assertThat(received.getTotalprice()).isEqualTo(booking.getTotalprice());
    }

    @Test
    @Story("Удаление")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /booking/{id} — бронирование удаляется")
    void deleteBookingTest() {
        int bookingId = createBooking(randomBooking());

        api.deleteBooking(bookingId, api.adminToken())
                .then()
                .spec(responseSpec(201));

        api.getBooking(bookingId)
                .then()
                .spec(responseSpec(404));
    }

    @Test
    @Story("Удаление")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("DELETE /booking/{id} без токена — 403, бронирование остаётся")
    void deleteBookingWithoutTokenTest() {
        int bookingId = createBooking(randomBooking());

        api.deleteBooking(bookingId, null)
                .then()
                .spec(responseSpec(403));

        api.getBooking(bookingId)
                .then()
                .spec(responseSpec(200));
    }

    @ParameterizedTest(name = "GET /booking/{0} — бронирования не существует")
    @ValueSource(ints = {999999999, 888888888})
    @Story("Получение")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("GET /booking/{id} несуществующего бронирования — 404")
    void getNonExistingBookingTest(int bookingId) {
        api.getBooking(bookingId)
                .then()
                .spec(responseSpec(404));
    }

    private int createBooking(Booking booking) {
        return api.createBooking(booking)
                .then()
                .spec(responseSpec(200))
                .extract().path("bookingid");
    }
}
