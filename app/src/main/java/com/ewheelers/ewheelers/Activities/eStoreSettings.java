package com.ewheelers.ewheelers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ewheelers.ewheelers.Fragments.eStoreEnglishFragment;
import com.ewheelers.ewheelers.Fragments.eStoreGeneralFragment;
import com.ewheelers.ewheelers.Fragments.eStoreMediaFragment;
import com.ewheelers.ewheelers.Fragments.eStoreReturnAddressFragment;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.ProductViewPagerAdapter;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.ewheelers.ewheelers.Activities.Home.drawer;

public class eStoreSettings extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_store_settings);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
    }

    private void setupViewPager(ViewPager viewPager) {
        ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new eStoreGeneralFragment(), "General");
        adapter.addFragment(new eStoreEnglishFragment(), "English");
        adapter.addFragment(new eStoreReturnAddressFragment(), "Return Address");
        adapter.addFragment(new eStoreMediaFragment(), "Media");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){
        /*Intent i = new Intent(eStoreSettings.this,Home.class);
        startActivity(i);*/
        SessionPreference.clearString(eStoreSettings.this,SessionPreference.shopaddress);
        SessionPreference.clearString(eStoreSettings.this,SessionPreference.zipcode);
        SessionPreference.clearString(eStoreSettings.this,SessionPreference.latitude);
        SessionPreference.clearString(eStoreSettings.this,SessionPreference.logitude);
        finish();
        drawer.openDrawer(Gravity.LEFT);
    }


}
