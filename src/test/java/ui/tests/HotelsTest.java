package ui.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ui.data.CityEnum;
import ui.pages.MainPage;

@Epic("Aviasales UI")
@Feature("Отели")
@Story("Популярные города")
@Owner("H1ghOctane")
@Tag("ui")
public class HotelsTest extends TestBase {

    private final MainPage mainPage = new MainPage();

    @ParameterizedTest(name = "В разделе «Отели» отображается город: {0}")
    @EnumSource(CityEnum.class)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверка наличия города в разделе «Отели»")
    void cityIsShownInHotelsTest(CityEnum city) {
        mainPage.openService("Отели")
                .checkCityVisible(city.getTitle());
    }
}
