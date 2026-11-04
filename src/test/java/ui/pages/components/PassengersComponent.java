package ui.pages.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PassengersComponent {

    private final SelenideElement root = $("[data-test-id='trip-class-and-number-passengers']");

    public void setAdults(int adults) {
        SelenideElement increase = root.shouldBe(visible)
                .$("[data-test-id='number-of-adults'] [data-test-id='increase-button']");
        for (int i = 1; i < adults; i++) {
            increase.click();
        }
        root.$("[data-test-id='passengers-ready-button']").click();
    }
}
