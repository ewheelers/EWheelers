package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.R;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeHolder> {
    Context context;
    List<String> header;

    public HomeRecyclerAdapter(Context context, List<String> header) {
        this.context = context;
        this.header = header;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        holder.headingIs.setText(header.get(position));
    }

    @Override
    public int getItemCount() {
        return header.size();
    }

    public class HomeHolder extends RecyclerView.ViewHolder {
        TextView headingIs;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            headingIs = itemView.findViewById(R.id.heading);
        }
    }
}
