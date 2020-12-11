package com.ewheelers.ewheelers.ActivtiesAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelers.Activities.WebViewActivity;
import com.ewheelers.ewheelers.ActivityModels.ClassifiedModel;
import com.ewheelers.ewheelers.Network.VolleySingleton;
import com.ewheelers.ewheelers.R;

import java.util.List;

public class Classifieds_Adapter extends RecyclerView.Adapter<Classifieds_Adapter.ViewHolder> {
    Context context;
    List<ClassifiedModel> classifieds_classes;

    public Classifieds_Adapter(Context context, List<ClassifiedModel> classifieds_classes) {
        this.context = context;
        this.classifieds_classes = classifieds_classes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.classified_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageLoader imageLoaderBann = VolleySingleton.getInstance(context).getImageLoader();
        imageLoaderBann.get(classifieds_classes.get(position).getImageurl(), ImageLoader.getImageListener(holder.imgIs, 0, 0));
        holder.imgIs.setImageUrl(classifieds_classes.get(position).getImageurl(), imageLoaderBann);
        holder.imgIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WebViewActivity.class);
                i.putExtra("urlIs", classifieds_classes.get(position).getUrl());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classifieds_classes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView imgIs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIs = itemView.findViewById(R.id.classi);
        }
    }
}
