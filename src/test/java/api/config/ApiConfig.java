package api.config;

import org.aeonbits.owner.Config;

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
}
