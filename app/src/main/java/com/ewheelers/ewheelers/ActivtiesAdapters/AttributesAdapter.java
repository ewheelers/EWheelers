package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.Activities.UpdateAttributes;
import com.ewheelers.ewheelers.ActivityModels.Attributes;
import com.ewheelers.ewheelers.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AttributesAdapter extends RecyclerView.Adapter<AttributesAdapter.HolderIs> {
    Context context;
    List<Attributes> attributesList;
    String id_is;
    List<String> strings = new ArrayList<>();
    public AttributesAdapter(Context context, List<Attributes> attributesList) {
        this.context = context;
        this.attributesList = attributesList;
    }

    @NonNull
    @Override
    public HolderIs onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attributes_layout, parent, false);
        return new HolderIs(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderIs holder, final int position) {
        holder.checkBox.setText(attributesList.get(position).getProductname());
        //String id = attributesList.get(position).getProductid();
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    id_is = attributesList.get(position).getProductid();
                    strings.add(id_is);
                }else {
                    strings.remove(attributesList.get(position).getProductid());
                }
                String list = Arrays.toString(strings.toArray()).replace("[", "").replace("]", "");
                if(context instanceof UpdateAttributes){
                    ((UpdateAttributes)context).attributeslist(list);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributesList.size();
    }

    public class HolderIs extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public HolderIs(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.mark_box);
        }
    }
}
