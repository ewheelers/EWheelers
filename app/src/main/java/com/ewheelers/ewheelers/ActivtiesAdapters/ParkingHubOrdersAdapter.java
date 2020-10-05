package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.Activities.ViewParkOrders;
import com.ewheelers.ewheelers.ActivityModels.HubOrdersModel;
import com.ewheelers.ewheelers.R;

import java.util.List;

public class ParkingHubOrdersAdapter extends RecyclerView.Adapter<ParkingHubOrdersAdapter.ParkHolder>{
    Context context;
    List<HubOrdersModel> hubOrdersModelList;

    public ParkingHubOrdersAdapter(Context context, List<HubOrdersModel> hubOrdersModelList) {
        this.context = context;
        this.hubOrdersModelList = hubOrdersModelList;
    }

    @NonNull
    @Override
    public ParkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_orders_layout,parent,false);
        return new ParkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkHolder holder, int position) {
        holder.completedOrders.setText(hubOrdersModelList.get(position).getCompletedOrders());
        holder.completedAmount.setText("\u20B9"+hubOrdersModelList.get(position).getCompletedAmount());
        holder.inprocessOrders.setText(hubOrdersModelList.get(position).getInprocessOrders());
        holder.inprocessAmount.setText("\u20B9"+hubOrdersModelList.get(position).getInprocessAmount());
        holder.pendingOrders.setText(hubOrdersModelList.get(position).getPendingOrders());
        holder.pendingAmount.setText("\u20B9"+hubOrdersModelList.get(position).getPendingAmount());
        holder.totalOrders.setText(hubOrdersModelList.get(position).getTotalOrders());
        holder.totalAmount.setText("\u20B9"+hubOrdersModelList.get(position).getTotalAmount());
        holder.orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewParkOrders.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hubOrdersModelList.size();
    }

    public class ParkHolder extends RecyclerView.ViewHolder {
        TextView completedOrders,completedAmount,inprocessOrders,inprocessAmount,pendingOrders,pendingAmount,
                totalOrders,totalAmount,orderView;
        public ParkHolder(View view) {
            super(view);
            completedOrders = view.findViewById(R.id.complete_order_count);
            completedAmount = view.findViewById(R.id.complete_order_amount);
            inprocessOrders = view.findViewById(R.id.inprocess_order_count);
            inprocessAmount = view.findViewById(R.id.inprocess_order_amount);
            pendingOrders = view.findViewById(R.id.pending_orders_count);
            pendingAmount = view.findViewById(R.id.pending_orders_amount);
            totalOrders = view.findViewById(R.id.total_orders_count);
            totalAmount = view.findViewById(R.id.total_orders_amount);
            orderView = view.findViewById(R.id.order_view);
        }
    }
}
