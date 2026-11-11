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

    @Disabled("В Selenoid на выдаче Aviasales появляется капча, билеты не загружаются")
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Фильтр «Прямые рейсы» оставляет в выдаче только рейсы без пересадок")
    void directFlightsFilterTest() {
        mainPage.openPage()
                .setOrigin(MOSCOW)
                .setDestination(SAINTPETERSBURG)
                .setOneWayDate(departDate())
                .submitSearch()
                .checkOpened()
                .waitForTickets()
                .applyDirectFlightsFilter()
                .checkAllFlightsDirect();
    }
}
