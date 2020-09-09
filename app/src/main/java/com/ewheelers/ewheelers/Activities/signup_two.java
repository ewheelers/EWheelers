package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.snackbar.Snackbar;

import static java.lang.String.*;

public class signup_two extends AppCompatActivity {
    Button next;
    EditText business_name,person_name,mobile_no,address_one,address_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_two);
        business_name = findViewById(R.id.businessname);
        person_name = findViewById(R.id.personname);
        mobile_no = findViewById(R.id.personno);
        address_one = findViewById(R.id.address1);
        address_two = findViewById(R.id.address2);
        next = findViewById(R.id.next_three);
        ScrollView scrollView = findViewById(R.id.scrl);
        String userid = new SessionPreference().getStrings(this,SessionPreference.userid);
        Snackbar.make(scrollView, "Your User ID is : "+userid, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String businessname = business_name.getText().toString();
                String personname = person_name.getText().toString();
                String mobileno = mobile_no.getText().toString();
                String addressone = address_one.getText().toString();
                String addresstwo = address_two.getText().toString();
                if(businessname.isEmpty()||personname.isEmpty()||mobileno.isEmpty()||addressone.isEmpty()||addresstwo.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(v, "Please! Fill all details.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    SessionPreference.saveString(signup_two.this,SessionPreference.businessname,businessname);
                    SessionPreference.saveString(signup_two.this,SessionPreference.personname,personname);
                    SessionPreference.saveString(signup_two.this,SessionPreference.mobileno,mobileno);
                    SessionPreference.saveString(signup_two.this,SessionPreference.addressone,addressone);
                    SessionPreference.saveString(signup_two.this,SessionPreference.addresstwo,addresstwo);
                    Intent i = new Intent(getApplicationContext(), signup_three.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
