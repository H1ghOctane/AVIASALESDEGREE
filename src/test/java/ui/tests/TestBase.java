package ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import ui.helpers.Attach;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import ui.config.WebConfig;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public class TestBase {

    private static final WebConfig webConfig = ConfigFactory.create(WebConfig.class, System.getProperties());

    @BeforeAll
    static void setUpOptions() {
        Configuration.pageLoadStrategy = "normal";
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 30_000;
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-features=AutoDeElevate");
        Configuration.browserCapabilities = chromeOptions;
    //    Configuration.remote = "https://user1:1234@selenoid.qa.guru/wd/hub";
        Configuration.baseUrl = webConfig.baseUrl();
        Configuration.browserSize = "1920x1080";
        //   Configuration.pageLoadStrategy = "eager";
        Configuration.remote = webConfig.remoteUrl();
        chromeOptions.addArguments("--disable-dev-shm-usage", "--no-sandbox");
        chromeOptions.setCapability("se:cdpEnabled", false);
        chromeOptions.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableVideo", true
        ));
    }

//    @BeforeEach
//    void setUpUrl() {
//        open(webConfig.baseUrl());
//    }

    @AfterEach
    void addAttachments() {
        if (Selenide.webdriver().driver().hasWebDriverStarted()) {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
            Attach.addVideo();
            closeWebDriver();
        }
    }
}
