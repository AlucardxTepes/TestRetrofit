package com.alucard.testretrofit;

import com.alucard.testretrofit.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Alucard on 29-Jun-17.
 */

public interface ApiEndpoint {
  
  @GET("posts")
  Call<List<Post>> getPostsList();
  
  @GET("posts/{id}")
  Call<Post> getPost(@Path("id") int postId);
  
  @POST("posts")
  Call<Post> createPost(@Body Post post);
}
