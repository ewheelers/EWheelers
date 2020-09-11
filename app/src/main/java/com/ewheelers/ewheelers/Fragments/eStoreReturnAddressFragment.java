package com.ewheelers.ewheelers.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.ProductViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class eStoreReturnAddressFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    public eStoreReturnAddressFragment() {
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
        View v = inflater.inflate(R.layout.fragment_e_store_return_address, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new eStoreSubGeneralFragment(), "General");
        adapter.addFragment(new eStoreSubEnglishFragment(), "English");
        viewPager.setAdapter(adapter);
    }

}
