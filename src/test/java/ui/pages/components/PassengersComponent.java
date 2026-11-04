package ui.pages.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static ui.helpers.Waits.appears;
import static ui.helpers.Waits.openPanel;

public class PassengersComponent {

    private final SelenideElement root = $("[data-test-id='trip-class-and-number-passengers']");
    private final SelenideElement readyButton = $("[data-test-id='passengers-ready-button']");

    public void setAdults(SelenideElement passengersField, int adults) {
        openPanel(passengersField, root, visible);
        SelenideElement increase = root.$("[data-test-id='number-of-adults'] [data-test-id='increase-button']");
        for (int i = 1; i < adults; i++) {
            increase.click();
        }
        close(passengersField);
    }

    private void close(SelenideElement passengersField) {
        if (appears(readyButton, visible)) {
            readyButton.click();
        } else {
            passengersField.click();
        }
        root.should(disappear);
    }
}