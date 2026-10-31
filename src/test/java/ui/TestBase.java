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
        // Allure
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true)
        );

        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;
        Configuration.pageLoadTimeout = 60_000;
        Configuration.pageLoadStrategy = "eager";

        // Включаем headless (без окна), если мы в Jenkins или Linux
        String isJenkins = System.getenv("JENKINS_HOME");
        boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");
        if (isJenkins != null || isLinux) {
            Configuration.headless = true;
        }

        // Если передан параметр -Dselenide.remote, используем удаленный драйвер
        if (System.getProperty("selenide.remote") != null) {
            Configuration.remote = System.getProperty("selenide.remote");
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Обязательно для Docker/root
        options.addArguments("--disable-dev-shm-usage"); // Обязательно для Docker
        options.addArguments("--disable-gpu"); // Полезно для стабильности в Linux

        // ВОТ ЭТА СТРОКА БЫЛА ПРОПУЩЕНА:
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