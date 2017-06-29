package com.alucard.testretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alucard.testretrofit.model.Post;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.alucard.testretrofit.R.id.myTextView;

public class MainActivity extends AppCompatActivity {
  public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
  
  private TextView mTextView;
  private Button mPostListButton;
  private Button mAddPostButton;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    mPostListButton = (Button) findViewById(R.id.postListButton);
    
    mPostListButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(MainActivity.this, PostListActivity.class);
        startActivity(i);
      }
    });
    
    
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    
    final ApiEndpoint apiService = retrofit.create(ApiEndpoint.class);
    final Call<Post> call = apiService.getPost(1);
    call.enqueue(new Callback<Post>() {
      @Override
      public void onResponse(Call<Post> call, Response<Post> response) {
        int statusCode = response.code();
        Post post = response.body();
        mTextView = (TextView) findViewById(myTextView);
        mTextView.setText("StatusCode: " + statusCode + ".\nPost title: " + post.getTitle() +
        "\nID: " + post.getId() + "\nUserID: " + post.getUserId()  + "\nBody: " + post.getBody());
      }
  
      @Override
      public void onFailure(Call<Post> call, Throwable t) {
        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
      }
    });
  
    // Add post
    mAddPostButton = (Button) findViewById(R.id.addPostButton);
    mAddPostButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Post newPost = new Post();
        newPost.setId(666);
        newPost.setUserId(1);
        newPost.setTitle("A new post");
        newPost.setBody("This is a new post");
        Call<Post> call = apiService.createPost(newPost);
        call.enqueue(new Callback<Post>() {
          @Override
          public void onResponse(Call<Post> call, Response<Post> response) {
            Toast.makeText(MainActivity.this, "Created Post: " + response.body().getTitle(), Toast.LENGTH_LONG).show();
          }
  
          @Override
          public void onFailure(Call<Post> call, Throwable t) {
            Log.e("TAG", "onFailure: ", t );
          }
        });
      }
    });
  }
}
