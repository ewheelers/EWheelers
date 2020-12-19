package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.ewheelers.ewheelers.ActivityModels.InventoryModel;
import com.ewheelers.ewheelers.ActivtiesAdapters.InventoryAdapter;
import com.ewheelers.ewheelers.Fragments.eStoreEnglishFragment;
import com.ewheelers.ewheelers.Fragments.eStoreGeneralFragment;
import com.ewheelers.ewheelers.Fragments.eStoreMediaFragment;
import com.ewheelers.ewheelers.Fragments.eStoreReturnAddressFragment;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.ProductViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageInventory extends AppCompatActivity {
RecyclerView recyclerView;
InventoryAdapter inventoryAdapter;
InventoryModel inventoryModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);
        recyclerView = findViewById(R.id.list_of_prods);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        inventoryAdapter = new InventoryAdapter(getApplicationContext(),getList());
        recyclerView.setAdapter(inventoryAdapter);
    }

    private List<InventoryModel> getList() {
        List<InventoryModel> inventoryModels = new ArrayList<>();
        inventoryModels.add(new InventoryModel("NEXZU Rompus 7.8","\u20B9 27000","10","2"));
        inventoryModels.add(new InventoryModel("NEXZU Roadluck","\u20B9 37000","5","2"));

        return inventoryModels;
    }
    public void setupViewPager(ViewPager viewPager) {
        ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new eStoreGeneralFragment(), "General");
        adapter.addFragment(new eStoreEnglishFragment(), "English");
        viewPager.setAdapter(adapter);
    }
}
