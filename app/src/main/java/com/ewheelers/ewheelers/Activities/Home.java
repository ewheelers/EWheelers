package com.ewheelers.ewheelers.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ewheelers.ewheelers.Fragments.HomeFragment;
import com.ewheelers.ewheelers.Fragments.WalletFragment;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.navigation.NavigationView;
import com.nineoldandroids.animation.AnimatorInflater;
import com.nineoldandroids.animation.AnimatorSet;

import static java.lang.System.exit;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    View mHeaderView;
    public static DrawerLayout drawer;
    RadioGroup radioGroup;
    RadioButton radioButton, radioButto;
    ImageView imageView_logout, menu_icon;
    TextView user_name, user_Is, view_account;
    Button scan_qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String username = new SessionPreference().getStrings(Home.this, SessionPreference.username);
        user_name = findViewById(R.id.username);
        user_name.setText(username);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderView = navigationView.getHeaderView(0);

        user_Is = mHeaderView.findViewById(R.id.userIs);
        view_account = mHeaderView.findViewById(R.id.viewAccount);
        user_Is.setText(username);
        scan_qr = findViewById(R.id.scanQR);
        menu_icon = findViewById(R.id.menuicon);
        radioGroup = findViewById(R.id.bottomLayout);
        imageView_logout = findViewById(R.id.logout);
        int id = radioGroup.getCheckedRadioButtonId();
        radioButto = findViewById(id);
        if (radioButto.isChecked()) {
            FragmentTras();
        }


        /*AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.property_animator);
        set.setTarget(scan_qr);
        set.start();*/

        view_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Shows account activity", Toast.LENGTH_SHORT).show();
            }
        });

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        imageView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionPreference.clearString(Home.this, SessionPreference.userid);
                SessionPreference.clearString(Home.this, SessionPreference.tokenvalue);
                Intent i = new Intent(getApplicationContext(), LoginScreenActivity.class);
                startActivity(i);
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                radioButton = findViewById(id);
                switch (checkedId) {
                    case R.id.home_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                        break;
                    case R.id.wallet_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new WalletFragment()).commit();
                        break;
                    /*case R.id.scan_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new WalletFragment()).commit();
                        break;*/
                    case R.id.messages_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new WalletFragment()).commit();
                        break;
                }
            }
        });
    }

    private void FragmentTras() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
      /*  if (id == R.id.bussinessettings) {
            Intent i = new Intent(getApplicationContext(), signup_two.class);
            startActivity(i);
        }*/
        if (id == R.id.accountsettings) {
            Intent i = new Intent(getApplicationContext(), SetupAccount.class);
            startActivity(i);
        }
        if (id == R.id.banksettings) {
            Intent i = new Intent(Home.this, BankAccountDetails.class);
            startActivity(i);
        }
        if (id == R.id.estoresettings) {
            SessionPreference.clearString(Home.this,SessionPreference.shopaddress);
            SessionPreference.clearString(Home.this,SessionPreference.zipcode);
            SessionPreference.clearString(Home.this,SessionPreference.latitude);
            SessionPreference.clearString(Home.this,SessionPreference.logitude);
            Intent i = new Intent(Home.this, eStoreSettings.class);
            startActivity(i);
        }
        if (id == R.id.parkingaddress) {

        }
        if (id == R.id.chargingaddress) {

        }
        if(id == R.id.logout){
            SessionPreference.clearString(Home.this, SessionPreference.userid);
            SessionPreference.clearString(Home.this, SessionPreference.tokenvalue);
            Intent i = new Intent(getApplicationContext(), LoginScreenActivity.class);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* @Override
    public void onBackPressed(){
        finish();
    }*/
}
