package api.config;

import org.aeonbits.owner.Config;

/**
 * Приоритет источников (сверху вниз):
 * 1. -D параметры (так значения передаёт Jenkins);
 * 2. файл aviasales.properties в домашней папке пользователя (секреты для локального запуска, в git не попадает);
 * 3. config/api.properties из репозитория (только несекретные значения).
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "file:~/aviasales.properties",
        "classpath:config/api.properties"
})
public interface ApiConfig extends Config {

    @Key("travelpayouts.baseUrl")
    String travelpayoutsBaseUrl();

    @Key("travelpayouts.token")
    String travelpayoutsToken();

    @Key("booker.baseUrl")
    String bookerBaseUrl();

    @Key("booker.username")
    String bookerUsername();

    @Key("booker.password")
    String bookerPassword();
}
