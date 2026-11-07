package ui.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ui.data.CityEnum;
import ui.pages.MainPage;

@Epic("Aviasales UI")
@Feature("Навигация")
@Story("Раздел «Отели»")
@Owner("H1ghOctane")
@Tag("ui")
public class NavigationTest extends TestBase {

    private final MainPage mainPage = new MainPage();

    @ParameterizedTest(name = "Через шапку открываются «Отели» с городом {0}")
    @EnumSource(CityEnum.class)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Переход в «Отели» через шапку и проверка популярных городов")
    void hotelsSectionShowsCityTest(CityEnum city) {
        mainPage.openPage()
                .header()
                .openHotels()
                .checkOpened()
                .checkCityVisible(city.getTitle());
    }
}
