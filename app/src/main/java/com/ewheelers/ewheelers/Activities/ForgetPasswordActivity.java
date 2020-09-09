package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.Network.VolleySingleton;
import com.ewheelers.ewheelers.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText forget_email;
    Button forget_send;
    TextView view_message;
    String s_user_email;
    SharedPreferences sharedPreferences;

    ProgressDialog progressDialog;
    InputMethodManager imm;

   /* String content = "Your password reset instructions has been sent to your email. " +
            "Please check your spam folder if you do not find it in your inbox." +
            " Please mind that this request is valid only for next 24 hours.";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        // getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Forgot Password");

        forget_email = findViewById(R.id.forget_password_user_emailid);
        forget_send = findViewById(R.id.forget_mail_send_but);
        view_message = findViewById(R.id.viewMessage);
        forget_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s_user_email = forget_email.getText().toString();
                if (s_user_email.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Enter your Registered Email Id", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //forget_email.setError("Enter your User Name");
                } else {
                    forgetJson(v);
                }

            }
        });

    }

    private void forgetJson(final View v) {
        try {
            imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String Login_url = API.forget_password;
        progressDialog.show();
        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int getStatus = jsonObject.getInt("status");
                            String smsg = jsonObject.getString("msg");
                            if (getStatus != 0) {
                                forget_email.setText("");
                                Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                Intent i = new Intent(ForgetPasswordActivity.this, LoginScreenActivity.class);
                                startActivity(i);
                            } else {
                                forget_email.setText("");
                                Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                s_user_email = forget_email.getText().toString();

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("user_email_username", s_user_email);

                return data3;

            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
