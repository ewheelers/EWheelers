package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = new SessionPreference().getStrings(SplashScreenActivity.this,SessionPreference.tokenvalue);
                if(token!=null){
                    Toast.makeText(SplashScreenActivity.this, "token generated", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SplashScreenActivity.this, LoginScreenActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashScreenActivity.this, LoginScreenActivity.class);
                    startActivity(i);
                    finish();
                }
            }

        }, 3*1000); // wait for 5 seconds

    }
}
