package ui.pages.components;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import ui.pages.HotelsPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class HeaderComponent {

    private ElementsCollection menuLinks() {
        return $$("nav").findBy(visible).$$("a");
    }

    @Step("Перейти в раздел «Отели» через шапку")
    public HotelsPage openHotels() {
        menuLinks().findBy(text("Отели")).click();
        return new HotelsPage();
    }
}
