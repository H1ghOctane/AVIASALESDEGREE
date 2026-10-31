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
        // Настройка Allure
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true)
        );

        // Базовые настройки Selenide
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 5_000;
        Configuration.pageLoadTimeout = 60_000;
        Configuration.pageLoadStrategy = "eager";

        // Определяем окружение
        boolean isJenkins = System.getenv("JENKINS_HOME") != null;
        boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");

        // Включаем headless только в CI (Jenkins/Linux), чтобы локально видеть браузер
        if (isJenkins || isLinux) {
            Configuration.headless = true;
        }

        // Настройка ChromeOptions
        ChromeOptions options = new ChromeOptions();

        // Если из Jenkins передан путь к Chromium, применяем его
        String chromeBinary = System.getProperty("chrome.binary");
        if (chromeBinary != null && !chromeBinary.isEmpty()) {
            options.setBinary(chromeBinary);
        }

        // Базовые флаги для стабильной работы в Linux/Docker
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        // Дополнительные флаги для предотвращения падений в контейнерах
        options.addArguments("--remote-debugging-port=9222");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-setuid-sandbox");
        options.addArguments("--lang=ru-RU");
        options.addArguments("--timezone=Europe/Moscow");

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