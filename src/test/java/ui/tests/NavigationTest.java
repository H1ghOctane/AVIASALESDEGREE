package ui.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ui.pages.MainPage;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static io.qameta.allure.Allure.step;

@Epic("Aviasales UI")
@Feature("Навигация")
@Owner("H1ghOctane")
@Tag("ui")
public class NavigationTest extends TestBase {

    private final MainPage mainPage = new MainPage();

    @Test
    @Story("Главная страница")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Главная страница открывается по базовому адресу")
    void mainPageOpensTest() {
        step("Проверить адрес открытой страницы", () ->
                webdriver().shouldHave(urlContaining("aviasales")));
    }

    @ParameterizedTest(name = "В шапке отображается пункт: {0}")
    @ValueSource(strings = {"Профиль", "Журнал", "Поддержка"})
    @Story("Шапка сайта")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Проверка видимости пунктов шапки")
    void headerElementsAreVisibleTest(String element) {
        mainPage.checkHeaderElementVisible(element);
    }
}
