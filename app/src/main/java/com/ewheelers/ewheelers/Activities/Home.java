package com.ewheelers.ewheelers.Activities;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.ewheelers.ewheelers.Fragments.HomeFragment;
import com.ewheelers.ewheelers.Fragments.WalletFragment;
import com.ewheelers.ewheelers.R;

public class Home extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton,radioButto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        radioGroup = findViewById(R.id.bottomLayout);
        int id = radioGroup.getCheckedRadioButtonId();
        radioButto = findViewById(id);
        if(radioButto.isChecked()){
            FragmentTras();
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                radioButton = findViewById(id);
                switch (checkedId) {
                    case R.id.home_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
                        break;
                    case R.id.wallet_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new WalletFragment()).commit();
                        break;
                    case R.id.scan_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new WalletFragment()).commit();
                        break;
                    case R.id.messages_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new WalletFragment()).commit();
                        break;
                }
            }
        });
    }
    private void FragmentTras() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();
    }
}
