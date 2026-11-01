package ui.data;

public enum CityEnum {

        MOSCOW("Москва"),
        SAINTPETERSBURG("Санкт-Петербург" ),
        SOCHI("Сочи" ),
        KALININGRAD("Калининград");

        private final String title;

        CityEnum(String title) {
                this.title = title;
        }

        public String getTitle() {
                return title;
        }
}
