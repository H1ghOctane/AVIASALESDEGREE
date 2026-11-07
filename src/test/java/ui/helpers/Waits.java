package ui.helpers;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;

import java.time.Duration;

public final class Waits {

    private static final Duration AUTO_OPEN_TIMEOUT = Duration.ofSeconds(2);
    private static final int MAX_CLICKS = 3;

    private Waits() {
    }

    public static boolean appears(SelenideElement element, WebElementCondition condition, Duration timeout) {
        try {
            element.shouldBe(condition, timeout);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public static boolean appears(SelenideElement element, WebElementCondition condition) {
        return appears(element, condition, AUTO_OPEN_TIMEOUT);
    }

    public static void openPanel(SelenideElement trigger, SelenideElement panel, WebElementCondition opened) {
        for (int i = 0; i < MAX_CLICKS && !appears(panel, opened); i++) {
            trigger.click();
        }
        panel.shouldBe(opened);
    }
}