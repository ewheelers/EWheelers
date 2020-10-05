package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.ActivityModels.DashboardList;
import com.ewheelers.ewheelers.R;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.HomeHolder> {
    Context context;
    List<DashboardList> header;

    public HomeRecyclerAdapter(Context context, List<DashboardList> header) {
        this.context = context;
        this.header = header;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_layout,parent,false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {

        holder.imageView.setImageDrawable(header.get(position).getImage());
        holder.headingIs.setText(header.get(position).getTitle());
        holder.headingIs.setTextColor(Color.parseColor("#9C3C34"));
        holder.attrOne.setText(header.get(position).getAttr_one());
        holder.attrOnePrice.setText(header.get(position).getAttr_one_price());
        holder.attrTwo.setText(header.get(position).getAtrr_two());
        holder.attrTwoPrice.setText(header.get(position).getAttr_two_price());

    }

    @Override
    public int getItemCount() {
        return header.size();
    }

    public class HomeHolder extends RecyclerView.ViewHolder {
        TextView headingIs,attrOne,attrTwo,attrOnePrice,attrTwoPrice;
        ImageView imageView;
        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon_img);
            headingIs = itemView.findViewById(R.id.dash_title);
            attrOne = itemView.findViewById(R.id.attr_one);;
            attrTwo = itemView.findViewById(R.id.attr_two);;
            attrOnePrice = itemView.findViewById(R.id.attr_one_price);;
            attrTwoPrice = itemView.findViewById(R.id.attr_two_price);;

        }
    }
}
