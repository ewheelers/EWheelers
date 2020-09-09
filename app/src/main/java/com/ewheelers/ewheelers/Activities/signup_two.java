package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ewheelers.ewheelers.R;

public class signup_two extends AppCompatActivity {
    Button next;
    String user_id;
    EditText business_name,person_name,mobile_no,address_one,address_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_two);
        user_id = getIntent().getStringExtra("userid");
        business_name = findViewById(R.id.businessname);
        person_name = findViewById(R.id.personname);
        next = findViewById(R.id.next_three);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),signup_three.class);
                startActivity(i);
            }
        });
    }
}
