package ui.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ui.pages.MainPage;

import static ui.data.CityEnum.*;
import static ui.data.SearchData.departDate;
import static ui.data.SearchData.returnDate;

@Epic("Aviasales UI")
@Feature("Поиск авиабилетов")
@Owner("H1ghOctane")
@Tag("ui")
public class SearchFormTest extends TestBase {

    private final MainPage mainPage = new MainPage();

    @Test
    @Story("Поиск")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Поиск билетов в одну сторону открывает выдачу с введёнными городами")
    void oneWaySearchTest() {
        mainPage.openPage()
                .setOrigin(MOSCOW)
                .setDestination(SAINTPETERSBURG)
                .setOneWayDate(departDate())
                .submitSearch()
                .checkOpened()
                .checkRoute(MOSCOW, SAINTPETERSBURG);
    }

    @Test
    @Story("Поиск")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Поиск туда-обратно на 2 пассажиров открывает выдачу с введёнными городами")
    void roundTripSearchTest() {
        mainPage.openPage()
                .setOrigin(MOSCOW)
                .setDestination(SOCHI)
                .setRoundTripDates(departDate(), returnDate())
                .setAdults(2)
                .submitSearch()
                .checkOpened()
                .checkRoute(MOSCOW, SOCHI);
    }

    @Test
    @Story("Форма поиска")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Кнопка смены городов меняет вылет и прилёт местами")
    void swapCitiesTest() {
        mainPage.openPage()
                .setOrigin(MOSCOW)
                .setDestination(SOCHI)
                .swapCities()
                .checkCities(SOCHI, MOSCOW);
    }

    @ParameterizedTest(name = "По запросу «{0}» подсказывается город «{1}»")
    @CsvSource({
            "Каз,  Казань",
            "Екат, Екатеринбург",
            "Кали, Калининград"
    })
    @Story("Форма поиска")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Подсказки городов по началу названия")
    void citySuggestionsTest(String prefix, String city) {
        mainPage.openPage()
                .typeDestination(prefix)
                .checkSuggestionVisible(city);
    }

    @Test
    @Story("Форма поиска")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Выбор трёх взрослых пассажиров отражается в поле формы")
    void passengersCountTest() {
        mainPage.openPage()
                .setAdults(3)
                .checkPassengers("3 пассажира");
    }
}
