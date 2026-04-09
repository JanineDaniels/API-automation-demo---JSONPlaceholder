package tests;

import base.BaseTest;
import client.PostAPIClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PostGetTests extends BaseTest {

    PostAPIClient client = new PostAPIClient();

    @Test  //TC_GET_01
    public void testGetAllPosts() {
        Response response = client.getAllPosts();


        assertEquals(200, response.statusCode());
        response.then().contentType(ContentType.JSON);
        response.then()
                .body(matchesJsonSchemaInClasspath("post-array-schema.json"));
        response.then()
                .body("size()", greaterThan(0));

        assertTrue(response.time() < MAX_RESPONSE_TIME, "Response time exceeded threshold");
    }

    //TC_GET_02 + TC_GET_03
    // Covers both valid and invalid ID scenarios from the test plan.
    // In a different team/project, these could be separated into distinct test methods if requested.
    @ParameterizedTest(name = "{2} → id={0}, expectedStatus={1}")
    @CsvSource({
            "1, 200, TC_GET_02",
            "9999, 404, TC_GET_03"
    })
    public void testGetPostById_scenarios(int id, int expectedStatus, String testCaseId) {

        Response response = client.getPostById(id);

        assertEquals(expectedStatus, response.statusCode(), testCaseId + " failed");

        if (id == 1) {

            validateJsonResponse(response, "post-schema.json");
            assertEquals(id, response.jsonPath().getInt("id"));
        }else {
            validateJsonResponse(response, "empty-schema.json");
        }

        assertTrue(response.time() < MAX_RESPONSE_TIME, "Response time exceeded threshold");
    }


}
