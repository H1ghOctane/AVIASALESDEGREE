package ui.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ui.pages.MainPage;

@Epic("Aviasales UI")
@Feature("Сервисы Aviasales")
@Story("Описание сервисов на главной странице")
@Owner("H1ghOctane")
@Tag("ui")
public class ServicesTest extends TestBase {

    private final MainPage mainPage = new MainPage();

    @ParameterizedTest(name = "Сервис «{0}» показывает текст: «{1}»")
    @CsvSource(value = {
            "Избранное   | Жмите на сердечко",
            "Впечатления | Впечатления",
            "Авиабилеты  | Тут покупают дешёвые авиабилеты",
            "Отели       | Здесь бронируют балдёжные отели"
    }, delimiter = '|')
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Проверка описания сервиса после перехода по ссылке")
    void serviceDescriptionTest(String service, String expectedText) {
        mainPage.openService(service)
                .checkPageHasText(expectedText);
    }
}
