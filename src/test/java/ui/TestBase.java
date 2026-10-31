package ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;
import org.selenide.videorecorder.junit5.VideoRecorderExtension;
import ui.config.WebConfig;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

@ExtendWith(VideoRecorderExtension.class)
public class TestBase {

    private static final WebConfig webConfig = ConfigFactory.create(WebConfig.class, System.getProperties());

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true)
        );

        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 60_000;
        Configuration.pageLoadStrategy = "eager";

        // Включаем headless (без окна), так как мы в Linux
        Configuration.headless = true;

        ChromeOptions options = new ChromeOptions();
        // Указываем браузер Chromium, который есть в контейнере
        options.setBinary("/usr/bin/chromium-browser");

        // Флаги для стабильной работы в Docker
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        Configuration.browserCapabilities = options;
    }

    @BeforeEach
    void setUpUrl() {
        open(webConfig.baseUrl());
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}