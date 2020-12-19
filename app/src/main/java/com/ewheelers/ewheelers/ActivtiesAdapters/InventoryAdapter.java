package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ewheelers.ewheelers.Activities.ManageInventory;
import com.ewheelers.ewheelers.ActivityModels.InventoryModel;
import com.ewheelers.ewheelers.Fragments.eStoreEnglishFragment;
import com.ewheelers.ewheelers.Fragments.eStoreGeneralFragment;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.ProductViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    Context context;
    List<InventoryModel> inventoryModels;

    public InventoryAdapter(Context context, List<InventoryModel> inventoryModels) {
        this.context = context;
        this.inventoryModels = inventoryModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_list_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(inventoryModels.get(position).getName());
        holder.sel_Price.setText(inventoryModels.get(position).getSell_price());
        holder.avail_qty.setText(inventoryModels.get(position).getAvail_qty());
        holder.rent_qty.setText(inventoryModels.get(position).getRental_qty());
        holder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.linearLayout.setVisibility(View.VISIBLE);
            }
        });
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if(rb.getText().toString().equals("Close")){
                        group.clearCheck();
                        holder.linearLayout.setVisibility(View.GONE);
                        holder.genLayout.setVisibility(View.GONE);
                    }
                    if(rb.getText().toString().equals("General")){
                        holder.genLayout.setVisibility(View.VISIBLE);
                        holder.renLayout.setVisibility(View.GONE);
                    }
                    if(rb.getText().toString().equals("Rental")){
                        holder.renLayout.setVisibility(View.VISIBLE);
                        holder.genLayout.setVisibility(View.GONE);
                    }
                }

            }
        });
        holder.cancelGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) holder.radioGroup.findViewById(holder.radioGroup.getCheckedRadioButtonId());
                holder.radioGroup.clearCheck();
                if(rb!=null && !rb.isChecked()){
                    holder.genLayout.setVisibility(View.GONE);
                }
            }
        });
        holder.cancelRen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) holder.radioGroup.findViewById(holder.radioGroup.getCheckedRadioButtonId());
                holder.radioGroup.clearCheck();
                if(rb!=null && !rb.isChecked()){
                    holder.renLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return inventoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,sel_Price,rent_qty,avail_qty;
        TextView button_edit,cancelGen,cancelRen;
        RadioGroup radioGroup;
        LinearLayout linearLayout,genLayout,renLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cancelRen = itemView.findViewById(R.id.cancel_ren);
            cancelGen = itemView.findViewById(R.id.cancel_gen);
            name = itemView.findViewById(R.id.name);
            sel_Price = itemView.findViewById(R.id.selprice);
            rent_qty = itemView.findViewById(R.id.rentqty);
            avail_qty = itemView.findViewById(R.id.avilqty);
            button_edit = itemView.findViewById(R.id.edit);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            linearLayout = itemView.findViewById(R.id.tabs);
            genLayout = itemView.findViewById(R.id.gen_detail);
            renLayout = itemView.findViewById(R.id.ren_detail);
        }
    }
}
