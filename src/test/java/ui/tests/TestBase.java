package ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import ui.config.BrowserOptionsFactory;
import ui.config.WebConfig;
import ui.helpers.Attach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    private static final WebConfig webConfig = ConfigFactory.create(WebConfig.class, System.getProperties());

    @BeforeAll
    static void setUpOptions() {
        Configuration.baseUrl = webConfig.baseUrl();
        Configuration.browser = webConfig.browser();
        Configuration.browserVersion = webConfig.browserVersion();
        Configuration.browserSize = webConfig.browserSize();
        Configuration.headless = webConfig.headless();
        Configuration.remote = webConfig.remoteUrl();
        Configuration.pageLoadStrategy = "eager";
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 30_000;
        Configuration.browserCapabilities = BrowserOptionsFactory.create(webConfig.browser(), isRemote());
    }

    @AfterEach
    void addAttachments() {
        if (Selenide.webdriver().driver().hasWebDriverStarted()) {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
            Attach.addVideo();
            closeWebDriver();
        }
    }

    private static boolean isRemote() {
        return TestBase.webConfig.remoteUrl() != null && !TestBase.webConfig.remoteUrl().isBlank();
    }

    private static boolean isChrome() {
        return "chrome".equalsIgnoreCase(webConfig.browser());
    }
}
