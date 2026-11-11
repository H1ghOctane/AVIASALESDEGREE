package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HotelsPage {

    private final SelenideElement title = $("h1[data-test-id='brand-text']");
    private final SelenideElement hotelForm = $("[data-test-id='hotel-form']");
    private final ElementsCollection cityChips = $$("[data-test-id='chip']");

    @Step("Проверить, что открыт раздел «Отели»")
    public HotelsPage checkOpened() {
        title.shouldHave(text("отели"));
        hotelForm.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в подборке «Где жить и что смотреть» есть город «{city}»")
    public HotelsPage checkCityVisible(String city) {
        cityChips.findBy(text(city)).scrollTo().shouldBe(visible);
        return this;
    }
}
