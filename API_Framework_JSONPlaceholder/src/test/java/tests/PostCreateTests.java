package tests;

import base.BaseTest;
import client.PostAPIClient;
import io.restassured.response.Response;
import models.Post;
import org.junit.jupiter.api.Test;
import utils.PostTestDataFactory;

import static org.junit.jupiter.api.Assertions.*;

public class PostCreateTests extends BaseTest {

    PostAPIClient client = new PostAPIClient();

    // Note: In a real system with persistent data,
    // these tests might be part of a create-update-delete flow
    // to ensure proper cleanup and avoid test data pollution.


    @Test  //TC_POST_01
    public void testCreatePost_validData() {

        Post post = PostTestDataFactory.createValidPost();

        Response response = client.createPost(post);

        assertEquals(201, response.statusCode());
        validateJsonResponse(response, "post-schema.json");

        Post responsePost = response.as(Post.class);
        assertEquals(post.title, responsePost.title);
        assertEquals(post.body, responsePost.body);
        assertEquals(post.userId, responsePost.userId);

        assertTrue(responsePost.id > 0);

        assertTrue(response.time() < MAX_RESPONSE_TIME,"Response time exceeded threshold");
    }

    @Test  //TC_POST_02
    public void testCreatePost_invalidData(){

        Post post = PostTestDataFactory.createInvalidPost();

        Response response = client.createPost(post);

        assertEquals(201, response.statusCode());
        validateJsonResponse(response, "post-schema.json");

        Post responsePost = response.as(Post.class);

        // Validate mock behavior: API accepts invalid payload
        assertTrue(responsePost.id > 0);
        assertNull(responsePost.title);
        assertEquals("", responsePost.body);
        assertEquals(-1, responsePost.userId);

        assertTrue(response.time() < MAX_RESPONSE_TIME,
                "Response time exceeded threshold");
    }
}
