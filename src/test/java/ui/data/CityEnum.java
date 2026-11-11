package ui.data;

public enum CityEnum {

    MOSCOW("Москва", "Москв"),
    SAINTPETERSBURG("Санкт-Петербург", "Санкт-Петер"),
    SOCHI("Сочи", "Соч"),
    KALININGRAD("Калининград", "Калинингр");

    private final String title;
    private final String query;

    CityEnum(String title, String query) {
        this.title = title;
        this.query = query;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Начало названия, которое вводится в поле, чтобы появилась подсказка с городом
     */
    public String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return title;
    }
}
