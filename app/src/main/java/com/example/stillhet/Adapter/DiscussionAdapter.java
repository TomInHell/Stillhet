package com.example.stillhet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stillhet.R;
import com.example.stillhet.StatesForAdapter.DiscussionState;

import java.util.ArrayList;
import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<DiscussionState> adapter;
    private final OnStateClickListener onClickListener;

    public DiscussionAdapter(Context context, ArrayList<DiscussionState> adapter, OnStateClickListener onClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.adapter = adapter;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public DiscussionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.discussion_row, parent, false);
        return new ViewHolder(view);
    }

    public interface OnStateClickListener{
        void onStateClick(DiscussionState state, int position);
    }

    @Override
    public void onBindViewHolder(DiscussionAdapter.ViewHolder holder, int position) {
        DiscussionState state = adapter.get(position);
        holder.Head.setText(state.getHead());
        holder.Theme.setText(state.getDiscussTheme());
        holder.Creator.setText(state.getCreator());
        holder.Body.setText(state.getBody());
        holder.Like.setText(state.getLike());

        holder.itemView.setOnClickListener(v -> onClickListener.onStateClick(state,position));
    }

    @Override
    public int getItemCount() {
        return adapter.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView Head, Theme, Creator, Body, Like;
        ViewHolder(View view){
            super(view);
            Head = view.findViewById(R.id.discussion_title);
            Theme = view.findViewById(R.id.Discuss_Theme);
            Creator = view.findViewById(R.id.Discuss_Creator);
            Body = view.findViewById(R.id.disc_body);
            Like = view.findViewById(R.id.discussion_like);
        }
    }
}