package tests;

import base.BaseTest;
import client.PostAPIClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostDeleteTests extends BaseTest {

    PostAPIClient client = new PostAPIClient();

    // Note: In a real system with persistent data,
    // these tests might be part of a create-update-delete flow
    // to ensure proper cleanup and avoid test data pollution.

    @Test  //TC_DELETE_01a
    public void testDeletePost_validId() {

        Response response = client.deletePost(1);

        assertEquals(200, response.statusCode());

        String body = response.asString();
        assertTrue(body.equals("{}") || body.isEmpty(), "Unexpected response body");
        validateJsonResponse(response, "empty-schema.json");

        assertTrue(response.time() < MAX_RESPONSE_TIME, "Response time exceeded threshold");
    }

    @Test  //TC_DELETE_01b
    public void testDeletePost_doesNotRemoveResource() {

        Response deleteResponse = client.deletePost(1);
        assertEquals(200, deleteResponse.statusCode());

        Response getResponse = client.getPostById(1);
        assertEquals(200, getResponse.statusCode());
        getResponse.then().contentType(ContentType.JSON);
        // Mock API does not enact actual deletion - real API should return 404

        assertEquals(1, getResponse.jsonPath().getInt("id"));
    }

    @Test  //TC_DELETE_03
    public void testDeletePost_invalidId() {

        Response response = client.deletePost(9999);

        assertEquals(200, response.statusCode());
        // Mock API returns 200 for non-existent delete - real API should return 404

        String body = response.asString();
        assertTrue(body.equals("{}") || body.isEmpty());
        response.then().contentType(ContentType.JSON);

        assertTrue(response.time() < MAX_RESPONSE_TIME, "Response time exceeded threshold");
    }
}
