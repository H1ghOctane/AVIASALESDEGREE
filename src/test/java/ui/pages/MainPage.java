package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import ui.data.CityEnum;
import ui.pages.components.CalendarComponent;
import ui.pages.components.HeaderComponent;
import ui.pages.components.PassengersComponent;

import java.time.Duration;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class MainPage {

    private static final Duration SUGGESTIONS_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration GEO_AUTOFILL_TIMEOUT = Duration.ofSeconds(10);

    private final SelenideElement form = $("[data-test-id='avia-form']");
    private final SelenideElement originInput = form.$("[data-test-id='origin-input']");
    private final SelenideElement destinationField = form.$$("[data-test-id='destination-autocomplete']").first();
    private final SelenideElement destinationInput = form.$("[data-test-id='destination-input']");
    private final SelenideElement swapButton = form.$("[data-test-id='round-button']");
    private final SelenideElement departDateField = form.$("[data-test-id='start-date-field']");
    private final SelenideElement passengersField = form.$("[data-test-id='passengers-field']");
    private final SelenideElement passengersCount = form.$("[data-test-id='passenger-numbers']");
    private final SelenideElement hotelsCheckbox = $("[data-test-id='hotels-checkbox']");
    private final SelenideElement submitButton = form.$("[data-test-id='form-submit']");
    private final ElementsCollection suggestions = $$("[data-test-id^='suggested-city-']");

    private final HeaderComponent header = new HeaderComponent();
    private final CalendarComponent calendar = new CalendarComponent();
    private final PassengersComponent passengers = new PassengersComponent();

    @Step("Открыть главную страницу")
    public MainPage openPage() {
        open("/");
        form.shouldBe(visible);
        waitForGeoAutofill();
        return this;
    }

    public HeaderComponent header() {
        return header;
    }

    @Step("Указать город вылета «{city}»")
    public MainPage setOrigin(CityEnum city) {
        chooseCity(originInput, city);
        return this;
    }

    @Step("Указать город прибытия «{city}»")
    public MainPage setDestination(CityEnum city) {
        chooseCity(destinationInput, city);
        return this;
    }

    @Step("Ввести в поле «Куда» начало названия «{prefix}»")
    public MainPage typeDestination(String prefix) {
        typeCity(destinationInput, prefix);
        return this;
    }

    @Step("Поменять города местами")
    public MainPage swapCities() {
        swapButton.click();
        return this;
    }

    @Step("Выбрать дату вылета {date} без обратного билета")
    public MainPage setOneWayDate(LocalDate date) {
        calendar.openWith(departDateField, date);
        calendar.selectDate(date);
        calendar.confirm();
        return this;
    }

    @Step("Выбрать даты: туда {departDate}, обратно {returnDate}")
    public MainPage setRoundTripDates(LocalDate departDate, LocalDate returnDate) {
        calendar.openWith(departDateField, departDate);
        calendar.selectDate(departDate);
        calendar.selectDate(returnDate);
        calendar.confirm();
        return this;
    }

    @Step("Указать взрослых пассажиров: {adults}")
    public MainPage setAdults(int adults) {
        passengers.setAdults(passengersField, adults);
        return this;
    }

    @Step("Нажать «Найти билеты»")
    public SearchResultsPage submitSearch() {
        disableHotelsInNewTab();
        submitButton.click();
        return new SearchResultsPage();
    }

    @Step("Нажать «Найти билеты» при ошибке в форме")
    public MainPage submitInvalidSearch() {
        submitButton.click();
        return this;
    }

    @Step("Проверить города в форме: «{origin}» → «{destination}»")
    public MainPage checkCities(CityEnum origin, CityEnum destination) {
        originInput.shouldHave(value(origin.getTitle()));
        destinationInput.shouldHave(value(destination.getTitle()));
        return this;
    }

    @Step("Проверить, что в подсказках есть город «{city}»")
    public MainPage checkSuggestionVisible(String city) {
        suggestions.findBy(text(city)).shouldBe(visible);
        return this;
    }

    @Step("Проверить поле пассажиров: «{expected}»")
    public MainPage checkPassengers(String expected) {
        passengersCount.shouldHave(text(expected));
        return this;
    }

    @Step("Проверить ошибку в поле «Куда»: «{message}», поиск не запустился")
    public MainPage checkDestinationError(String message) {
        destinationField.$$("[data-test-id='text']").findBy(text(message)).shouldBe(visible);
        webdriver().shouldNotHave(urlContaining("/search/"));
        return this;
    }

    private void disableHotelsInNewTab() {
        if (hotelsCheckbox.exists() && hotelsCheckbox.$("input").isSelected()) {
            hotelsCheckbox.click();
        }
    }

    /**
     * Через несколько секунд после загрузки сайт запрашивает nearest_places.json
     * и подставляет в «Откуда» ближайший город по IP. Если начать ввод раньше,
     * подстановка затрёт введённый тестом город. Ждём её не дольше 10 секунд;
     * если сайт ничего не подставил, тест просто продолжается
     */
    private void waitForGeoAutofill() {
        originInput.is(not(empty), GEO_AUTOFILL_TIMEOUT);
    }

    private void chooseCity(SelenideElement input, CityEnum city) {
        typeCity(input, city.getQuery());
        SelenideElement suggestion = suggestions.findBy(text(city.getTitle()));
        if (suggestion.is(visible, SUGGESTIONS_TIMEOUT)) {
            suggestion.click();
        }
        input.shouldHave(value(city.getTitle()));
    }

    /**
     * clear() и setValue() здесь не подходят: поле управляется React, после clear() сайт
     * возвращает прежний город и новый текст склеивается с ним («АстанаМоскв»).
     * Поэтому старое значение стираем обычными клавишами Backspace и Delete
     * (они одинаково работают на Windows, Linux и macOS, в отличие от Ctrl+A / Cmd+A)
     * и только потом вводим текст
     */
    private void typeCity(SelenideElement input, String text) {
        input.click();
        String oldValue = input.getValue();
        if (oldValue != null && !oldValue.isEmpty()) {
            input.sendKeys(repeat(Keys.BACK_SPACE, oldValue.length()) + repeat(Keys.DELETE, oldValue.length()));
        }
        input.shouldHave(exactValue(""));
        input.sendKeys(text);
    }

    private static String repeat(Keys key, int times) {
        return String.valueOf(key).repeat(times);
    }
}