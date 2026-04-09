package base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BaseTest {

    protected static final long MAX_RESPONSE_TIME = 2000;

    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    protected void validateJsonResponse(Response response, String schema) {
        response.then()
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath(schema));
    }

}
