package utils;

import models.Post;

public class PostTestDataFactory {

    public static Post createValidPost() {
        Post post = new Post();
        post.userId = 1;
        post.title = "test title";
        post.body = "test body";
        return post;
    }

    public static Post createInvalidPost() {
        Post post = new Post();
        post.userId = -1;
        post.title = null;
        post.body = "";
        return post;
    }
}
