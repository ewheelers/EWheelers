package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.ActivityModels.OrdersModel;
import com.ewheelers.ewheelers.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderHolder> {
    Context context;
    private List<OrdersModel> ordersModelsList;

    public OrdersAdapter(Context context, List<OrdersModel> ordersModels) {
        this.context = context;
        this.ordersModelsList = ordersModels;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_order_layout, parent, false);
        return new OrderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        holder.order_id.setText("Order Id: "+ordersModelsList.get(position).getOrderid());
        holder.invoice_no.setText("Invoice: "+ordersModelsList.get(position).getInvoiceno());
        holder.dateadded.setText("Date: "+ordersModelsList.get(position).getDate());
        holder.productname.setText(ordersModelsList.get(position).getProductname());
        holder.vehtype.setText(ordersModelsList.get(position).getVehtype());
        holder.amount.setText("Amount: \u20B9 "+ordersModelsList.get(position).getAmount());
        holder.status.setText("Status: "+ordersModelsList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return ordersModelsList.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        TextView order_id,invoice_no,dateadded,productname,vehtype,amount,status;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.orderid);
            invoice_no = itemView.findViewById(R.id.invocieno);
            dateadded = itemView.findViewById(R.id.date);
            productname = itemView.findViewById(R.id.produ_name);
            vehtype = itemView.findViewById(R.id.vehtype);
            amount = itemView.findViewById(R.id.amount);
            status  = itemView.findViewById(R.id.status);
        }
    }
}
