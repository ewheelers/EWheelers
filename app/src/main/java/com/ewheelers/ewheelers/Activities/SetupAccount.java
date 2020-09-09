package com.ewheelers.ewheelers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ewheelers.ewheelers.R;

public class SetupAccount extends AppCompatActivity {
    EditText fulname,mobileno,dob,organization,profile;
    Button nextthree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        fulname = findViewById(R.id.fullname);
        mobileno = findViewById(R.id.phoneno);
        dob = findViewById(R.id.dob);
        organization = findViewById(R.id.organization);
        profile = findViewById(R.id.briefprofile);
        nextthree = findViewById(R.id.next_three);
        nextthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetupAccount.this,BankAccountDetails.class);
                startActivity(i);
                finish();
            }
        });

    }
}
