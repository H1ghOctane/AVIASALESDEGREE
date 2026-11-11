package ui.config;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Map;

public final class BrowserOptionsFactory {

    private BrowserOptionsFactory() {
    }

    public static MutableCapabilities create(String browser, boolean remote) {
        MutableCapabilities options = switch (browser.toLowerCase()) {
            case "chrome" -> chromeOptions(remote);
            case "firefox" -> new FirefoxOptions();
            default -> new MutableCapabilities();
        };
        if (remote) {
            options.setCapability("selenoid:options", Map.of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
        }
        return options;
    }

    private static ChromeOptions chromeOptions(boolean remote) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-features=AutoDeElevate", "--disable-dev-shm-usage", "--no-sandbox");
        if (remote) {
            options.setCapability("se:cdpEnabled", false);
        }
        return options;
    }
}
