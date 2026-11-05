package ui.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ui.pages.MainPage;

import static ui.data.CityEnum.MOSCOW;
import static ui.data.CityEnum.SAINTPETERSBURG;
import static ui.data.SearchData.departDate;

@Epic("Aviasales UI")
@Feature("Выдача билетов")
@Story("Фильтры")
@Owner("H1ghOctane")
@Tag("ui")
public class SearchFiltersTest extends TestBase {

    private final MainPage mainPage = new MainPage();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Фильтр «Без пересадок» оставляет в выдаче только прямые рейсы")

    void directFlightsFilterTest() {
        mainPage.openPage()
                .setOrigin(MOSCOW.getTitle())
                .setDestination(SAINTPETERSBURG.getTitle())
                .setOneWayDate(departDate())
                .submitSearch()
                .checkOpened()
                .waitForTickets()
                .applyDirectFlightsFilter()
                .checkAllFlightsDirect();
    }
}
