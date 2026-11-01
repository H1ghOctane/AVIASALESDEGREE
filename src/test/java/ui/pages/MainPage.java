package ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final SelenideElement body = $("body");

    @Step("Открыть сервис «{serviceName}»")
    public MainPage openService(String serviceName) {
        $$("a").findBy(text(serviceName)).click();
        return this;
    }

    @Step("Проверить, что на странице есть текст «{expectedText}»")
    public MainPage checkPageHasText(String expectedText) {
        body.shouldHave(text(expectedText));
        return this;
    }

    @Step("Проверить, что элемент меню «{elementName}» отображается")
    public MainPage checkHeaderElementVisible(String elementName) {
        $(byText(elementName)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что на странице отображается город «{cityName}»")
    public MainPage checkCityVisible(String cityName) {
        $$("a, button, h2, h3, span").findBy(text(cityName))
                .scrollTo()
                .shouldBe(visible);
        return this;
    }
}
