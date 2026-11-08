package ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.noneMatch;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class SearchResultsPage {

    private static final Duration RESULTS_TIMEOUT = Duration.ofSeconds(60);

    private final SelenideElement form = $("[data-test-id='avia-form']");
    private final SelenideElement originInput = form.$("[data-test-id='origin-input']");
    private final SelenideElement destinationInput = form.$("[data-test-id='destination-input']");
    private final SelenideElement transfersFilter = $("[data-test-id='set-filter-transfers_count']");
    private final ElementsCollection tickets = $$("[data-test-id^='bdui-ticket-preview-normal-']");

    @Step("Проверить, что открылась выдача билетов")
    public SearchResultsPage checkOpened() {
        webdriver().shouldHave(urlContaining("/search/"), RESULTS_TIMEOUT);
        return this;
    }

    @Step("Проверить маршрут в форме выдачи: «{origin}» → «{destination}»")
    public SearchResultsPage checkRoute(String origin, String destination) {
        originInput.shouldHave(value(origin));
        destinationInput.shouldHave(value(destination));
        return this;
    }

    @Step("Дождаться загрузки билетов")
    public SearchResultsPage waitForTickets() {
        tickets.shouldHave(sizeGreaterThan(0), RESULTS_TIMEOUT);
        return this;
    }

    @Step("Оставить в фильтре «Пересадки» только прямые рейсы")
    public SearchResultsPage applyDirectFlightsFilter() {
        transfersFilter.shouldBe(visible).scrollIntoView("{block: 'center'}");
        for (SelenideElement row : transfersFilter.$$("[data-test-id^='set-filter-row-']").asFixedIterable()) {
            boolean isDirect = "set-filter-row-0".equals(row.getAttribute("data-test-id"));
            if (row.$("input").isSelected() != isDirect) {
                row.click();
            }
        }
        transfersFilter.$("[data-test-id='set-filter-row-0'] input").shouldBe(checked);
        return this;
    }

    @Step("Проверить, что все рейсы в выдаче прямые")
    public SearchResultsPage checkAllFlightsDirect() {
        tickets.shouldHave(sizeGreaterThan(0), RESULTS_TIMEOUT)
                .shouldHave(noneMatch("Нет билетов с пересадками",
                        ticket -> ticket.getText().matches("(?s).*\\d+\\s+пересад.*")), RESULTS_TIMEOUT);
        return this;
    }
}