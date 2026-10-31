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
        // ... (код Allure)

        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 60_000;
        Configuration.pageLoadStrategy = "eager";

        // Включаем headless-режим автоматически, если мы в Jenkins (или Linux)
        String isJenkins = System.getenv("JENKINS_HOME");
        if (isJenkins != null || System.getProperty("os.name").toLowerCase().contains("linux")) {
            Configuration.headless = true;
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-features=AutoDeElevate");

        // Флаги для стабильной работы в Docker/Jenkins
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        Configuration.browserCapabilities = options;
    }
}