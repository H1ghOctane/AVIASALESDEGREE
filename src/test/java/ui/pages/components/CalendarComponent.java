package ui.pages.components;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.actions;

public class CalendarComponent {

    private static final DateTimeFormatter DAY_ID = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final SelenideElement root = $("[data-test-id='exact-calendar']");

    public void selectDate(LocalDate date) {
        root.shouldBe(visible)
                .$("[data-test-id='date-" + date.format(DAY_ID) + "']")
                .click();
    }

    public void close() {
        actions().sendKeys(Keys.ESCAPE).perform();
    }
}
