package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.ActivityModels.InventoryModel;
import com.ewheelers.ewheelers.R;

import java.util.List;

public class SplPriceAdapter extends RecyclerView.Adapter<SplPriceAdapter.ViewHolder> {
    Context context;
    List<InventoryModel> inventoryModels;
    public SplPriceAdapter(Context context, List<InventoryModel> inventoryModels) {
        this.context = context;
        this.inventoryModels = inventoryModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spl_price_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(inventoryModels.get(position).getName());
        holder.startdate.setText(inventoryModels.get(position).getSell_price());
        holder.enddate.setText(inventoryModels.get(position).getAvail_qty());
        holder.splprice.setText(inventoryModels.get(position).getRental_qty());
    }

    @Override
    public int getItemCount() {
        return inventoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,startdate,enddate,splprice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            startdate = itemView.findViewById(R.id.startDate);
            enddate = itemView.findViewById(R.id.endDate);
            splprice = itemView.findViewById(R.id.splPrice);
        }
    }
}
