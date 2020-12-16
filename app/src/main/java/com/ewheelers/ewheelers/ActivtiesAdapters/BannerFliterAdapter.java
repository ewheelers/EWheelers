package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.ActivityModels.OrdersModel;
import com.ewheelers.ewheelers.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class BannerFliterAdapter extends RecyclerView.Adapter<BannerFliterAdapter.ViewHolder> {
    Context context;
    List<OrdersModel> ordersModel;
    int indexrow = 0;

    public BannerFliterAdapter(Context context, List<OrdersModel> ordersModel) {
        this.context = context;
        this.ordersModel = ordersModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_filter_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chip.setText(ordersModel.get(position).getProductname());
        holder.chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexrow=position;
                notifyDataSetChanged();
            }
        });
        if(indexrow==position){
            holder.chip.setBackgroundResource(R.drawable.button_click_grad);
            holder.chip.setTextColor(Color.WHITE);
        }
        else
        {
            holder.chip.setBackgroundResource(R.drawable.transperantborder);
            holder.chip.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return ordersModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chip;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.choosechip);
        }
    }
}
