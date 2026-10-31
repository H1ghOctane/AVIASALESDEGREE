package ui.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/web.properties"
})
public interface WebConfig extends Config {
    @Key("web.baseUrl")
    @DefaultValue("https://www.aviasales.ru/")
    String baseUrl();
}