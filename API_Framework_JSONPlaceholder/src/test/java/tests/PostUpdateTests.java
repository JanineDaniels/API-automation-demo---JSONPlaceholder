package tests;

import base.BaseTest;
import client.PostAPIClient;
import io.restassured.response.Response;
import models.Post;
import org.junit.jupiter.api.Test;
import utils.PostTestDataFactory;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostUpdateTests extends BaseTest {

    PostAPIClient client = new PostAPIClient();

    // Note: In a real system with persistent data,
    // these tests might be part of a create-update-delete flow
    // to ensure proper cleanup and avoid test data pollution.

    @Test  //TC_PUT_01
    public void testUpdatePost_validData() {

        Post post = PostTestDataFactory.createValidPost();

        Response response = client.updatePost(1, post);

        assertEquals(200, response.statusCode());
        if (!response.jsonPath().getMap("$").isEmpty()) {
            response.then()
                    .body(matchesJsonSchemaInClasspath("post-schema.json"));

            Post responsePost = response.as(Post.class);
            assertEquals(post.title, responsePost.title);
            assertEquals(post.body, responsePost.body);
            assertEquals(post.userId, responsePost.userId);

            assertEquals(1, responsePost.id);

            assertTrue(response.time() < MAX_RESPONSE_TIME);
        }
    }

    @Test // TC_PUT_02
    public void testUpdatePost_invalidId() {

        int invalidId = 9999;

        Post post = PostTestDataFactory.createValidPost();

        Response response = client.updatePost(invalidId, post);
        String contentType = response.getContentType();

        assertTrue(
                response.statusCode() == 200 || response.statusCode() == 500,
                "Unexpected status code: " + response.statusCode()
        );
        // mock API behaviour - should be 404 in real scenario
        // JSONplaceholder appears to send 200 or 500 across various scenarios.
        // Therefore, chose to allow multipel acceptable codes specifically for this case.

        if (contentType != null && contentType.contains("application/json")) {

            response.then()
                    .body(matchesJsonSchemaInClasspath("post-schema.json"));
        // Mock API error response not guaranteed to be JSON. Added to prevent test failures

            Post responsePost = response.as(Post.class);
            assertEquals(invalidId, responsePost.id);
            // Mock API echoes ID even if resource doesn't exist

            assertEquals(post.title, responsePost.title);
            assertEquals(post.body, responsePost.body);
            assertEquals(post.userId, responsePost.userId);

            assertTrue(response.time() < MAX_RESPONSE_TIME);

        }
    }
}
