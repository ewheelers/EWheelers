package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.Network.VolleySingleton;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreenActivity extends AppCompatActivity implements View.OnClickListener {
    EditText username, password;
    Button login;
    TextView create_accout;
    TextView forget_password;
    String susername, spass;
    SharedPreferences sharedPreferences;
    String token, userName, userImage, reponseMessage,tokenno;
    String getStatus;
    int userId;
    ProgressDialog progressDialog;
    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    //TabLayout tabs;
    //AutoScrollViewPager viewPager;
    InputMethodManager imm;
    private String[] permissions = {"android.permission.CAMERA","android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION","android.permission.CALL_PHONE","com.example.mapdemo.permission.MAPS_RECEIVE","android.permission.ACCESS_NETWORK_STATE","android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.INTERNET"};
    int requestCode = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        progressDialog = new ProgressDialog(LoginScreenActivity.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Login...");
        progressDialog.setCancelable(false);
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        login = findViewById(R.id.login1);
        create_accout = findViewById(R.id.createNewAccout);
        forget_password = findViewById(R.id.forget_password);


        sharedPreferences = getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE);

        tokenno = new SessionPreference().getStrings(this,SessionPreference.tokenvalue);



       /* AutoScrollPagerAdapter autoScrollPagerAdapter = new AutoScrollPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(autoScrollPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        viewPager.startAutoScroll();
        viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        viewPager.setCycle(true);
        viewPager.setAutoScrollDurationFactor(5.00);
        viewPager.setBorderAnimation(false);*/

        login.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        create_accout.setOnClickListener(this);


      /*  KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (!isVisible) {
                    tabs.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                } else {
                    tabs.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                }
            }
        });
*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login1:
                String userName = username.getText().toString();
                String passWord = password.getText().toString();
                if (userName.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Enter User Name", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if (passWord.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Enter Password", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //password.setError("Enter Password");
                }
                else {
                    loginJson(v);
                }
                break;
            case R.id.forget_password:
                try {
                    imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                Intent forget_act = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(forget_act);
                break;
            case R.id.createNewAccout:
               /* try {
                    imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }*/
                Intent j = new Intent(getApplicationContext(), UserRegistrationActivity.class);
                startActivity(j);
                break;
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(LoginScreenActivity.this);

        builder.setMessage("Do you want to exit ?");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loginJson(final View v) {
        try {
            imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String Login_url = API.Login_post;
        progressDialog.show();

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            getStatus = jsonObject.getString("status");

                            if (getStatus.equals("1")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                token = jsonObject1.getString("token");
                                userName = jsonObject1.getString("user_name");
                                userId = jsonObject1.getInt("user_id");
                                userImage = jsonObject1.getString("user_image");
                                Log.i("TokenId", token);
                                SessionPreference.saveString(LoginScreenActivity.this,SessionPreference.tokenvalue,token);
                                SessionPreference.saveString(LoginScreenActivity.this,SessionPreference.username,userName);
                                Intent i = new Intent(getApplicationContext(), Home.class);
                                startActivity(i);
                                finish();
                            } else {
                                reponseMessage = jsonObject.getString("msg");
                                username.setText("");
                                password.setText("");
                                Snackbar.make(v, reponseMessage, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                susername = username.getText().toString();

                spass = password.getText().toString();

                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("username", susername);
                data3.put("password", spass);
                data3.put("userType","2");
                return data3;

            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void showPrivacyPolicies(String url) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        WebView webView = new WebView(this);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        dialog.setView(webView);
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //viewPager.startAutoScroll(AUTO_SCROLL_THRESHOLD_IN_MILLI);
    }

    @Override
    protected void onPause() {
        //viewPager.stopAutoScroll();
        super.onPause();
    }
}
