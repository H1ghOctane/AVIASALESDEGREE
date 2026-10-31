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
        Configuration.headless = true;

        ChromeOptions options = new ChromeOptions();

        // Читаем путь к браузеру из системного свойства, переданного из Jenkins
        String chromeBinary = System.getProperty("chrome.binary");
        if (chromeBinary != null && !chromeBinary.isEmpty()) {
            options.setBinary(chromeBinary);
        } else {
            // Локальный запуск (Windows/Mac)
            // options.setBinary("C:/Program Files/Google/Chrome/Application/chrome.exe");
        }

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