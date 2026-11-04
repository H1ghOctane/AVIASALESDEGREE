package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import ui.pages.components.CalendarComponent;
import ui.pages.components.HeaderComponent;
import ui.pages.components.PassengersComponent;

import java.time.Duration;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static ui.helpers.Waits.appears;
import static ui.helpers.Waits.openPanel;

public class MainPage {

    private static final Duration SUGGESTIONS_TIMEOUT = Duration.ofSeconds(10);

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
    private final SelenideElement focusedInput = $("input:focus");

    private final HeaderComponent header = new HeaderComponent();
    private final CalendarComponent calendar = new CalendarComponent();
    private final PassengersComponent passengers = new PassengersComponent();

    @Step("Открыть главную страницу")
    public MainPage openPage() {
        open("/");
        form.shouldBe(visible);
        return this;
    }

    public HeaderComponent header() {
        return header;
    }

    @Step("Указать город вылета «{city}»")
    public MainPage setOrigin(String city) {
        chooseCity(originInput, "origin", city);
        return this;
    }

    @Step("Указать город прибытия «{city}»")
    public MainPage setDestination(String city) {
        chooseCity(destinationInput, "destination", city);
        return this;
    }

    @Step("Ввести в поле «Куда» начало названия «{prefix}»")
    public MainPage typeDestination(String prefix) {
        typeCity(destinationInput, "destination", prefix);
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
    public MainPage checkCities(String origin, String destination) {
        originInput.shouldHave(value(origin));
        destinationInput.shouldHave(value(destination));
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

    private void chooseCity(SelenideElement input, String fieldName, String city) {
        typeCity(input, fieldName, city.substring(0, city.length() - 1));
        SelenideElement suggestion = suggestions.findBy(text(city));
        if (appears(suggestion, visible, SUGGESTIONS_TIMEOUT)) {
            suggestion.click();
        }
        input.shouldHave(value(city));
    }

    private void typeCity(SelenideElement input, String fieldName, String city) {
        openPanel(input, focusedInput, attributeMatching("data-test-id", ".*" + fieldName + ".*"));
        actions().keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .perform();
        if (!city.isEmpty()) {
            actions().sendKeys(city).perform();
        }
    }
}