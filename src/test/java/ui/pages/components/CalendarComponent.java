package ui.pages.components;

import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static ui.helpers.Waits.appears;
import static ui.helpers.Waits.openPanel;

public class CalendarComponent {

    private static final DateTimeFormatter DAY_ID = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final SelenideElement actionButton = $("[data-test-id='calendar-action-button']");

    public void openWith(SelenideElement dateField, LocalDate date) {
        openPanel(dateField, day(date), visible);
    }

    public void selectDate(LocalDate date) {
        day(date).shouldBe(visible).click(usingJavaScript());
    }

    public void confirm() {
        if (appears(actionButton, visible)) {
            actionButton.click(usingJavaScript());
        }
    }

    private SelenideElement day(LocalDate date) {
        return $("[data-test-id='date-" + date.format(DAY_ID) + "']");
    }
}