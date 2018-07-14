package com.hannahpark.hannahgram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {

    private List<String> mComments;
    Context context;

    public CommentAdapter(ArrayList<String> comments) {
        mComments = comments;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final String comment = mComments.get(i);
        try {
            holder.tvUsername.setText(ParseUser.getCurrentUser().fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvComment.setText(comment);

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvComment) TextView tvComment;

        public ViewHolder(View postView) {
            super(postView);
            ButterKnife.bind(this, postView);
        }
    }
}
