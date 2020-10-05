package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.Activities.ParkingHubDetailActivity;
import com.ewheelers.ewheelers.ActivityModels.Attributes;
import com.ewheelers.ewheelers.R;

import java.util.List;

public class DashBoardSettingsAdapter extends RecyclerView.Adapter<DashBoardSettingsAdapter.DashHolder> {
    Context context;
    List<Attributes> attributes;

    public DashBoardSettingsAdapter(Context context, List<Attributes> attributes) {
        this.context = context;
        this.attributes = attributes;
    }

    @NonNull
    @Override
    public DashHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_settings_layout, parent, false);
        return new DashHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashHolder holder, final int position) {
        //holder.hub_manage.setText(attributes.get(position).getProductid());
        holder.hub_name.setText(attributes.get(position).getProductname());
        holder.hub_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ParkingHubDetailActivity.class);
                i.putExtra("hubname",attributes.get(position).getProductname());
                i.putExtra("hubid",attributes.get(position).getProductid());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributes.size();
    }

    public class DashHolder extends RecyclerView.ViewHolder {
        TextView hub_name,hub_manage;
        public DashHolder(View v) {
            super(v);
            hub_manage = v.findViewById(R.id.hub_manage);
            hub_name = v.findViewById(R.id.hub_name);
        }
    }
}
