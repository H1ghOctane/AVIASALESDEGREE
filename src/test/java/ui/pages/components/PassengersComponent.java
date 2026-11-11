package ui.pages.components;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PassengersComponent {

    private static final Duration AUTO_OPEN_TIMEOUT = Duration.ofSeconds(2);

    private final SelenideElement root = $("[data-test-id='trip-class-and-number-passengers']");
    private final SelenideElement increaseAdults =
            root.$("[data-test-id='number-of-adults'] [data-test-id='increase-button']");

    public void setAdults(SelenideElement passengersField, int adults) {
        open(passengersField);
        for (int i = 1; i < adults; i++) {
            increaseAdults.click();
        }
        close(passengersField);
    }

    private void open(SelenideElement passengersField) {
        if (!root.is(visible, AUTO_OPEN_TIMEOUT)) {
            passengersField.click();
        }
        root.shouldBe(visible);
    }

    private void close(SelenideElement passengersField) {
        passengersField.click();
        root.should(disappear);
    }
}
