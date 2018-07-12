package com.hannahpark.hannahgram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hannahpark.hannahgram.model.Post;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>  {

    private List<Post> mPosts;
    Context context;

    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Post post = mPosts.get(i);
        try {
            holder.tvUsername.setText(post.getUser().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //  holder.ivImage.setImageBitmap(BitmapFactory.decodeFile(post.getImage()));
        //holder.ivProfileImage.set
        Context context = holder.itemView.getContext();
        String caption = post.getDescription();
        String url = post.getImage().getUrl();
        holder.tvCaption.setText(caption);

        Glide.with(context)
                .load(url)
//                .bitmapTransform(new RoundedCornersTransformation(context, 8, 0))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivPhoto);

//        holder.ivPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = DetailActivity.newIntent(context, post);
//                NavigationUtils.navigate(context, intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public ImageView ivPhoto;
        public TextView tvCaption;

        public ViewHolder(View postView) {
            super(postView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfilePic);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);

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

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
