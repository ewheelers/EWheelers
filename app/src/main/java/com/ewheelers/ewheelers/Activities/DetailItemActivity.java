package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ewheelers.ewheelers.Network.VolleySingleton;
import com.ewheelers.ewheelers.R;
import com.google.android.material.snackbar.Snackbar;

import java.net.URLEncoder;

public class DetailItemActivity extends AppCompatActivity {
    String brandname,city,model,postedon,imgurl,manyr,regyr,state,pincode,mobileno,userid;
    TextView brandIs,cityIs,modelIs,postedonIs,manyrIs,regyrIs,stateIs,pincodeIs,useridIs;
    NetworkImageView imgurlIs;
    ScrollView scrolLayout;
    Button mobileIs,watsappchat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        scrolLayout = findViewById(R.id.scrol_view);
        brandIs = findViewById(R.id.brand_name);
        brandname = getIntent().getStringExtra("brandname");
        brandIs.setText(brandname);
        cityIs = findViewById(R.id.city_name);
        city = getIntent().getStringExtra("city");
        cityIs.setText(city);
        modelIs = findViewById(R.id.model_name);
        model = getIntent().getStringExtra("model");
        modelIs.setText(model);
        postedonIs = findViewById(R.id.posted_on);
        postedon = getIntent().getStringExtra("postedon");
        postedonIs.setText(postedon);
        imgurlIs = findViewById(R.id.net_img);
        imgurl = getIntent().getStringExtra("imgurl");
        ImageLoader imageLoader = VolleySingleton.getInstance(this).getImageLoader();
        imageLoader.get(imgurl, ImageLoader.getImageListener(imgurlIs, 0, android.R.drawable.ic_dialog_alert));
        imgurlIs.setImageUrl(imgurl, imageLoader);
        manyrIs = findViewById(R.id.manu_yr);
        manyr = getIntent().getStringExtra("manyr");
        manyrIs.setText(manyr);
        regyrIs = findViewById(R.id.reg_year);
        regyr = getIntent().getStringExtra("regyr");
        regyrIs.setText(regyr);
        stateIs = findViewById(R.id.state_name);
        state = getIntent().getStringExtra("state");
        stateIs.setText(state);
        pincodeIs = findViewById(R.id.pincode_name);
        pincode = getIntent().getStringExtra("pincode");
        pincodeIs.setText(pincode);
        mobileIs = findViewById(R.id.call_phone);
        mobileno = getIntent().getStringExtra("mobileno");
        mobileIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()){
                    call_action(mobileno);
                }
            }
        });
        watsappchat = findViewById(R.id.watsapp_chat);
        watsappchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                try {
                    String url = "https://api.whatsapp.com/send?phone=+91"+ mobileno +"&text=" + URLEncoder.encode("My query regarding ...", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        userid = getIntent().getStringExtra("userid");

    }

    public void call_action(String mobileno) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobileno));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        startActivity(callIntent);
    }

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action(mobileno);
                } else {
                    Snackbar snackbar = Snackbar.make(scrolLayout, "Permission denied. Allow phone permission to make a call", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    //Toast.makeText(getApplicationContext(), "Permission denied. Allow permission in App Settings for next Time", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
