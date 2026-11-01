package api.data;

import api.models.booker.Booking;
import api.models.booker.BookingDates;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.util.Locale;

public class BookingData {

    private static final Faker faker = new Faker(Locale.ENGLISH);

    private BookingData() {
    }

    public static Booking randomBooking() {
        LocalDate checkin = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        LocalDate checkout = checkin.plusDays(faker.number().numberBetween(1, 14));

        return Booking.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(100, 1000))
                .depositpaid(faker.bool().bool())
                .bookingdates(BookingDates.builder()
                        .checkin(checkin.toString())
                        .checkout(checkout.toString())
                        .build())
                .additionalneeds(faker.options().option("Breakfast", "Lunch", "Dinner", "Late checkout"))
                .build();
    }
}
