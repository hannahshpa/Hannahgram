package com.hannahpark.hannahgram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hannahpark.hannahgram.model.Post;

import org.parceler.Parcels;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder>   {

    private List<Post> mPosts;
    Context context;

    public GridAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @NonNull
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.photo_grid, parent, false);
        GridAdapter.ViewHolder viewHolder = new GridAdapter.ViewHolder(postView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Post post = mPosts.get(i);
        String url = post.getImage().getUrl();

        Glide.with(context)
                .load(url)
                .into(holder.ivPhoto);

    }

    @Override
    public int getItemCount() { return mPosts.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivPhoto;

        public ViewHolder(View postView) {
            super(postView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);

            postView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //get the position of the item
            int position = getAdapterPosition();
            //check if the position is valid and exists in the view
            if(position != RecyclerView.NO_POSITION) {
                //get movie at selected position
                Post post = mPosts.get(position);
                //create the intent for this activity
                Intent intent = new Intent(context, PostDetailsActivity.class);
                //serialize the movie using parceler, use its short name as a key
                intent.putExtra("post", Parcels.wrap(post));

                //show the activity
                context.startActivity(intent);
            }
        }
    }
}
