package client;

import constants.Endpoints;
import io.restassured.response.Response;
import models.Post;

import static io.restassured.RestAssured.given;

public class PostAPIClient {

    public Response getAllPosts(){
        return given()
                .when()
                .get(Endpoints.POSTS);
    }

    public Response getPostById(int id){
        return given()
                .when()
                .get(Endpoints.POSTS+"/"+id);
    }

    public Response createPost(Post post){
        return given()
                .contentType("application/json")
                .body(post)
                .when()
                .post(Endpoints.POSTS);
    }

    public Response updatePost(int id, Post post){
        return given()
                .contentType("application/json")
                .body(post)
                .when()
                .put(Endpoints.POSTS+"/"+id);
    }

    public Response deletePost(int id){
        return given()
                .when()
                .delete(Endpoints.POSTS+"/"+id);
    }



}
