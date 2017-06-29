package com.alucard.testretrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alucard.testretrofit.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostListActivity extends AppCompatActivity {
  
  RecyclerView mRecyclerView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post_list);
    
    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MainActivity.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    
    ApiEndpoint apiService = retrofit.create(ApiEndpoint.class);
    Call<List<Post>> call = apiService.getPostsList();
    call.enqueue(new Callback<List<Post>>() {
      @Override
      public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
        for(Post p : response.body()) {
          Log.i("TAG", "onResponse: " + p.getTitle());
        }
        PostAdapter adapter = new PostAdapter(response.body(), R.layout.post_list_item);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PostListActivity.this));
        mRecyclerView.setAdapter(adapter);
      }
  
      @Override
      public void onFailure(Call<List<Post>> call, Throwable t) {
        Log.e("TAG", "onFailure: " + t.getMessage() );
      }
    });
  }
  
  private class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    
    private List<Post> mDataset;
    private int mItemLayout;
  
    public PostAdapter(List<Post> dataset, int itemLayout) {
      this.mDataset = dataset;
      this.mItemLayout = itemLayout;
    }
  
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayout, parent, false);
      return new PostViewHolder(v);
    }
  
    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
      Post post = mDataset.get(position);
      holder.mTextView.setText(post.getTitle());
    }
  
    @Override
    public int getItemCount() {
      return mDataset.size();
    }
  
    public class PostViewHolder extends RecyclerView.ViewHolder {
      
      public TextView mTextView;
  
      public PostViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.textView);
      }
    }
  }
}
