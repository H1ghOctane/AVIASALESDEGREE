package api.specs;

import api.config.ApiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;

import static api.helpers.CustomAllureListener.withCustomTemplates;

public class ApiSpecs {

    private static final ApiConfig config = ConfigFactory.create(ApiConfig.class, System.getProperties());

    public static final RequestSpecification travelpayoutsRequestSpec = baseRequestSpec(config.travelpayoutsBaseUrl());

    public static final RequestSpecification bookerRequestSpec = baseRequestSpec(config.bookerBaseUrl());

    private ApiSpecs() {
    }

    public static RequestSpecification travelpayoutsAuthorizedSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(travelpayoutsRequestSpec)
                .addHeader("X-Access-Token", config.travelpayoutsToken())
                .build();
    }

    public static ResponseSpecification responseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .build();
    }

    private static RequestSpecification baseRequestSpec(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                // Строго один тип: ContentType.JSON шлёт в Accept список из четырёх типов,
                // и restful-booker отвечает на POST /booking статусом 418
                .setAccept("application/json")
                .addFilter(withCustomTemplates())
                .build();
    }
}