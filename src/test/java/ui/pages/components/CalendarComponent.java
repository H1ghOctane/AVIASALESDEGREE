package ui.pages.components;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class CalendarComponent {

    private static final DateTimeFormatter DAY_ID = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final Duration AUTO_OPEN_TIMEOUT = Duration.ofSeconds(2);

    private final SelenideElement actionButton = $("[data-test-id='calendar-action-button']");

    public void openWith(SelenideElement dateField, LocalDate date) {
        SelenideElement day = day(date);
        if (!day.is(visible, AUTO_OPEN_TIMEOUT)) {
            dateField.click();
        }
        day.shouldBe(visible);
    }

    public void selectDate(LocalDate date) {
        day(date).shouldBe(visible).click(usingJavaScript());
    }

    public void confirm() {
        if (actionButton.is(visible, AUTO_OPEN_TIMEOUT)) {
            actionButton.click(usingJavaScript());
        }
    }

    private SelenideElement day(LocalDate date) {
        return $("[data-test-id='date-" + date.format(DAY_ID) + "']");
    }
}
