package api.specs;

import api.config.ApiConfig;
import api.config.ApiConfigProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static api.helpers.CustomAllureListener.withCustomTemplates;

public final class ApiSpecs {

    private static final ApiConfig config = ApiConfigProvider.CONFIG;

    public static final RequestSpecification travelpayoutsRequestSpec = new RequestSpecBuilder()
            .setBaseUri(config.travelpayoutsBaseUrl())
            .setContentType(ContentType.JSON)
            .setAccept("application/json")
            .addFilter(withCustomTemplates())
            .build();

    public static final ResponseSpecification successResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .build();

    private ApiSpecs() {
    }

    public static RequestSpecification travelpayoutsAuthorizedSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(travelpayoutsRequestSpec)
                .addHeader("X-Access-Token", config.travelpayoutsToken())
                .build();
    }
}
