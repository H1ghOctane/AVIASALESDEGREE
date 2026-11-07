package api.config;

import org.aeonbits.owner.ConfigFactory;

public final class ApiConfigProvider {

    public static final ApiConfig CONFIG =
            ConfigFactory.create(ApiConfig.class, System.getProperties());

    private ApiConfigProvider() {
    }
}
