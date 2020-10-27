package com.ewheelers.ewheelers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_store_settings);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        imageView = findViewById(R.id.homeBack);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Home.class);
                startActivity(i);
                finish();
            }
        });
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
        /*String shopaddress = new SessionPreference().getStrings(eStoreSettings.this,SessionPreference.shopaddress);
        String zipcode = new SessionPreference().getStrings(eStoreSettings.this,SessionPreference.zipcode);
        String latitude = new SessionPreference().getStrings(eStoreSettings.this,SessionPreference.latitude);
        String longtitude = new SessionPreference().getStrings(eStoreSettings.this,SessionPreference.logitude);*/
        SessionPreference.clearString(eStoreSettings.this,SessionPreference.shopaddress);
        SessionPreference.clearString(eStoreSettings.this,SessionPreference.zipcode);
        SessionPreference.clearString(eStoreSettings.this,SessionPreference.latitude);
        SessionPreference.clearString(eStoreSettings.this,SessionPreference.logitude);
        SessionPreference.clearString(eStoreSettings.this, SessionPreference.identifier);
        SessionPreference.clearString(eStoreSettings.this, SessionPreference.seourl);
        SessionPreference.clearString(eStoreSettings.this, SessionPreference.phone);
        SessionPreference.clearString(eStoreSettings.this, SessionPreference.freeship);
        SessionPreference.clearString(eStoreSettings.this, SessionPreference.maxsell);
        SessionPreference.clearString(eStoreSettings.this, SessionPreference.maxrent);
        Intent i = new Intent(getApplicationContext(),Home.class);
        startActivity(i);
        finish();
        drawer.openDrawer(Gravity.LEFT);
    }


}
