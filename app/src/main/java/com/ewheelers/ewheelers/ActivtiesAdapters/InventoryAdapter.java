package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.ActivityModels.InventoryModel;
import com.ewheelers.ewheelers.R;

import java.util.ArrayList;
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
                holder.cloneLayout.setVisibility(View.GONE);
                holder.button_edit.setBackgroundResource(R.drawable.grey_color_bg);
                holder.clonebtn.setBackgroundResource(R.drawable.grey_button);

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
                        holder.button_edit.setBackgroundResource(R.drawable.grey_button);
                        holder.clonebtn.setBackgroundResource(R.drawable.grey_button);
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
        holder.clonebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cloneLayout.setVisibility(View.VISIBLE);
                holder.linearLayout.setVisibility(View.GONE);
                holder.clonebtn.setBackgroundResource(R.drawable.grey_color_bg);
                holder.button_edit.setBackgroundResource(R.drawable.grey_button);
            }
        });
        holder.forSale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.extraLayout.setVisibility(View.VISIBLE);
                }else {
                    holder.extraLayout.setVisibility(View.GONE);
                }
            }
        });
        holder.forRent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
            }
        });
       holder.textViewClose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               holder.cloneLayout.setVisibility(View.GONE);
               holder.clonebtn.setBackgroundResource(R.drawable.grey_button);
           }
       });
       holder.addSplPrice.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openBottomSheet(v);
           }
       });
       holder.addVolDis.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openBottomSheetVol(v);
           }
       });

    }

    private void openBottomSheetVol(View v) {
        Context context=v.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate (R.layout.vol_bottom_layout, null);
        ImageButton imageButton = view.findViewById(R.id.close_btn);
        RecyclerView recyclerView = view.findViewById(R.id.splPricelist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        SplPriceAdapter splPriceAdapter = new SplPriceAdapter(context,getList());
        recyclerView.setAdapter(splPriceAdapter);
        final Dialog mBottomSheetDialog = new Dialog (context, R.style.Theme_MaterialComponents_BottomSheetDialog);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show ();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
    }

    private void openBottomSheet(View v) {
        Context context=v.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate (R.layout.bottom_sheet_layout, null);
        ImageButton imageButton = view.findViewById(R.id.close_btn);
        RecyclerView recyclerView = view.findViewById(R.id.splPricelist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        SplPriceAdapter splPriceAdapter = new SplPriceAdapter(context,getList());
        recyclerView.setAdapter(splPriceAdapter);
        final Dialog mBottomSheetDialog = new Dialog (context, R.style.Theme_MaterialComponents_BottomSheetDialog);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show ();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
    }

    private List<InventoryModel> getList() {
        List<InventoryModel> inventoryModels = new ArrayList<>();
        inventoryModels.add(new InventoryModel("NEXZU Roasluck","2020-12-18","2020-12-22","\u20B9 90000.00"));
        inventoryModels.add(new InventoryModel("NEXZU Roasluck","2020-12-18","2020-12-22","\u20B9 90000.00"));
        inventoryModels.add(new InventoryModel("NEXZU Roasluck","2020-12-18","2020-12-22","\u20B9 90000.00"));
        inventoryModels.add(new InventoryModel("NEXZU Roasluck","2020-12-18","2020-12-22","\u20B9 90000.00"));
        inventoryModels.add(new InventoryModel("NEXZU Roasluck","2020-12-18","2020-12-22","\u20B9 90000.00"));

        return inventoryModels;
    }


    @Override
    public int getItemCount() {
        return inventoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,sel_Price,rent_qty,avail_qty;
        TextView button_edit,cancelGen,cancelRen,clonebtn,addSplPrice,addVolDis;
        RadioGroup radioGroup;
        TextView textViewClose;
        LinearLayout linearLayout,genLayout,renLayout,cloneLayout,extraLayout;
        CheckBox forSale,forRent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addVolDis = itemView.findViewById(R.id.add_vol_discount);
            addSplPrice = itemView.findViewById(R.id.add_spl_price);
            extraLayout = itemView.findViewById(R.id.for_sale_layout);
            textViewClose = itemView.findViewById(R.id.closeBtn);
            forSale = itemView.findViewById(R.id.for_sale);
            forRent = itemView.findViewById(R.id.for_rent);
            cloneLayout = itemView.findViewById(R.id.clone_layout);
            clonebtn = itemView.findViewById(R.id.clone);
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
