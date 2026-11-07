package ui.data;

import java.time.LocalDate;

public final class SearchData {

    private SearchData() {
    }

    public static LocalDate departDate() {
        return LocalDate.now().plusDays(10);
    }

    public static LocalDate returnDate() {
        return departDate().plusDays(5);
    }
}
