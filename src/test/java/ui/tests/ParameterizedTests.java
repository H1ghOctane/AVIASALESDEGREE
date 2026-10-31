package ui.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import ui.CityEnum;
import ui.TestBase;
import ui.pages.MainPage;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

@Epic("Aviasales UI")
@Feature("Главная страница")
@Tag("ui")
public class ParameterizedTests extends TestBase {

    private MainPage mainPage;

    @BeforeEach
    void initPage() {
        mainPage = new MainPage();   // создаём ПОСЛЕ open() из TestBase
    }

    static Stream<Arguments> checkCities() {
        return Stream.of(
                Arguments.of(CityEnum.MOSCOW),
                Arguments.of(CityEnum.SAINTPETERSBURG),
                Arguments.of(CityEnum.SOCHI),
                Arguments.of(CityEnum.KALININGRAD)
        );
    }

    @Story("Проверка сервисов")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest(name = "Услуга «{0}» содержит описание: «{1}»")
    @CsvSource(value = {
            "Избранное   | Жмите на сердечко",
            "Впечатления | Впечатления",
            "Авиабилеты  | Тут покупают дешёвые авиабилеты",
            "Отели       | Здесь бронируют балдёжные отели",
    }, delimiter = '|')
    @DisplayName("Проверка описания сервисов на главной странице")
    void serviceTest(String service, String expectedText) {
        mainPage.clickService(service);
        mainPage.getBody().shouldHave(text(expectedText));
    }

    @Story("Проверка городов")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest(name = "Отели есть в городе: {0}")
    @MethodSource("checkCities")
    @DisplayName("Проверка наличия отелей в выбранном городе")
    void checkCityTest(CityEnum city) {
        mainPage.clickService("Отели");
        mainPage.getCityElement(city.getTitle())
                .scrollTo()
                .shouldBe(visible);
    }

    @Story("Проверка навигации")
    @Severity(SeverityLevel.MINOR)
    @ParameterizedTest(name = "Отображается кнопка: {0}")
    @ValueSource(strings = {"Профиль", "Журнал", "Поддержка", "rub"})
    @DisplayName("Проверка видимости элементов навигации")
    void visionOfServicesTest(String service) {
        mainPage.getHeaderElement(service).shouldBe(visible);
    }
}