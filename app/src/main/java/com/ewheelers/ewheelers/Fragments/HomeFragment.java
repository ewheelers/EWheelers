package com.ewheelers.ewheelers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ewheelers.ewheelers.Activities.eStoreSettings;
import com.ewheelers.ewheelers.ActivtiesAdapters.HomeRecyclerAdapter;
import com.ewheelers.ewheelers.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    RecyclerView recyclerView;
    HomeRecyclerAdapter homeRecyclerAdapter;
    TextView estorsetting;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.recycler_list);
        estorsetting = v.findViewById(R.id.estoresetting);
        homeRecyclerAdapter = new HomeRecyclerAdapter(getActivity(),stringList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(homeRecyclerAdapter);
        estorsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), eStoreSettings.class);
                startActivity(i);
            }
        });
        return v;
    }

    public List<String> stringList(){
        List<String> strings = new ArrayList<>();
        strings.add("Parking Orders");
        strings.add("Charging Orders");
        strings.add("Repair Orders");
        return strings;
    }
}
