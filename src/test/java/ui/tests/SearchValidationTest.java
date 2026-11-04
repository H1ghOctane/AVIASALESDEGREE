package ui.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ui.pages.MainPage;

import static ui.data.CityEnum.MOSCOW;
import static ui.data.SearchData.departDate;

@Epic("Aviasales UI")
@Feature("Поиск авиабилетов")
@Story("Валидация формы")
@Owner("H1ghOctane")
@Tag("ui")
public class SearchValidationTest extends TestBase {

    private final MainPage mainPage = new MainPage();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Поиск без города прибытия не запускается и показывает ошибку")
    void emptyDestinationTest() {
        mainPage.openPage()
                .setOrigin(MOSCOW.getTitle())
                .setOneWayDate(departDate())
                .submitInvalidSearch()
                .checkDestinationError("Укажите город прибытия");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Поиск с одинаковыми городами вылета и прилёта не запускается")
    void sameCitiesTest() {
        mainPage.openPage()
                .setOrigin(MOSCOW.getTitle())
                .setDestination(MOSCOW.getTitle())
                .setOneWayDate(departDate())
                .submitInvalidSearch()
                .checkDestinationError("Укажите разные города");
    }
}
