package ui.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    public SelenideElement getServiceLink(String serviceName) {
        return $$("a").findBy(text(serviceName));
    }

    public void clickService(String serviceName) {
        getServiceLink(serviceName).click();
    }

    public SelenideElement getCityElement(String cityName) {
        return $$("a, button, h2, h3, span").findBy(text(cityName));
    }

    public SelenideElement getBody() {
        return $("body");
    }

    public SelenideElement getHeaderElement(String elementName) {
        return $(byText(elementName));
    }
}