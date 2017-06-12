package com.example.somebody.eventmanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.somebody.eventmanager.R;

public class NoEventAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_event_layout,
                parent, false);
        return new NoEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    // а нужно ли это? вьюха-то одна
    class NoEventViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public NoEventViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.no_event_logo);
            textView = (TextView) itemView.findViewById(R.id.no_event_text);
        }
    }
}